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
 * 站点实现注册类
 * @author Administrator
 *
 */
@Service("siteRegisterService")
public class RegisterSiteImpl implements ISiteRegisterService
{
    private static final Logger LOG = LoggerFactory.getLogger(RegisterSiteImpl.class);
    
    // REDIS访问对象
    @Autowired
    private JRedisUtils redisUtil;
    
    // 被观察者
    @Autowired
    private SiteObservable observable;

    /**
     * {@inheritDoc}
     */
    public void registerSite(RegisterSiteReq request) throws Exception
    {
        BusinessSite site = new BusinessSite(request);
        site.setRegisterTime(String.valueOf(System.currentTimeMillis()));
        
        // 重复注册
        if (redisUtil.hasKeyInMap(Constants.SITE_REGISTER_INFO, site.getSiteId()))
        {
            return;
        }
        
        // 把站点保存到redis中
        redisUtil.putValueToMap(Constants.SITE_REGISTER_INFO, request.getSiteId(), JSONObject.toJSONString(site));
        LOG.debug("site register success. site={}.", site);
        
        //TODO 使用集群部署需要修改
        
        // 发送消息消息通知
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
        
        //TODO 使用集群部署需要修改
        
        // 发送消息消息通知
        BusinessSite site = new BusinessSite();
        site.setSiteId(siteId);
        
        SiteObserverEvent event = new SiteObserverEvent();
        event.setEventType(SiteEventType.UNREGISTER);
        event.setSite(site);
        observable.notifyAll(event);

    }

}
