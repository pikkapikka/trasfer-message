/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.business.message.bean.MessageInfo;

/**
 * 
 * @author Administrator
 *
 */
public interface IOriginalMessageService
{
    /**
     * 把消息发送的转发队列中
     * @param message
     */
    void putMessageForTransfer(MessageInfo message);
    
    /**
     * 获取可用的消息，如果可以消息可以获取，则此接口会阻塞
     * @return 消息
     */
    MessageInfo getAvaliableMessage();
    
    /**
     * 删除消息
     */
    void removeMessage();
}
