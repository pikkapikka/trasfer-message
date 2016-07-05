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
     * ע��վ��
     * @param request ������Ϣ
     * @throws Exception
     */
    void registerSite(RegisterSiteReq request) throws Exception;
    
    /**
     * ȡ��վ��ע��
     * @param siteId վ��ID
     * @throws Exception
     */
    void unregisterSite(String siteId) throws Exception;
}
