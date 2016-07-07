/**
 * 
 */
package com.softisland.message.business.message.service;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softisland.message.observer.SiteObserverEvent;
import com.softisland.message.util.SiteEventType;
import com.softisland.message.util.SiteRoleType;

/**
 * �۲���
 * 
 * @author Administrator
 *
 */
@Service("messageMgrObserver")
public class MessageManagerObserver implements Observer
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageManagerObserver.class);

    @Autowired
    private ISiteThreadService siteThreadService;
    
    @Autowired
    private ISiteMessageService siteMsgService;

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable observable, Object data)
    {
        if (data instanceof SiteObserverEvent)
        {
            SiteObserverEvent event = (SiteObserverEvent) data;
            if (SiteEventType.REGISTER == event.getEventType())
            {
                if (SiteRoleType.SENDER.getName().equalsIgnoreCase(event.getSite().getRole()))
                {
                    LOG.info("sender site not need to reveive message, siteId={}.", event.getSite().getSiteId());
                    return;
                }
                
                // ������Ϣ����
                siteMsgService.addSite(event.getSite());
                
                // ���ע��վ���¼�
                siteThreadService.addSite(event.getSite());
            }
            else
            {
                // ɾ����Ϣ����
                siteMsgService.removeSite(event.getSite().getSiteId());
                
                // ɾ��վ���¼�
                siteThreadService.removeSite(event.getSite().getSiteId());
            }
        }
        else
        {
            LOG.warn("receive an unkown event, discard it.");
        }
    }

}
