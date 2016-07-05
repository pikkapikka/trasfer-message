/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.business.message.bean.MessageInfo;

/**
 * @author Administrator
 *
 */
public interface IMessageReceiveService
{
    /**
     * 转发消息(异步)
     * @param message 同步消息
     */
    void transferMessageAsyn(MessageInfo message);
    
    /**
     * 转发消息(同步)
     * @param message 异步消息
     */
    String transferMessageSync(MessageInfo message);
}
