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
 * 线程初始化器
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
     * 启动各线程
     */
    public void init()
    {
        // 启动消息分发线程
        Thread distributeThread = new Thread(new MessageDistributeTask(originalMsgService, siteMsgService));
        distributeThread.setName("message-distribute-thread");
        distributeThread.start();
    }
}
