/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.business.message.bean.MessageInfo;

/**
 * ��Ϣ��Ӧ���֪ͨ����
 * @author qxf
 *
 */
public interface IMessageNotifyService
{
    /**
     * �����Ӧ��Ϣ���ڴ���
     * @param dstSite Ŀ��վ��
     * @param result ��Ӧ���
     * @param message ��Ϣ����
     * @param isSuc �Ƿ�ɹ�
     */
    void addMessageResult(String dstSite, String result, MessageInfo message, boolean isSuc);
    
    /**
     * ������Ϣ��Ӧ��URL�͵�ַ
     * @param fromSite ��Դվ��
     * @param messageUuid ��ϢUUID
     * @param dstNum Ŀ��վ������
     */
    void setNotifyUrl(String fromSite, String messageUuid, int dstNum);
    
    /**
     * ��ȡĿ��վ������
     * @param messageUuid ��ϢΨһ��ʾ��
     * @return Ŀ��վ������
     */
    Integer getDstSiteNumber(String messageUuid);
    
    /**
     * ��ȡ��Ϣ֪ͨurl
     * @param messageUuid ��ϢΨһ��ʶ��
     * @return URL
     */
    String getNotifyUrl(String messageUuid);
    
    /**
     * ��ȡ��Ӧ�������
     * @param messageUuid ��ϢΨһ��ʶ��
     * @return ��Ϣ��Ӧ���
     */
    int getMessageResponseSize(String messageUuid);
    
    /**
     * �Ƴ���Ϣ�Ľ��
     * @param fromSite ��Ϣ��Դվ��
     * @param messageUuid ��ϢΨһ��ʶ��
     */
    void removeMessageResponse(String fromSite, String messageUuid);
    
    /**
     * ��ȡ��֪ͨ����ϢUUID�� ���û�У���˽ӿ�����
     * @param fromSite ��Ϣ����վ��
     * @return ��ϢUUID
     */
    String takeMessageUuid(String fromSite);
    
    /**
     * ������Ϣ
     * @param notifyUrl
     * @param messageUuid
     */
    void sendNotifyMessage(String notifyUrl, String messageUuid) throws Exception;
}
