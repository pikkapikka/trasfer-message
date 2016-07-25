/**
 * 
 */
package com.softisland.message.business.message.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.softisland.bean.utils.JRedisUtils;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.dao.IMessageDao;
import com.softisland.message.business.message.service.IMessageNotifyService;
import com.softisland.message.business.message.service.ISiteMessageService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.Constants;
import com.softisland.message.util.ErrConstants;

/**
 * @author Administrator
 *
 */
@Service("siteMsgService")
public class SiteMessageServiceImpl implements ISiteMessageService
{
    private static final Logger LOG = LoggerFactory.getLogger(SiteMessageServiceImpl.class);

    // 队列长度，10万
    private static final int QUEUE_SIZE = 20000;

    // 每次从数据库中获取数据的数量
    private static final int FETCH_DATE_PER_NUM = 2000;

    private Set<String> sites = new ConcurrentSkipListSet<String>();

    // 记录站点对应的消息队列是否已经满
    private Map<String, Boolean> siteIsFull = new ConcurrentHashMap<String, Boolean>();

    @Autowired
    private IMessageNotifyService notifyService;

    @Autowired
    private IMessageDao dao;

    @Autowired
    private JRedisUtils redisUtils;

    /**
     * {@inheritDoc}
     */
    public boolean putMessageToPerSite(MessageInfo message)
    {
        int receiveSiteNum = 0;
        int dstSiteNotifyNum = 0;

        // TODO Auto-generated method stub
        for (String siteId : sites)
        {
            // 消息不能自己发给自己
            if (siteId.equalsIgnoreCase(message.getFromSite()))
            {
                continue;
            }

            // 如果指定了目标站点，则按照目标站点进行
            List<String> dstSites = message.getReceiveSites();
            if (!CollectionUtils.isEmpty(dstSites))
            {
                if (!dstSites.contains(siteId))
                {
                    continue;
                }
            }

            try
            {
                // 将消息放进消息队列中
                tryPutMessageToQueue(message, siteId);
            }
            catch (Exception e)
            {
                LOG.error("put message to queue failed, message={}, siteId={}.", message, siteId);
                LOG.error("put message to queue catch exception:", e);
                throw new IslandUncheckedException(e);
            }

            receiveSiteNum++;

            if (message.isNeedNotify())
            {
                dstSiteNotifyNum++;
            }
        }

        // 如果没有接收者，则选择重试
        if (0 == receiveSiteNum)
        {
            LOG.error("there is no receiver for message, may try again. message={}.", message);
            return false;
        }

        // 如果消息需要通知,则记录到通知消息中
        if (dstSiteNotifyNum > 0)
        {
            notifyService.setNotifyUrl(message.getFromSite(), message.getUuid(), dstSiteNotifyNum);
        }

        return true;
    }

    /*
     * 尝试将消息队放进消息队列中。 如果消息队列已经满，则将此放进数据表中。
     */
    private void tryPutMessageToQueue(MessageInfo message, String dstSite) throws Exception
    {
        Boolean isFull = siteIsFull.get(dstSite);
        if ((null == isFull) || !isFull.booleanValue())
        {
            // if (!messageQueue.offer(message))
            if (redisUtils.getListSize(Constants.SEND_MESSAGE_FLAG + dstSite) >= QUEUE_SIZE)
            {
                LOG.error("offer message to queue of site failed, may be is full, meesgae={}. site={}.", message,
                        dstSite);
                siteIsFull.put(dstSite, Boolean.TRUE);

                // 存放在临时表
                dao.saveUnSendTempMessage(message, dstSite);
            }
            else
            {
                redisUtils.appendValueToList(Constants.SEND_MESSAGE_FLAG + dstSite, JSONObject.toJSONString(message));
            }
        }
        else
        {
            // 存放在临时表
            dao.saveUnSendTempMessage(message, dstSite);

            // 尝试从临时表的数据移到内存中
            tryFetchDataFromDB(dstSite);
        }
    }

    private void tryFetchDataFromDB(String dstSite) throws Exception
    {
        String lastReceiveTime = null;
        int times = (int) ((QUEUE_SIZE - redisUtils.getListSize(Constants.SEND_MESSAGE_FLAG + dstSite)) / FETCH_DATE_PER_NUM);

        // 从数据库中获取临时表数据，并放入到消息队列中。
        for (int idx = 0; idx < times; idx++)
        {
            List<MessageInfo> messages = dao.fetchMessageFromTempDB(dstSite, idx * FETCH_DATE_PER_NUM,
                    FETCH_DATE_PER_NUM);
            if (messages.isEmpty())
            {
                break;
            }

            for (MessageInfo message : messages)
            {
                // 消息队列此时有空余的空间，应该空余存放数据成功
                redisUtils.appendValueToList(Constants.SEND_MESSAGE_FLAG + dstSite, JSONObject.toJSONString(message));

                // 记录获取的最后一条记录
                lastReceiveTime = String.valueOf(message.getReceiveTime());
            }
        }

        // 尝试删除已经获取的数据
        if (!StringUtils.isEmpty(lastReceiveTime))
        {
            dao.deleteTempMessage(dstSite, lastReceiveTime);
        }

        // 如果从数据库中取空数据，则表示下次数据可以直接存放在消息队列中
        if (0 == dao.countTempMessage(dstSite))
        {
            siteIsFull.put(dstSite, Boolean.FALSE);
        }
    }

    /**
     * {@inheritDoc}
     */
    public MessageInfo getMessage(String siteId)
    {
        try
        {
            String strMsg = redisUtils.lpopValueFromList(Constants.SEND_MESSAGE_FLAG + siteId);
            if (StringUtils.isEmpty(strMsg))
            {
                return null;
            }

            return JSONObject.parseObject(strMsg, MessageInfo.class);
        }
        catch (Exception e)
        {
            LOG.error("take message from queue for site exception, siteId=" + siteId, e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void addSite(BusinessSite site)
    {
        sites.add(site.getSiteId());

        LOG.info("add a message queue for site, siteId={}.", site.getSiteId());
    }

    /**
     * {@inheritDoc}
     */
    public void removeSite(String siteId)
    {
        LOG.debug("begin remove a message queue for site, siteId={}.", siteId);

        try
        {
            // 删除在消息缓存中的消息
            redisUtils.deleteValue(Constants.SEND_MESSAGE_FLAG + siteId);
        }
        catch (Exception e)
        {
            LOG.error("redis connect failed, siteId={}.", siteId);
            throw new IslandUncheckedException(ErrConstants.ERR_ACCESS_REDIS);
        }

        sites.remove(siteId);

        LOG.info("remove a message queue for site, siteId={}.", siteId);
    }

}
