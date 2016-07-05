/**
 * 
 */
package com.softisland.message.business.message.service.thread;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.service.IOriginalMessageService;
import com.softisland.message.business.message.service.ISiteMessageService;

/**
 * 消息分发任务, 从原始消息队列分发到个站点队列
 * 
 * @author Administrator
 *
 */
public class MessageDistributeTask implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageDistributeTask.class);

    private static final int THRED_SLEEP_TIME = 50;

    private static final int EXCEPTION_SLEEP_TIME = 1000;

    private IOriginalMessageService originalMsgService;

    private ISiteMessageService siteMsgService;

    /**
     * 构造函数
     * 
     * @param originalMsgService 原始消息接口
     * @param siteMsgService 各站点消息接口
     */
    public MessageDistributeTask(IOriginalMessageService originalMsgService, ISiteMessageService siteMsgService)
    {
        this.originalMsgService = originalMsgService;
        this.siteMsgService = siteMsgService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        LOG.info("distribute message thread start.");

        while (true)
        {
            try
            {
                // 从原始接收到的消息队列中，从头部取出消息
                MessageInfo message = originalMsgService.getAvaliableMessage();
                if (null == message)
                {
                    sleep(THRED_SLEEP_TIME);
                    continue;
                }

                // 把消息分发到个站点队列
                tryPullMessage(message);
                
                // 移除已经处理的消息
                removeMessage();

                // 休眠50毫秒，以平滑CPU值
                sleep(THRED_SLEEP_TIME);
            }
            catch (Exception e)
            {
                LOG.error("distribute message catch exception.", e);
                sleep(EXCEPTION_SLEEP_TIME);
            }

        }
    }

    private void tryPullMessage(MessageInfo message)
    {
        while (true)
        {
            try
            {
                // 如果消息分发失败， 可能是因为没有接收者，则等会再试。
                if (!siteMsgService.putMessageToPerSite(message))
                {
                    sleep(EXCEPTION_SLEEP_TIME);
                    continue;
                }

                return;
            }
            catch (Exception e)
            {
                LOG.error("distribute message to site failed, message = {}.", message);
                LOG.error("distribute message to site catch exception.", e);

                sleep(EXCEPTION_SLEEP_TIME);
            }
        }
    }

    private void sleep(int time)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(time);
        }
        catch (InterruptedException e)
        {
            LOG.error("sleep thread catch exception, error is:", e);
        }
    }

    private void removeMessage()
    {
        while (true)
        {
            try
            {
                originalMsgService.removeMessage();
                return;
            }
            catch (Exception e)
            {
                LOG.error("remove message to site catch exception.", e);

                sleep(EXCEPTION_SLEEP_TIME);
            }
        }
    }
}
