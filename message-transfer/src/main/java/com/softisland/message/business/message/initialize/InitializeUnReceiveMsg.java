/**
 * 
 */
package com.softisland.message.business.message.initialize;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.softisland.common.utils.HttpClientUtil;
import com.softisland.common.utils.bean.SoftHttpResponse;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.bean.MessageAsynRequest;
import com.softisland.message.business.message.service.IMessageReceiveService;
import com.softisland.message.business.site.service.ISiteBaseService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.init.InitSystemService;

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
            List<MessageAsynRequest> tempMsgs = getMessage(site.getGetUnsendDataUrl());
            if (CollectionUtils.isEmpty(tempMsgs))
            {
                LOG.info("there is no unsend message from site, siteId={}.", site.getSiteId());
                continue;
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
        }
    }

    private List<MessageAsynRequest> getMessage(String url)
    {
        try
        {
            SoftHttpResponse response = HttpClientUtil.getJson(url);
            if (HttpStatus.SC_OK != response.getStatus())
            {
                LOG.error("get message failed, url={}, httpcode={}.", url, response.getStatus());
                throw new IslandUncheckedException(String.valueOf(response.getStatus()));
            }

            String content = response.getContent();
            return JSONObject.parseArray(content, MessageAsynRequest.class);
        }
        catch (IOException e)
        {
            LOG.error("get message catch exception, url=" + url, e);
            throw new IslandUncheckedException(e);
        }
    }

}
