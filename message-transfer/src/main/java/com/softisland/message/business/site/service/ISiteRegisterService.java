/**
 * 
 */
package com.softisland.message.business.site.service;

import com.softisland.message.business.site.bean.RegisterSiteReq;

/**
 * @author Administrator
 *
 */
public interface ISiteRegisterService
{
    /**
     * 注册站点
     * @param request 请求消息
     * @throws Exception
     */
    void registerSite(RegisterSiteReq request) throws Exception;
    
    /**
     * 取消站点注册
     * @param siteId 站点ID
     * @throws Exception
     */
    void unregisterSite(String siteId) throws Exception;
}
