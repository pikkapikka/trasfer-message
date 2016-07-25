/**
 * 
 */
package com.softisland.message.business.message.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softisland.message.business.message.service.IMessageNotifyService;
import com.softisland.message.business.message.service.INotifyThreadService;
import com.softisland.message.business.message.service.thread.MessageNotifyThread;
import com.softisland.message.entity.BusinessSite;

/**
 * @author Administrator
 *
 */
@Service("notifyThreadService")
public class NotifyThreadServiceImpl implements INotifyThreadService
{
    private static final Logger LOG = LoggerFactory.getLogger(NotifyThreadServiceImpl.class);

    // �����̺߳�
    private Map<String, MessageNotifyThread> notifyThreads = new ConcurrentHashMap<String, MessageNotifyThread>();
    
    @Autowired
    private IMessageNotifyService msgNotifyService;

    /** {@inheritDoc} */
    @Override
    public void addSite(BusinessSite site)
    {
        // �ظ���Ӳ��ô���
        if (notifyThreads.containsKey(site.getSiteId()))
        {
            return;
        }

        MessageNotifyThread notifyThread = new MessageNotifyThread(site.getSiteId(), msgNotifyService);
        notifyThread.setName("siteId-" + site.getSiteId() + "-notify-message-thread");

        // �����߳�
        notifyThread.start();

        // ��ӵ���Ϣ������
        notifyThreads.put(site.getSiteId(), notifyThread);

        LOG.info("create notify thread for site success. siteId={}.", site.getSiteId());
    }

    @Override
    public void removeSite(String siteId)
    {
        LOG.debug("begin remove a notify thread for site, siteId={}.", siteId);

        MessageNotifyThread notifyThread = notifyThreads.get(siteId);
        if (null != notifyThread)
        {
            // �����߳��ж�
            notifyThread.interrupt();
            LOG.info("send interrupt to notify thread for site, siteId={}.", siteId);

            try
            {
                notifyThread.join();
                LOG.info("notify thread for site is closed, siteId={}.", siteId);
            }
            catch (InterruptedException e)
            {
                LOG.info("wait notify thread to close failed, siteId={}.", siteId);
                LOG.info("wait notify thread to close exception.", e);
            }
        }

        notifyThreads.remove(siteId);
        LOG.info("remove a notify thread for site success. siteId={}.", siteId);
        
    }

}
