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
 * ֪ͨ��Ӧ����Ϣר���߳�
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
     * ���캯��
     * 
     * @param msgNotifyService ��Ϣ֪ͨ����
     * @param վ��ID
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
                // �����ȡ��ϢUUIDʧ�ܣ���������쳣���ͣ� ��ô����ʱ������
                String messageUuid = msgNotifyService.takeMessageUuid(fromSite);
                if (StringUtils.isEmpty(messageUuid))
                {
                    if (sleep(THREAD_SLEEP_TIME))
                    {
                        return;
                    }

                    continue;
                }

                // ���Է���֪ͨ��Ӧ
                if (tryNotifyResponse(messageUuid))
                {
                    return;
                }

                // ����һ����ƽ��CPU
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
            // �����ȡ����Ϣû��Ŀ��վ������ ������Ϣ��û�е������ԵȻ�����
            Integer dstNum = msgNotifyService.getDstSiteNumber(messageUuid);
            if ((null == dstNum) || dstNum.intValue() == 0)
            {
                if (sleep(THREAD_SLEEP_TIME))
                {
                    return true;
                }

                continue;
            }

            // ���Ŀ��վ������ �� �õ�����Ӧ��һ��������ܻ�����Ϣû����Ӧ���Ȼ�����
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
                // ����֪ͨ��Ϣ
                msgNotifyService.sendNotifyMessage(msgNotifyService.getNotifyUrl(messageUuid), messageUuid);

                // ɾ�����������
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
