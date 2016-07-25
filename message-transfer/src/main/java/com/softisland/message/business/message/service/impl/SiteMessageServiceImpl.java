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

    // ���г��ȣ�10��
    private static final int QUEUE_SIZE = 20000;

    // ÿ�δ����ݿ��л�ȡ���ݵ�����
    private static final int FETCH_DATE_PER_NUM = 2000;

    private Set<String> sites = new ConcurrentSkipListSet<String>();

    // ��¼վ���Ӧ����Ϣ�����Ƿ��Ѿ���
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
            // ��Ϣ�����Լ������Լ�
            if (siteId.equalsIgnoreCase(message.getFromSite()))
            {
                continue;
            }

            // ���ָ����Ŀ��վ�㣬����Ŀ��վ�����
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
                // ����Ϣ�Ž���Ϣ������
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

        // ���û�н����ߣ���ѡ������
        if (0 == receiveSiteNum)
        {
            LOG.error("there is no receiver for message, may try again. message={}.", message);
            return false;
        }

        // �����Ϣ��Ҫ֪ͨ,���¼��֪ͨ��Ϣ��
        if (dstSiteNotifyNum > 0)
        {
            notifyService.setNotifyUrl(message.getFromSite(), message.getUuid(), dstSiteNotifyNum);
        }

        return true;
    }

    /*
     * ���Խ���Ϣ�ӷŽ���Ϣ�����С� �����Ϣ�����Ѿ������򽫴˷Ž����ݱ��С�
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

                // �������ʱ��
                dao.saveUnSendTempMessage(message, dstSite);
            }
            else
            {
                redisUtils.appendValueToList(Constants.SEND_MESSAGE_FLAG + dstSite, JSONObject.toJSONString(message));
            }
        }
        else
        {
            // �������ʱ��
            dao.saveUnSendTempMessage(message, dstSite);

            // ���Դ���ʱ��������Ƶ��ڴ���
            tryFetchDataFromDB(dstSite);
        }
    }

    private void tryFetchDataFromDB(String dstSite) throws Exception
    {
        String lastReceiveTime = null;
        int times = (int) ((QUEUE_SIZE - redisUtils.getListSize(Constants.SEND_MESSAGE_FLAG + dstSite)) / FETCH_DATE_PER_NUM);

        // �����ݿ��л�ȡ��ʱ�����ݣ������뵽��Ϣ�����С�
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
                // ��Ϣ���д�ʱ�п���Ŀռ䣬Ӧ�ÿ��������ݳɹ�
                redisUtils.appendValueToList(Constants.SEND_MESSAGE_FLAG + dstSite, JSONObject.toJSONString(message));

                // ��¼��ȡ�����һ����¼
                lastReceiveTime = String.valueOf(message.getReceiveTime());
            }
        }

        // ����ɾ���Ѿ���ȡ������
        if (!StringUtils.isEmpty(lastReceiveTime))
        {
            dao.deleteTempMessage(dstSite, lastReceiveTime);
        }

        // ��������ݿ���ȡ�����ݣ����ʾ�´����ݿ���ֱ�Ӵ������Ϣ������
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
            // ɾ������Ϣ�����е���Ϣ
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
