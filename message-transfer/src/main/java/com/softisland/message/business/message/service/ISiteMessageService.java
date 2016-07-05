/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.entity.BusinessSite;

/**
 * ÿ��վ�����Ϣ����
 * @author qxf
 *
 */
public interface ISiteMessageService
{
    /**
     * ������Ϣ��ÿ��վ��
     * @param message ��Ϣ
     */
    boolean putMessageToPerSite(MessageInfo message);
    
    /**
     * �Ӹ���վ������л�ȡ����
     * @param siteId վ��ID
     * @return ��Ϣ
     */
    MessageInfo getMessage(String siteId);
    
    /**
     * վ����ӵĴ���
     * @param site
     */
    void addSite(BusinessSite site);
    
    /**
     * �Ƴ�վ��
     * @param siteId վ��ID
     */
    void removeSite(String siteId);
}
