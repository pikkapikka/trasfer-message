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
public class MessageNotifyTask implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageNotifyTask.class);

    private static final int THREAD_SLEEP_TIME = 100;
    
    private static final int EXCEPTION_SLEEP_TIME = 1000;

    private IMessageNotifyService msgNotifyService;

    /**
     * ���캯��
     * 
     * @param msgNotifyService ��Ϣ֪ͨ����
     */
    public MessageNotifyTask(IMessageNotifyService msgNotifyService)
    {
        this.msgNotifyService = msgNotifyService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        LOG.info("notify response for message thread start.");
        
        while (true)
        {
            try
            {
                // �����ȡ��ϢUUIDʧ�ܣ���������쳣���ͣ� ��ô����ʱ������
                String messageUuid = msgNotifyService.takeMessageUuid();
                if (StringUtils.isEmpty(messageUuid))
                {
                    sleep(THREAD_SLEEP_TIME);
                    continue;
                }
                
                // ���Է���֪ͨ��Ӧ
                tryNotifyResponse(messageUuid);
                
                // ����һ����ƽ��CPU
                sleep(THREAD_SLEEP_TIME);
            }
            catch (Exception e)
            {
                LOG.error("send notify message result exception: ", e);
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
            LOG.error("notify thread sleep catch exception, error is:", e);
        }
    }

    private void tryNotifyResponse(String messageUuid)
    {
        while (true)
        {
            // �����ȡ����Ϣû��Ŀ��վ������ ������Ϣ��û�е������ԵȻ�����
            Integer dstNum = msgNotifyService.getDstSiteNumber(messageUuid);
            if ((null == dstNum) || dstNum.intValue() == 0)
            {
                sleep(THREAD_SLEEP_TIME);
                continue;
            }

            // ���Ŀ��վ������ �� �õ�����Ӧ��һ��������ܻ�����Ϣû����Ӧ���Ȼ�����
            if (dstNum.intValue() != msgNotifyService.getMessageResponseSize(messageUuid))
            {
                sleep(THREAD_SLEEP_TIME);
                continue;
            }

            try
            {
                // ����֪ͨ��Ϣ
                msgNotifyService.sendNotifyMessage(msgNotifyService.getNotifyUrl(messageUuid), messageUuid);
                
                // ɾ�����������
                msgNotifyService.removeMessageResponse(messageUuid);
                return;
            }
            catch (Exception e)
            {
                LOG.error("send notify message failed.", e);
                sleep(EXCEPTION_SLEEP_TIME);
                continue;
            }
        }
    }

}
