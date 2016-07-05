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
 * ϵͳ����ʱ����ʼ����վ������
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

    // ���۲���
    @Autowired
    private SiteObservable observable;

    @Autowired
    private MessageManagerObserver messageMgrObserver;

    @Autowired
    private SiteObservable siteObservable;

    /**
     * ��ʼ��վ������
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

        // TODO ��Ⱥģʽ��Ҫ�޸�
        for (BusinessSite site : sites)
        {
            // ������Ϣ��Ϣ֪ͨ
            SiteObserverEvent event = new SiteObserverEvent();
            event.setEventType(SiteEventType.REGISTER);
            event.setSite(site);
            observable.notifyAll(event);
        }
    }

    private void registerObsever()
    {
        // ע��۲���
        siteObservable.addObserver(messageMgrObserver);
    }
}
