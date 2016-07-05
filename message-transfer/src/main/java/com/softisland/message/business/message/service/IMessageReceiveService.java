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
     * ת����Ϣ(�첽)
     * @param message ͬ����Ϣ
     */
    void transferMessageAsyn(MessageInfo message);
    
    /**
     * ת����Ϣ(ͬ��)
     * @param message �첽��Ϣ
     */
    String transferMessageSync(MessageInfo message);
}
