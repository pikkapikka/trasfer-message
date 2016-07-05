/**
 * 
 */
package com.softisland.message.business.ping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.softisland.bean.utils.EmailUtils;
import com.softisland.message.business.site.service.ISiteBaseService;
import com.softisland.message.entity.BusinessSite;

/**
 * ping��վ��ķ���
 * 
 * @author Administrator
 *
 */
@Service("pingSiteService")
@EnableScheduling
public class PingSiteService
{
    private static final Logger LOG = LoggerFactory.getLogger(PingSiteService.class);

    @Value("${site.ping.init.delay}")
    private int initDelay;

    @Value("${site.ping.fixed.rate}")
    private int fixedDelay;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private ISiteBaseService siteService;

    // �ڴ����
    private Map<String, Timer> tasks = new ConcurrentHashMap<String, Timer>();

    @Scheduled(initialDelay = 2 * 1000, fixedDelay = 3 * 1000)
    public void updateTasks()
    {
        List<BusinessSite> sites = siteService.getAvalibleSite();

        // �����ѯվ��Ϊ�գ������ִ��ping����
        if (CollectionUtils.isEmpty(sites) && !tasks.isEmpty())
        {
            LOG.warn("there is no register site. so will clean the schedule tasks.");
            for (Timer timer : tasks.values())
            {
                timer.cancel();
            }
            
            tasks.clear();
            return;
        }

        // ���վ��û���ڱ�����ڴ������У��򴴽�����
        for (BusinessSite site : sites)
        {
            if (tasks.containsKey(site.getSiteId()))
            {
                continue;
            }

            SitePingTask task = new SitePingTask(site.getSiteId(), site.getPingUrl(), emailUtils);
            Timer timer = new Timer("schedule-task-" + site.getSiteId());
            timer.scheduleAtFixedRate(task, initDelay * 1000, fixedDelay * 1000);
            tasks.put(site.getSiteId(), timer);
        }

        // ���վ�㱻ȡ��ע�ᣬ��Ӧ�ô�������ɾ��
        List<String> removedSites = new ArrayList<String>();
        for (String siteId : tasks.keySet())
        {
            if (!findSite(siteId, sites))
            {
                Timer timer = tasks.get(siteId);
                timer.cancel();
                LOG.warn("cancel timer for site, id={}.", siteId);
                removedSites.add(siteId);
            }
        }

        for (String siteId : removedSites)
        {
            tasks.remove(siteId);
        }

    }
    
    private boolean findSite(String siteId, List<BusinessSite> sites)
    {
        for (BusinessSite site : sites)
        {
            if (siteId.equals(site.getSiteId()))
            {
                return true;
            }
        }

        return false;
    }
}
