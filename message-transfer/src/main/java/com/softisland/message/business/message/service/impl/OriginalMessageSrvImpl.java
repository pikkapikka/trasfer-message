/**
 * 
 */
package com.softisland.message.business.message.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.softisland.bean.utils.JRedisUtils;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.service.IOriginalMessageService;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.Constants;

/**
 * @author Administrator
 *
 */
@Service("originalMsgService")
public class OriginalMessageSrvImpl implements IOriginalMessageService
{
    private static final Logger LOG = LoggerFactory.getLogger(OriginalMessageSrvImpl.class);

    @Autowired
    private JRedisUtils redisUtils;

    /**
     * {@inheritDoc}
     */
    public void putMessageForTransfer(MessageInfo message)
    {
        /*
         * ��Ϣ����ڶ��к����߳�ʵʱ�Ӷ����л�ȡͷ�����ݡ�
         */
        try
        {
            redisUtils.appendValueToList(Constants.MESSAGE_ORIGINALQUEUE_INFO, JSONObject.toJSONString(message));
        }
        catch (Exception e)
        {
            LOG.error("put original message to queue failed, message=" + message, e);
            throw new IslandUncheckedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public MessageInfo getAvaliableMessage()
    {
        try
        {
            String message = redisUtils.getValueFromList(Constants.MESSAGE_ORIGINALQUEUE_INFO, 0);
            if (StringUtils.isEmpty(message))
            {
                return null;
            }

            return JSONObject.parseObject(message, MessageInfo.class);
        }
        catch (Exception e)
        {
            LOG.warn("take message from queue failed.", e);
            throw new IslandUncheckedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeMessage()
    {
        try
        {
            redisUtils.lpopValueFromList(Constants.MESSAGE_ORIGINALQUEUE_INFO);
        }
        catch (Exception e)
        {
            LOG.error("put original message to queue failed", e);
            throw new IslandUncheckedException(e);
        }
    }
}
