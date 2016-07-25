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
 * ���վ�㷢����Ϣ���߳�
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
     * ���췽��
     * 
     * @param siteId վ��ID
     * @param siteMsgService վ����Ϣ�������
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
        
        // �������ֹͣʱ���ϴ����ڷ��͵���Ϣ��
        // ����߳��жϣ����˳�
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

            // �����ȡ��ϢΪ�գ�˵����ɶ�쳣����������ʱ������
            MessageInfo message = siteMsgService.getMessage(siteId);
            if (null == message)
            {
                // �̱߳��ж����˳�
                if (sleep(THREAD_SLEEP_TIME))
                {
                    return;
                }

                continue;
            }

            // ������Ϣ��ȷ�����ͳɹ�
            if (trySendMessage(message) || sleep(THREAD_SLEEP_TIME))
            {
                // ����̱߳��жϣ�˵��Ŀ��վ��ȡ��ע�ᣬ���¼ʧ�ܵ���־
                // sendMsgService.saveSendFailed(message, siteId);
                return;
            }
        }
    }

    /**
     * ����һ��ʱ��
     * 
     * @param time ʱ��
     * @return ��������жϣ��򷵻�true�����򷵻�false
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

    // ����̱߳��жϣ�����true�����򷵻�false
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

                // �����Ϣ����ʧ�ܣ�������һ��ʱ��������
                if (sleep(EXCEPTION_SLEEP_TIME))
                {
                    return true;
                }
            }
        }
    }

    // ����̱߳��жϣ��򷵻�true�� ���򷵻�false
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

                // �����Ϣ����ʧ�ܣ�������һ��ʱ��������
                if (sleep(EXCEPTION_SLEEP_TIME))
                {
                    return true;
                }

                // �����ȡʧ�ܣ�������redis�����쳣������Ҫ����
                continue;
            }

            // ���û�����ڷ��͵���Ϣ���򷵻�
            if (null == message)
            {
                LOG.info("there is no sending message, siteId={}.", siteId);
                return false;
            }

            return trySendMessage(message);
        }
    }
}
