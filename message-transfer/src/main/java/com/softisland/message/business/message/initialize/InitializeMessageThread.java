/**
 * 
 */
package com.softisland.message.business.message.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softisland.message.business.message.service.IMessageNotifyService;
import com.softisland.message.business.message.service.IOriginalMessageService;
import com.softisland.message.business.message.service.ISiteMessageService;
import com.softisland.message.business.message.service.thread.MessageDistributeTask;
import com.softisland.message.business.message.service.thread.MessageNotifyTask;

/**
 * �̳߳�ʼ����
 * @author Administrator
 *
 */
@Component("initializeMsgThread")
public class InitializeMessageThread
{
    @Autowired
    private IOriginalMessageService originalMsgService;
    
    @Autowired
    private ISiteMessageService siteMsgService;
    
    @Autowired
    private IMessageNotifyService msgNotifyService;
    
    /**
     * �������߳�
     */
    public void init()
    {
        // ������Ϣ�ַ��߳�
        Thread distributeThread = new Thread(new MessageDistributeTask(originalMsgService, siteMsgService));
        distributeThread.start();
        
        // ����֪ͨ�߳�
        Thread notifyThread = new Thread(new MessageNotifyTask(msgNotifyService));
        notifyThread.start();
    }
}
