/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.business.message.bean.MessageInfo;

/**
 * 发送异步消息到站点服务
 * @author Administrator
 *
 */
public interface IMessageAsynSendService extends IMessageSyncSendService
{    
    /**
     * 获取正在发送的消息
     * @param dstSite
     * @return
     */
    MessageInfo getSendingMessage(String dstSite);
}
