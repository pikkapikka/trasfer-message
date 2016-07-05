/**
 * 
 */
package com.softisland.message.business.site.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softisland.message.business.message.service.MessageManagerObserver;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.observer.SiteObservable;
import com.softisland.message.observer.SiteObserverEvent;
import com.softisland.message.util.SiteEventType;

/**
 * 系统启动时，初始化的站点数据
 * 
 * @author Administrator
 *
 */
@Service("initSiteService")
public class InitSiteService
{
    private static final Logger LOG = LoggerFactory.getLogger(InitSiteService.class);

    @Autowired
    private ISiteBaseService siteService;

    // 被观察者
    @Autowired
    private SiteObservable observable;

    @Autowired
    private MessageManagerObserver messageMgrObserver;

    @Autowired
    private SiteObservable siteObservable;

    /**
     * 初始化站点数据
     */
    public void init()
    {
        registerObsever();

        List<BusinessSite> sites = siteService.getAvalibleSite();
        if (CollectionUtils.isEmpty(sites))
        {
            LOG.info("there is no register site.");
            return;
        }

        // TODO 集群模式下要修改
        for (BusinessSite site : sites)
        {
            // 发送消息消息通知
            SiteObserverEvent event = new SiteObserverEvent();
            event.setEventType(SiteEventType.REGISTER);
            event.setSite(site);
            observable.notifyAll(event);
        }
    }

    private void registerObsever()
    {
        // 注册观察者
        siteObservable.addObserver(messageMgrObserver);
    }
}
