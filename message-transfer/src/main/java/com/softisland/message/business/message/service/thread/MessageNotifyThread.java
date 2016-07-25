/**
 * 
 */
package com.softisland.message.business.message.service.thread;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softisland.message.business.message.service.IMessageNotifyService;

/**
 * 通知响应的消息专用线程
 * 
 * @author Administrator
 *
 */
public class MessageNotifyThread extends Thread
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageNotifyThread.class);

    private static final int THREAD_SLEEP_TIME = 100;

    private static final int EXCEPTION_SLEEP_TIME = 1000;

    private IMessageNotifyService msgNotifyService;

    private String fromSite;

    /**
     * 构造函数
     * 
     * @param msgNotifyService 消息通知对象
     * @param 站点ID
     */
    public MessageNotifyThread(String fromSite, IMessageNotifyService msgNotifyService)
    {
        this.fromSite = fromSite;
        this.msgNotifyService = msgNotifyService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        LOG.info("notify response for message thread start, name={}.", getName());

        while (true)
        {
            if (isInterrupted())
            {
                LOG.error("notify thread is interrupted, so it will close. name={}.", getName());
                return;
            }

            try
            {
                // 如果获取消息UUID失败，则可能有异常发送， 那么过点时间再试
                String messageUuid = msgNotifyService.takeMessageUuid(fromSite);
                if (StringUtils.isEmpty(messageUuid))
                {
                    if (sleep(THREAD_SLEEP_TIME))
                    {
                        return;
                    }

                    continue;
                }

                // 尝试发送通知响应
                if (tryNotifyResponse(messageUuid))
                {
                    return;
                }

                // 休眠一下以平滑CPU
                if (sleep(THREAD_SLEEP_TIME))
                {
                    return;
                }
            }
            catch (Exception e)
            {
                LOG.error("send notify message result exception: ", e);
            }
        }
    }

    private boolean sleep(int time)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(time);
            return false;
        }
        catch (InterruptedException e)
        {
            LOG.error("notify thread sleep catch exception, error is:", e);
            return true;
        }
    }

    private boolean tryNotifyResponse(String messageUuid)
    {
        while (true)
        {
            // 如果获取到消息没有目标站点数， 可能消息还没有到，则稍等会再试
            Integer dstNum = msgNotifyService.getDstSiteNumber(messageUuid);
            if ((null == dstNum) || dstNum.intValue() == 0)
            {
                if (sleep(THREAD_SLEEP_TIME))
                {
                    return true;
                }

                continue;
            }

            // 如果目标站点数量 和 得到的响应不一样，则可能还有消息没有响应，等会再试
            if (dstNum.intValue() != msgNotifyService.getMessageResponseSize(messageUuid))
            {
                if (sleep(THREAD_SLEEP_TIME))
                {
                    return true;
                }
                continue;
            }

            try
            {
                // 发送通知消息
                msgNotifyService.sendNotifyMessage(msgNotifyService.getNotifyUrl(messageUuid), messageUuid);

                // 删除缓存的数据
                msgNotifyService.removeMessageResponse(fromSite, messageUuid);
                return false;
            }
            catch (Exception e)
            {
                LOG.error("send notify message failed.", e);
                if (sleep(EXCEPTION_SLEEP_TIME))
                {
                    return true;
                }

                continue;
            }
        }
    }

}
