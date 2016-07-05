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
 * ��Ϣ�ַ�����, ��ԭʼ��Ϣ���зַ�����վ�����
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
     * ���캯��
     * 
     * @param originalMsgService ԭʼ��Ϣ�ӿ�
     * @param siteMsgService ��վ����Ϣ�ӿ�
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
                // ��ԭʼ���յ�����Ϣ�����У���ͷ��ȡ����Ϣ
                MessageInfo message = originalMsgService.getAvaliableMessage();
                if (null == message)
                {
                    sleep(THRED_SLEEP_TIME);
                    continue;
                }

                // ����Ϣ�ַ�����վ�����
                tryPullMessage(message);
                
                // �Ƴ��Ѿ��������Ϣ
                removeMessage();

                // ����50���룬��ƽ��CPUֵ
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
                // �����Ϣ�ַ�ʧ�ܣ� ��������Ϊû�н����ߣ���Ȼ����ԡ�
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
