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
     * ����Ϣ���͵�ת��������
     * @param message
     */
    void putMessageForTransfer(MessageInfo message);
    
    /**
     * ��ȡ���õ���Ϣ�����������Ϣ���Ի�ȡ����˽ӿڻ�����
     * @return ��Ϣ
     */
    MessageInfo getAvaliableMessage();
    
    /**
     * ɾ����Ϣ
     */
    void removeMessage();
}
