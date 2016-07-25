/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.entity.BusinessSite;

/**
 * 消息通知线程管理服务
 * @author Administrator
 *
 */
public interface INotifyThreadService
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
