/**
 * 
 */
package com.softisland.message.business.message.service.impl;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.softisland.common.utils.HttpClientUtil;
import com.softisland.common.utils.bean.SoftHttpResponse;
import com.softisland.message.business.message.bean.MessageForSend;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.dao.IMessageDao;
import com.softisland.message.business.message.service.IMessageSyncSendService;
import com.softisland.message.business.site.service.ISiteBaseService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.ErrConstants;

/**
 * 发送同步消息
 * @author Administrator
 *
 */
@Service("messageSyncSendSrv")
public class MessageSyncSendServiceImpl implements IMessageSyncSendService
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageSyncSendServiceImpl.class);
    
    @Autowired
    private ISiteBaseService siteService;
    
    @Autowired
    private IMessageDao dao;

    /** 
     * {@inheritDoc}
     */
    public SoftHttpResponse sendToSite(MessageInfo message, String dstSite) throws Exception
    {
        BusinessSite site = siteService.getSiteById(dstSite);
        if (null == site)
        {
            LOG.error("can not find the site, siteId={}.", dstSite);
            dao.saveTransferResult(message.getUuid(), null, null, false);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
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

        // 入库保存
        dao.saveTransferResult(message.getUuid(), site.getSiteId(), site.getReceiveMsgUrl(),
                HttpStatus.SC_OK == response.getStatus());
        return response;

    }
    
    private SoftHttpResponse sendMessage(MessageInfo message, BusinessSite site)
    {
        try
        {
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

}
