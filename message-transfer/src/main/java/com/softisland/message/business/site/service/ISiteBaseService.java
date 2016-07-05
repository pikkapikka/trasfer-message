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
     * 获取当前服务的有效站点
     * @return 站点列表
     */
    List<BusinessSite> getAvalibleSite();
    
    /**
     * 根据站点ID获取站点
     * @param siteId
     * @return
     */
    BusinessSite getSiteById(String siteId);
}
