/**
 * 
 */
package com.softisland.message.business.message.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.softisland.bean.utils.JRedisUtils;
import com.softisland.common.utils.HttpClientUtil;
import com.softisland.common.utils.bean.SoftHttpResponse;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.dao.IMessageDao;
import com.softisland.message.business.message.service.IMessageNotifyService;
import com.softisland.message.business.site.service.ISiteBaseService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.Constants;

/**
 * 消息通知服务
 * 
 * @author qxf
 *
 */
@Service("messageNotifySrv")
public class MessageNotifyServiceImpl implements IMessageNotifyService
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageNotifyServiceImpl.class);

    @Autowired
    private ISiteBaseService siteService;

    @Autowired
    private IMessageDao dao;

    @Autowired
    private JRedisUtils redisUtils;

    /**
     * {@inheritDoc}
     */
    public synchronized void addMessageResult(String dstSite, String result, MessageInfo message, boolean isSuc)
    {
        // 如果消息结果没有通知URL，则丢弃消息
        // 消息没有通知URL，说明此消息的来源站点已经被取消注册
        String fromSite = null;
        try
        {
            fromSite = (String) redisUtils.getHashValueByKey(Constants.NOTIFY_FROMSITE_INFO, message.getUuid());
        }
        catch (Exception e)
        {
            LOG.error("get from site data exception", e);
        }

        if (StringUtils.isEmpty(fromSite))
        {
            LOG.error("there is no site from receive the notify, so the message will discard. messageUuid={}.",
                    message.getUuid());
            dao.saveNotifyResult(message.getUuid(), null, null, false);
            return;
        }

        // TODO 后续考虑到REDIS异常的话，则需要修改
        try
        {
            // 从缓存中获取现有数据，并添加新的响应结果
            Map<String, Object> rets = buildResult(dstSite, result, message, isSuc);

            redisUtils.putValueToMap(Constants.NOTIFY_RESPONSE_INFO, message.getUuid(), JSONObject.toJSONString(rets));
        }
        catch (Exception e)
        {
            LOG.error("save response data to redis failed, dstSite={}, messageUuid={}, result={}", dstSite, message.getUuid(),
                    result);
            LOG.error("save response data to redis exception", e);
        }
    }
    
    private Map<String, Object> buildResult(String dstSite, String result, MessageInfo message, boolean isSuc)
            throws Exception
    {
        Map<String, String> tempRet = new HashMap<String, String>();
        tempRet.put(Constants.KEY_STATUS, isSuc ? Constants.KEY_SUCCESS : Constants.KEY_FAILED);
        tempRet.put(Constants.KEY_CONTENT, result);

        Map<String, Object> rets = getResponseTempData(message.getUuid());
        if (rets.isEmpty())
        {
            rets.put(Constants.KEY_MESSAGEID, message.getMessageId());

            Map<String, Object> siteRet = new HashMap<String, Object>();
            siteRet.put(dstSite, tempRet);
            rets.put(Constants.KEY_RESULT, siteRet);
        }
        else
        {
            JSONObject siteRet = (JSONObject) rets.get(Constants.KEY_RESULT);
            if (null != siteRet)
            {
                Map<String, Object> maps = new HashMap<String, Object>();
                maps.put(dstSite, tempRet);
                
                siteRet.putAll(maps);
                
                
                
//                Map<String, Object> siteMap = JSONObject.parseObject(siteRet, Map.class);
//                siteMap.put(dstSite, tempRet);
                rets.put(Constants.KEY_RESULT, siteRet);
            }
            else
            {
                LOG.error("can not find the exist resut, rets={}.", rets);
            }
        }

        return rets;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getResponseTempData(String messageUuid) throws Exception
    {
        String response = (String) redisUtils.getHashValueByKey(Constants.NOTIFY_RESPONSE_INFO, messageUuid);
        Map<String, Object> rets = null;
        if (StringUtils.isEmpty(response))
        {
            rets = new HashMap<String, Object>();

        }
        else
        {
            rets = JSONObject.parseObject(response, Map.class);
        }

        return rets;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setNotifyUrl(String fromSite, String messageUuid, int dstNum)
    {
        BusinessSite site = siteService.getSiteById(fromSite);
        if (null == site)
        {
            LOG.error("can not find the site which message come from, so the reponse will be discard. siteId={}.",
                    fromSite);
            dao.saveNotifyResult(messageUuid, null, null, false);
            return;
        }

        // TODO 后续考虑到REDIS异常的话，则需要修改

        try
        {
            // 保存消息队列
            redisUtils.appendValueToList(Constants.NOTIFY_MESSAGE_QUEUE_INFO, messageUuid);

            // 保存目标站点的数量
            redisUtils.putValueToMap(Constants.NOTIFY_DSTSITE_NUM_INFO, messageUuid, String.valueOf(dstNum));

            // 保存通知消息的通知站点
            redisUtils.putValueToMap(Constants.NOTIFY_FROMSITE_INFO, messageUuid, JSONObject.toJSONString(site));
        }
        catch (Exception e)
        {
            LOG.error("save data to redis failed, fromSite={}, messageUuid={}, dstNum={}", fromSite, messageUuid,
                    dstNum);
            LOG.error("save data to redis exception", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Integer getDstSiteNumber(String messageUuid)
    {
        try
        {
            String num = (String) redisUtils.getHashValueByKey(Constants.NOTIFY_DSTSITE_NUM_INFO, messageUuid);
            return StringUtils.isEmpty(num) ? null : Integer.parseInt(num);
        }
        catch (Exception e)
        {
            LOG.error("get dstsite number failed, messageUuid=" + messageUuid, e);
            throw new IslandUncheckedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getNotifyUrl(String messageUuid)
    {
        try
        {
            String site = (String) redisUtils.getHashValueByKey(Constants.NOTIFY_FROMSITE_INFO, messageUuid);
            return JSONObject.parseObject(site, BusinessSite.class).getNotifyUrl();
        }
        catch (Exception e)
        {
            LOG.error("get dstsite number failed, messageUuid=" + messageUuid, e);
            throw new IslandUncheckedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public int getMessageResponseSize(String messageUuid)
    {
        try
        {
            String response = (String) redisUtils.getHashValueByKey(Constants.NOTIFY_RESPONSE_INFO, messageUuid);
            if (StringUtils.isEmpty(response))
            {
                return 0;
            }

            Map<String, Object> rets = JSONObject.parseObject(response, Map.class);
            JSONObject result = (JSONObject)rets.get(Constants.KEY_RESULT);
            return (null == result) ? 0 : result.size();
        }
        catch (Exception e)
        {
            LOG.error("get response number failed, messageUuid=" + messageUuid, e);
            throw new IslandUncheckedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void removeMessageResponse(String messageUuid)
    {
        // 删除响应站点数量
        deleteTempData(Constants.NOTIFY_DSTSITE_NUM_INFO, messageUuid);

        // 删除通知站点信息
        deleteTempData(Constants.NOTIFY_FROMSITE_INFO, messageUuid);

        // 删除响应信息
        deleteTempData(Constants.NOTIFY_RESPONSE_INFO, messageUuid);

        try
        {
            // 从头部移除消息
            redisUtils.lpopValueFromList(Constants.NOTIFY_MESSAGE_QUEUE_INFO);
        }
        catch (Exception e)
        {
            LOG.error("pop message uuid value, messageUuid=" + messageUuid, e);
        }
    }

    private void deleteTempData(String key, String messageUuid)
    {
        try
        {
            redisUtils.removeMapValues(key, messageUuid);
        }
        catch (Exception e)
        {
            LOG.error("delete some temp data from redis failed, messageUuid=" + messageUuid, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String takeMessageUuid()
    {
        try
        {
            // 总是从队列的头部获取信息
            return redisUtils.getValueFromList(Constants.NOTIFY_MESSAGE_QUEUE_INFO, 0);
        }
        catch (Exception e)
        {
            LOG.error("get message uuid from queue exception:", e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sendNotifyMessage(String notifyUrl, String messageUuid) throws Exception
    {
        // 再次校验站点是否存在。
        String strSite = (String) redisUtils.getHashValueByKey(Constants.NOTIFY_FROMSITE_INFO, messageUuid);
        String fromSite = JSONObject.parseObject(strSite, BusinessSite.class).getSiteId();
        BusinessSite site = siteService.getSiteById(fromSite);
        if (null == site)
        {
            LOG.error("can not find the site which message come from, so the reponse will be discard. siteId={}.",
                    fromSite);
            dao.saveNotifyResult(messageUuid, null, null, false);
            return;
        }

        // 获取消息发送的响应消息
        String rets = (String) redisUtils.getHashValueByKey(Constants.NOTIFY_RESPONSE_INFO, messageUuid);

        SoftHttpResponse response = sendMessage(notifyUrl, rets);
        if (HttpStatus.SC_OK != response.getStatus())
        {
            LOG.error("send notify message failed, url is {}, messageUuid={}, content={}.", notifyUrl, messageUuid,
                    response.getContent());
            throw new IslandUncheckedException(String.valueOf(response.getStatus()));
        }

        dao.saveNotifyResult(messageUuid, site.getSiteId(), notifyUrl, true);
        LOG.debug("notify meesage success, notifyUrl={}, messageUuid={}, ret={}.", notifyUrl, messageUuid, rets);
    }

    private SoftHttpResponse sendMessage(String url, String content)
    {
        try
        {
            return HttpClientUtil.postJsonDataToUrl(url, content);
        }
        catch (Exception e)
        {
            LOG.error("send notify message catch exceptiopn, url is {}, content={}.", url, content);
            LOG.error("send notify message catch exceptiopn, error is :", e);
            throw new IslandUncheckedException(e);
        }
    }

}
