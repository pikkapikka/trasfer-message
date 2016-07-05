/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.entity.BusinessSite;

/**
 * 站点对应站点管理器
 * @author qxf
 *
 */
public interface ISiteThreadService
{
    /**
     * 站点添加的处理
     * 
     * @param site
     */
    void addSite(BusinessSite site);

    /**
     * 移除站点
     * 
     * @param siteId 站点ID
     */
    void removeSite(String siteId);
}
