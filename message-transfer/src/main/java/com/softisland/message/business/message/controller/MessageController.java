/**
 * 
 */
package com.softisland.message.business.message.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.bean.MessageSyncRequest;
import com.softisland.message.business.message.bean.MessageAsynRequest;
import com.softisland.message.business.message.service.IMessageReceiveService;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.ErrConstants;
import com.softisland.message.util.MD5Util;

/**
 * 消息发送的控制器
 * 
 * @author qxf
 *
 */
@Controller
@RequestMapping(value = "/softisland/message")
public class MessageController
{
    // 日志打印对象
    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);

    // x消息转发服务
    @Autowired
    private IMessageReceiveService messageReceiveSrv;

    /**
     * 发送消息的的URL， 异步消息
     * 
     * @param request 请求消息
     * @return 成功返回 OK
     */
    @RequestMapping(value = "/send/asyn", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String sendAsyn(@RequestBody MessageAsynRequest request)
    {
        LOG.debug("receive a asyn message to transfer, request={}.", request);
        if (request.isInvalid())
        {
            LOG.error("parameter is invalid, request={}.", request);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }

        // 检查校验码，已确保消息没有被更改
        if (!request.getSalt().equals(MD5Util.sign(request.getMessageId() + request.getMessageType())))
        {
            LOG.error("check salt failed, request={}.", request);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }

        try
        {
            MessageInfo message = new MessageInfo(request);
            message.setUuid(UUID.randomUUID().toString());
            message.setReceiveTime(System.currentTimeMillis());
            message.setAsyn(true);
            
            // 转发服务
            messageReceiveSrv.transferMessageAsyn(message);
        }
        catch (IslandUncheckedException e)
        {
            LOG.error("transfer asyn message failed, request={}.", request);
            LOG.error("transfer asyn message exception:", e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("transfer asyn message failed, request={}.", request);
            LOG.error("transfer asyn message exception:", e);
            throw new IslandUncheckedException(e);
        }

        return ErrConstants.RET_SUCCESS;
    }
    
    /**
     * 发送消息的的URL， 同步消息
     * 
     * @param request 请求消息
     * @return 成功返回 OK
     */
    @RequestMapping(value = "/send/sync", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String sendSync(@RequestBody MessageSyncRequest request)
    {
        LOG.debug("receive a sync message to transfer, request={}.", request);
        if (request.isInvalid())
        {
            LOG.error("parameter is invalid, request={}.", request);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }

        // 检查校验码，已确保消息没有被更改
        if (!request.getSalt().equals(MD5Util.sign(request.getMessageId() + request.getMessageType())))
        {
            LOG.error("check salt failed, request={}.", request);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }

        try
        {
            MessageInfo message = new MessageInfo(request);
            message.setUuid(UUID.randomUUID().toString());
            message.setReceiveTime(System.currentTimeMillis());
            message.setAsyn(false);
            
            // 转发服务
            return messageReceiveSrv.transferMessageSync(message);
        }
        catch (IslandUncheckedException e)
        {
            LOG.error("transfer sync message failed, request={}.", request);
            LOG.error("transfer sync message exception:", e);
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("transfer sync message failed, request={}.", request);
            LOG.error("transfer sync message exception:", e);
            throw new IslandUncheckedException(e);
        }
    }
}
