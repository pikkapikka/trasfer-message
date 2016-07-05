/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.entity.BusinessSite;

/**
 * 每个站点的消息管理
 * @author qxf
 *
 */
public interface ISiteMessageService
{
    /**
     * 发送消息到每个站点
     * @param message 消息
     */
    boolean putMessageToPerSite(MessageInfo message);
    
    /**
     * 从各自站点队列中获取数据
     * @param siteId 站点ID
     * @return 消息
     */
    MessageInfo getMessage(String siteId);
    
    /**
     * 站点添加的处理
     * @param site
     */
    void addSite(BusinessSite site);
    
    /**
     * 移除站点
     * @param siteId 站点ID
     */
    void removeSite(String siteId);
}
