/**
 * 
 */
package com.softisland.message.business.message.dao;

import java.util.List;

import com.softisland.message.business.message.bean.MessageInfo;

/**
 * ���ݴ�����ӿ�
 * @author Administrator
 *
 */
public interface IMessageDao
{
    /**
     * ������Ϣ�����ݿ�
     * @param info ��Ϣ��
     */
    void saveMessage(MessageInfo info);
    
    /**
     * ����û�з��͵���ʱ��Ϣ��������ָû�з��͵���Ϣ���е����ݣ�
     * @param info ��Ϣ
     * @param dstSite Ŀ��վ��
     */
    void saveUnSendTempMessage(MessageInfo info, String dstSite);
    
    /**
     * ����ʱ���л�ȡָ��������δ���͵���Ϣ
     * @param dstSite Ŀ��վ��
     * @param start ��ʼ����
     * @param num ����
     * @return ���ؽ��
     */
    List<MessageInfo> fetchMessageFromTempDB(String dstSite, int start, int num);
    
    /**
     * ɾ��ָ������ʱ��Ϣ
     * @param dstSite Ŀ��վ��
     * @param lastReceiveTime ��Ϣ�����һ������ʱ��
     */
    void deleteTempMessage(String dstSite, String lastReceiveTime);
    
    /**
     * ͳ��û�з��͵���Ϣ�ж���
     * @param dstSite Ŀ��վ��
     */
    int countTempMessage(String dstSite);
    
    /**
     * ������Ϣ��֪ͨ���
     * @param messageUuid ��Ϣ��ʶ
     * @param fromSite վ��ID
     * @param siteUrl վ��URL
     * @param isSuc �Ƿ�ɹ�
     */
    void saveNotifyResult(String messageUuid, String fromSite, String siteUrl, boolean isSuc);
    
    /**
     * ������Ϣ�ı���
     * @param messageUuid ��Ϣ��ʾ
     * @param dstSite Ŀ��վ��ID
     * @param siteUrl Ŀ��վ��URL
     * @param isSuc �Ƿ�ɹ�
     */
    void saveTransferResult(String messageUuid, String dstSite, String siteUrl, boolean isSuc);
}
