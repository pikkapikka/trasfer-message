/**
 * 
 */
package com.softisland.message.business.site.service;

import java.util.List;

import com.softisland.message.entity.BusinessSite;

/**
 * @author Administrator
 *
 */
public interface ISiteBaseService
{
    /**
     * ��ȡ��ǰ�������Чվ��
     * @return վ���б�
     */
    List<BusinessSite> getAvalibleSite();
    
    /**
     * ����վ��ID��ȡվ��
     * @param siteId
     * @return
     */
    BusinessSite getSiteById(String siteId);
}
