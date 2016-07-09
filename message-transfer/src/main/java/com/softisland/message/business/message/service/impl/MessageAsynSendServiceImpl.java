/**
 * 
 */
package com.softisland.message.business.message.service.impl;

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
import com.softisland.message.business.message.bean.MessageForSend;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.dao.IMessageDao;
import com.softisland.message.business.message.service.IMessageNotifyService;
import com.softisland.message.business.message.service.IMessageAsynSendService;
import com.softisland.message.business.site.service.ISiteBaseService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.Constants;

/**
 * 转发异步消息到指定的站点服务
 * 
 * @author qxf
 *
 */
@Service("messageAsynSendSrv")
public class MessageAsynSendServiceImpl implements IMessageAsynSendService
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageAsynSendServiceImpl.class);

    @Autowired
    private ISiteBaseService siteService;

    @Autowired
    private IMessageNotifyService msgNotifyService;

    @Autowired
    private IMessageDao dao;

    @Autowired
    private JRedisUtils redisUtils;

    /**
     * {@inheritDoc}
     */
    public SoftHttpResponse sendToSite(MessageInfo message, String dstSite) throws Exception
    {
        BusinessSite site = siteService.getSiteById(dstSite);
        if (null == site)
        {
            LOG.error("can not find the site, siteId={}.", dstSite);
            saveSendFailed(message, dstSite);

            redisUtils.removeMapValues(Constants.MESSAGE_SENDING_INFO, dstSite);
            return null;
        }

        // 通过http post消息发送消息
        SoftHttpResponse response = sendMessage(message, site);
        if (HttpStatus.SC_OK != response.getStatus())
        {
            LOG.error("send message to target site failed. dstSite={}, url={}, message={}, error={}.", dstSite,
                    site.getReceiveMsgUrl(), message, response.getContent());
        }

        LOG.debug("send message to target site success. dstSite={}, url={}, message={}.", dstSite,
                site.getReceiveMsgUrl(), message);
        if (message.isNeedNotify())
        {
            msgNotifyService.addMessageResult(dstSite, response.getContent(), message,
                    HttpStatus.SC_OK == response.getStatus());
        }

        // 入库保存
        dao.saveTransferResult(message.getUuid(), site.getSiteId(), site.getReceiveMsgUrl(),
                HttpStatus.SC_OK == response.getStatus());

        // 发送成功，删除记录
        redisUtils.removeMapValues(Constants.MESSAGE_SENDING_INFO, dstSite);
        return response;
    }

    private SoftHttpResponse sendMessage(MessageInfo message, BusinessSite site)
    {
        try
        {
            // 入临时库保存
            redisUtils.putValueToMap(Constants.MESSAGE_SENDING_INFO, site.getSiteId(),
                    JSONObject.toJSONString(message));

            // 发送到远端
            return HttpClientUtil.postJsonDataToUrl(site.getReceiveMsgUrl(),
                    JSONObject.toJSONString(getOriginalMessage(message)));
        }
        catch (Exception e)
        {
            LOG.error("send message to target site exception, dstSite={}, url={}, message={}.", site.getSiteId(),
                    site.getReceiveMsgUrl(), message);
            LOG.error("send message to target site exception, error is ", e);
            throw new IslandUncheckedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private MessageForSend getOriginalMessage(MessageInfo message)
    {
        MessageForSend sendMsg = new MessageForSend();
        sendMsg.setFromSite(message.getFromSite());
        sendMsg.setMessage(JSONObject.parseObject(message.getMessage(), Map.class));
        sendMsg.setMessageId(message.getMessageId());
        sendMsg.setMessageType(message.getMessageType());
        sendMsg.setSalt(message.getSalt());
        sendMsg.setTimestamp(message.getTimestamp());
        return sendMsg;
    }

    /**
     * {@inheritDoc}
     */
    public void saveSendFailed(MessageInfo message, String dstSite)
    {
        if (message.isNeedNotify())
        {
            msgNotifyService.addMessageResult(dstSite, "", message, false);
        }

        // 入库保存
        dao.saveTransferResult(message.getUuid(), null, null, false);
    }

    /**
     * {@inheritDoc}
     */
    public MessageInfo getSendingMessage(String dstSite)
    {
        try
        {
            String strMsg = (String) redisUtils.getHashValueByKey(Constants.MESSAGE_SENDING_INFO, dstSite);
            if (StringUtils.isEmpty(strMsg))
            {
                return null;
            }

            return JSONObject.parseObject(strMsg, MessageInfo.class);
        }
        catch (Exception e)
        {
            LOG.error("get sending message failed, dstSite=" + dstSite, e);
            throw new IslandUncheckedException(e);
        }
    }
}
