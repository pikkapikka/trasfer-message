/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.business.message.bean.MessageInfo;

/**
 * �����첽��Ϣ��վ�����
 * @author Administrator
 *
 */
public interface IMessageAsynSendService extends IMessageSyncSendService
{    
    /**
     * ��ȡ���ڷ��͵���Ϣ
     * @param dstSite
     * @return
     */
    MessageInfo getSendingMessage(String dstSite);
}
