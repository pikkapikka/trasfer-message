/**
 * 
 */
package com.softisland.message.business.message.service.thread;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.service.IMessageAsynSendService;
import com.softisland.message.business.message.service.ISiteMessageService;

/**
 * 向各站点发送消息的线程
 * 
 * @author qxf
 *
 */
public class SiteMessageSendThread extends Thread
{
    private static final Logger LOG = LoggerFactory.getLogger(SiteMessageSendThread.class);

    private static final int THREAD_SLEEP_TIME = 100;

    private static final int EXCEPTION_SLEEP_TIME = 1000;

    private String siteId;

    private ISiteMessageService siteMsgService;

    private IMessageAsynSendService sendMsgService;

    /**
     * 构造方法
     * 
     * @param siteId 站点ID
     * @param siteMsgService 站点消息管理服务
     */
    public SiteMessageSendThread(String siteId, ISiteMessageService siteMsgService,
            IMessageAsynSendService sendMsgService)
    {
        this.siteId = siteId;
        this.siteMsgService = siteMsgService;
        this.sendMsgService = sendMsgService;
    }

    public String getSiteId()
    {
        return siteId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run()
    {
        LOG.info("send message thread start, name={}.", getName());
        
        // 处理进程停止时，上次正在发送的消息。
        // 如果线程中断，则退出
        if (tryDoSendingMessage())
        {
            return;
        }

        while (true)
        {
            if (isInterrupted())
            {
                LOG.error("send message thread is interrupted, so it will close. name={}.", getName());
                return;
            }

            // 如果获取消息为空，说明有啥异常发生，过点时间再试
            MessageInfo message = siteMsgService.getMessage(siteId);
            if (null == message)
            {
                // 线程被中断则退出
                if (sleep(THREAD_SLEEP_TIME))
                {
                    return;
                }

                continue;
            }

            // 发送消息，确保发送成功
            if (trySendMessage(message) || sleep(THREAD_SLEEP_TIME))
            {
                // 如果线程被中断，说明目标站点取消注册，则记录失败的日志
                // sendMsgService.saveSendFailed(message, siteId);
                return;
            }
        }
    }

    /**
     * 休眠一点时间
     * 
     * @param time 时间
     * @return 如果遇到中断，则返回true，否则返回false
     */
    private boolean sleep(int time)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(time);
            return false;
        }
        catch (InterruptedException e)
        {
            LOG.error("sleep thread catch exception, so it will close. name={}.", getName());
            LOG.error("sleep thread catch exception, error is:", e);
            return true;
        }
    }

    // 如果线程被中断，返回true，否则返回false
    private boolean trySendMessage(MessageInfo message)
    {
        while (true)
        {
            try
            {
                sendMsgService.sendToSite(message, siteId);
                return false;
            }
            catch (Exception e)
            {
                LOG.error("send message to site catch exception, error is:", e);

                // 如果消息发送失败，则休眠一点时间再重试
                if (sleep(EXCEPTION_SLEEP_TIME))
                {
                    return true;
                }
            }
        }
    }

    // 如果线程被中断，则返回true， 否则返回false
    private boolean tryDoSendingMessage()
    {
        while (true)
        {
            MessageInfo message = null;
            try
            {
                message = sendMsgService.getSendingMessage(siteId);
            }
            catch (Exception e)
            {
                LOG.error("get sending message catch exception, error is:", e);

                // 如果消息发送失败，则休眠一点时间再重试
                if (sleep(EXCEPTION_SLEEP_TIME))
                {
                    return true;
                }

                // 如果获取失败，可能是redis连接异常，则需要重试
                continue;
            }

            // 如果没有正在发送的消息，则返回
            if (null == message)
            {
                LOG.info("there is no sending message, siteId={}.", siteId);
                return false;
            }

            return trySendMessage(message);
        }
    }
}
