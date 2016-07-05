/**
 * 
 */
package com.softisland.message.business.message.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.softisland.message.business.message.service.IMessageAsynSendService;
import com.softisland.message.business.message.service.ISiteMessageService;
import com.softisland.message.business.message.service.ISiteThreadService;
import com.softisland.message.business.message.service.thread.SiteMessageSendThread;
import com.softisland.message.entity.BusinessSite;

/**
 * 站点对应的线程管理服务
 * 
 * @author Administrator
 *
 */
@Service("siteThreadService")
public class SiteThreadServiceImpl implements ISiteThreadService
{
    private static final Logger LOG = LoggerFactory.getLogger(SiteThreadServiceImpl.class);

    // 管理线程和
    private Map<String, SiteMessageSendThread> siteThreads = new ConcurrentHashMap<String, SiteMessageSendThread>();

    @Autowired
    private ISiteMessageService siteMsgService;

    @Autowired
    @Qualifier("messageAsynSendSrv")
    private IMessageAsynSendService sendMsgService;

    /**
     * {@inheritDoc}
     */
    public void addSite(BusinessSite site)
    {
        // 重复添加不用处理
        if (siteThreads.containsKey(site.getSiteId()))
        {
            return;
        }

        SiteMessageSendThread siteThread = new SiteMessageSendThread(site.getSiteId(), siteMsgService, sendMsgService);
        siteThread.setName("siteId-" + site.getSiteId() + "-send-message-thread");

        // 启动线程
        siteThread.start();

        // 添加到消息管理器
        siteThreads.put(site.getSiteId(), siteThread);

        LOG.info("create thread for site success. siteId={}.", site.getSiteId());
    }

    /**
     * {@inheritDoc}
     */
    public void removeSite(String siteId)
    {
        LOG.debug("begin remove a thread for site, siteId={}.", siteId);

        SiteMessageSendThread siteThread = siteThreads.get(siteId);
        if (null != siteThread)
        {
            // 发送线程中断
            siteThread.interrupt();
            LOG.info("send interrupt to thread for site, siteId={}.", siteId);

            try
            {
                siteThread.join();
                LOG.info("thread for site is closed, siteId={}.", siteId);
            }
            catch (InterruptedException e)
            {
                LOG.info("wait thread to close failed, siteId={}.", siteId);
                LOG.info("wait thread to close exception.", e);
            }
        }

        siteThreads.remove(siteId);
        LOG.info("remove a thread for site success. siteId={}.", siteId);
    }

}
