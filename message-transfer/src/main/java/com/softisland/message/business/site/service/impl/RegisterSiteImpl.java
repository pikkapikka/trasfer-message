/**
 * 
 */
package com.softisland.message.business.site.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.softisland.bean.utils.JRedisUtils;
import com.softisland.message.business.site.bean.RegisterSiteReq;
import com.softisland.message.business.site.service.ISiteRegisterService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.observer.SiteObservable;
import com.softisland.message.observer.SiteObserverEvent;
import com.softisland.message.util.Constants;
import com.softisland.message.util.SiteEventType;

/**
 * վ��ʵ��ע����
 * @author Administrator
 *
 */
@Service("siteRegisterService")
public class RegisterSiteImpl implements ISiteRegisterService
{
    private static final Logger LOG = LoggerFactory.getLogger(RegisterSiteImpl.class);
    
    // REDIS���ʶ���
    @Autowired
    private JRedisUtils redisUtil;
    
    // ���۲���
    @Autowired
    private SiteObservable observable;

    /**
     * {@inheritDoc}
     */
    public void registerSite(RegisterSiteReq request) throws Exception
    {
        BusinessSite site = new BusinessSite(request);
        site.setRegisterTime(String.valueOf(System.currentTimeMillis()));
        
        // �ظ�ע��
        if (redisUtil.hasKeyInMap(Constants.SITE_REGISTER_INFO, site.getSiteId()))
        {
            return;
        }
        
        // ��վ�㱣�浽redis��
        redisUtil.putValueToMap(Constants.SITE_REGISTER_INFO, request.getSiteId(), JSONObject.toJSONString(site));
        LOG.debug("site register success. site={}.", site);
        
        //TODO ʹ�ü�Ⱥ������Ҫ�޸�
        
        // ������Ϣ��Ϣ֪ͨ
        SiteObserverEvent event = new SiteObserverEvent();
        event.setEventType(SiteEventType.REGISTER);
        event.setSite(site);
        observable.notifyAll(event);
    }

    /**
     * {@inheritDoc}
     */
    public void unregisterSite(String siteId) throws Exception
    {
        if (redisUtil.hasKeyInMap(Constants.SITE_REGISTER_INFO, siteId))
        {
            redisUtil.removeMapValues(Constants.SITE_REGISTER_INFO, siteId);
        }
        else
        {
            LOG.warn("the site which will unregister is not exist, siteId={}.", siteId);
        }
        
        //TODO ʹ�ü�Ⱥ������Ҫ�޸�
        
        // ������Ϣ��Ϣ֪ͨ
        BusinessSite site = new BusinessSite();
        site.setSiteId(siteId);
        
        SiteObserverEvent event = new SiteObserverEvent();
        event.setEventType(SiteEventType.UNREGISTER);
        event.setSite(site);
        observable.notifyAll(event);

    }

}
