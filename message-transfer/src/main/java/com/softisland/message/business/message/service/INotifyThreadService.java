/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.entity.BusinessSite;

/**
 * ��Ϣ֪ͨ�̹߳������
 * @author Administrator
 *
 */
public interface INotifyThreadService
{
    /**
     * վ����ӵĴ���
     * 
     * @param site
     */
    void addSite(BusinessSite site);

    /**
     * �Ƴ�վ��
     * 
     * @param siteId վ��ID
     */
    void removeSite(String siteId);
}
