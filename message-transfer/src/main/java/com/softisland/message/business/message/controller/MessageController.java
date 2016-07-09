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
 * ��Ϣ���͵Ŀ�����
 * 
 * @author qxf
 *
 */
@Controller
@RequestMapping(value = "/softisland/message")
public class MessageController
{
    // ��־��ӡ����
    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);

    // x��Ϣת������
    @Autowired
    private IMessageReceiveService messageReceiveSrv;

    /**
     * ������Ϣ�ĵ�URL�� �첽��Ϣ
     * 
     * @param request ������Ϣ
     * @return �ɹ����� OK
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

        // ���У���룬��ȷ����Ϣû�б�����
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
            
            // ת������
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
     * ������Ϣ�ĵ�URL�� ͬ����Ϣ
     * 
     * @param request ������Ϣ
     * @return �ɹ����� OK
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

        // ���У���룬��ȷ����Ϣû�б�����
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
            
            // ת������
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
