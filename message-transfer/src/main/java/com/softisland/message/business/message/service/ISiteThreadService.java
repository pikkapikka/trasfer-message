/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.entity.BusinessSite;

/**
 * վ���Ӧվ�������
 * @author qxf
 *
 */
public interface ISiteThreadService
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
