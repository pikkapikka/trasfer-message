/**
 * 
 */
package com.softisland.message.business.message.initialize;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.softisland.common.utils.HttpClientUtil;
import com.softisland.common.utils.bean.SoftHttpResponse;
import com.softisland.message.business.message.bean.MessageAsynRequest;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.service.IMessageReceiveService;
import com.softisland.message.business.site.service.ISiteBaseService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.init.InitSystemService;
import com.softisland.message.util.Constants;
import com.softisland.message.util.SiteRoleType;

/**
 * 初始化没有进程异常期间，各站点没有发送的数据
 * 
 * @author qxf
 *
 */
@Component
public class InitializeUnReceiveMsg
{
    private static final Logger LOG = LoggerFactory.getLogger(InitSystemService.class);
    
    private static final int SLEEP_TIME = 200;

    @Autowired
    private ISiteBaseService siteService;

    @Autowired
    private IMessageReceiveService messageRecService;
    
    public void postInitData()
    {
        try
        {
            init();
        }
        catch (Exception e)
        {
            LOG.error("initialize the unsend data failed, so the system will exit.", e);
            System.exit(0);
        }
    }

    private void init()
    {
        List<BusinessSite> sites = siteService.getAvalibleSite();
        if (CollectionUtils.isEmpty(sites))
        {
            LOG.info("there is no register site.");
            return;
        }

        for (BusinessSite site : sites)
        {
            // 对于接收站点，则不需要获取获取数据
            if (SiteRoleType.RECEIVER.getName().equalsIgnoreCase(site.getRole()))
            {
                continue;
            }
            
            while (true)
            {
                List<MessageAsynRequest> tempMsgs = getMessage(site.getGetUnsendDataUrl());
                if (CollectionUtils.isEmpty(tempMsgs))
                {
                    LOG.info("there is no unsend message from site, siteId={}.", site.getSiteId());
                    break;
                }
                
                for (MessageAsynRequest tempMsg : tempMsgs)
                {
                    if (tempMsg.isInvalid())
                    {
                        LOG.error("message is invalid, it will be discarded.");
                        continue;
                    }
                    
                    MessageInfo message = new MessageInfo(tempMsg);
                    message.setUuid(UUID.randomUUID().toString());
                    message.setReceiveTime(System.currentTimeMillis());
                    message.setAsyn(true);

                    messageRecService.transferMessageAsyn(message);
                }
                
                try
                {
                    // 休眠一点时间
                    TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                }
                catch (InterruptedException e)
                {
                    LOG.error("sleep failed.", e);
                }
            }
            
            LOG.info("initialize un send date for site end, siteId={}", site.getSiteId());
        }
    }

    private List<MessageAsynRequest> getMessage(String url)
    {
        String queryUrl = url + Constants.QUERY_MESSAGE_LIMIT_PARAM; 
        try
        {
            
            SoftHttpResponse response = HttpClientUtil.getJson(queryUrl);
            if (!Constants.isHttpSuc(response.getStatus()))
            {
                LOG.error("get message failed, url={}, httpcode={}.", queryUrl, response.getStatus());
                throw new IslandUncheckedException(String.valueOf(response.getStatus()));
            }

            String content = response.getContent();
            return JSONObject.parseArray(content, MessageAsynRequest.class);
        }
        catch (IOException e)
        {
            LOG.error("get message catch exception, url=" + queryUrl, e);
            throw new IslandUncheckedException(e);
        }
    }

}
