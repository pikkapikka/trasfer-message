/**
 * 
 */
package com.softisland.message.business.message.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softisland.message.business.message.service.IOriginalMessageService;
import com.softisland.message.business.message.service.ISiteMessageService;
import com.softisland.message.business.message.service.thread.MessageDistributeTask;

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
    
    /**
     * �������߳�
     */
    public void init()
    {
        // ������Ϣ�ַ��߳�
        Thread distributeThread = new Thread(new MessageDistributeTask(originalMsgService, siteMsgService));
        distributeThread.setName("message-distribute-thread");
        distributeThread.start();
    }
}
