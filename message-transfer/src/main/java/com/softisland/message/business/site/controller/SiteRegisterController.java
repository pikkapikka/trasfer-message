/**
 * 
 */
package com.softisland.message.business.site.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softisland.message.business.site.bean.RegisterSiteReq;
import com.softisland.message.business.site.bean.UnregisterSiteReq;
import com.softisland.message.business.site.service.ISiteRegisterService;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.ErrConstants;
import com.softisland.message.util.MD5Util;

/**
 * վ��ע��/ȡ��ע��Ŀ�����
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/softisland/message/site")
public class SiteRegisterController
{
    private static final Logger LOG = LoggerFactory.getLogger(SiteRegisterController.class);
    
    @Autowired
    private ISiteRegisterService registerService;
    
    /**
     * վ��ע��
     * @param request ������Ϣ��
     * @return ִ�н��
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes="application/json")
    public @ResponseBody String register(@RequestBody RegisterSiteReq request)
    {
        LOG.debug("enter register site, request={}.", request);
        if (request.isInvalid())
        {
            LOG.error("parameter invalid, request={}.", request);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }
        
        // ���MD5ǩ������ֹ�Ƿ�վ��ע��
        if (!request.getSign().equals(MD5Util.sign(request.getSiteId())))
        {
            LOG.error("check salt failed, request={}.", request);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }
        
        try
        {
            // ע��վ��
            registerService.registerSite(request);
        }
        catch (Exception e)
        {
            LOG.error("register site failed, request={}.", request);
            LOG.error("register site catch exception:", e);
            throw new IslandUncheckedException(e);
        }
        
        return ErrConstants.RET_SUCCESS;
    }
    
    /**
     * ȡ��վ��ע��
     * @param request ������Ϣ��
     * @return ִ�н��
     */
    @RequestMapping(value = "/unregister", method = RequestMethod.POST, consumes="application/json")
    public @ResponseBody String unregister(@RequestBody UnregisterSiteReq request)
    {
        LOG.debug("enter unregister site, request={}.", request);
        if (StringUtils.isEmpty(request.getSiteId()))
        {
            LOG.error("parameter invalid, request={}.", request);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }
        
        // ���MD5ǩ������ֹ�Ƿ�վ��ע��
        if (!request.getSign().equals(MD5Util.sign(request.getSiteId())))
        {
            LOG.error("check salt failed, request={}.", request);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }
        
        try
        {
            // ȡ��ע��վ��
            registerService.unregisterSite(request.getSiteId());
        }
        catch (Exception e)
        {
            LOG.error("unregister site failed, request={}.", request);
            LOG.error("unregister site catch exception:", e);
            throw new IslandUncheckedException(e);
        }
        
        return ErrConstants.RET_SUCCESS;
    }
}
