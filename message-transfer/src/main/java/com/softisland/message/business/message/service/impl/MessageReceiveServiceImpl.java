/**
 * 
 */
package com.softisland.message.business.message.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.softisland.common.utils.bean.SoftHttpResponse;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.dao.IMessageDao;
import com.softisland.message.business.message.service.IMessageReceiveService;
import com.softisland.message.business.message.service.IMessageSyncSendService;
import com.softisland.message.business.message.service.IOriginalMessageService;
import com.softisland.message.business.site.service.ISiteBaseService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.Constants;
import com.softisland.message.util.ErrConstants;

/**
 * 转发消息服务
 * 
 * @author qxf
 *
 */
@Service("messageReceiveService")
public class MessageReceiveServiceImpl implements IMessageReceiveService
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiveServiceImpl.class);

    @Autowired
    private IMessageDao dao;

    @Autowired
    private IOriginalMessageService originalMsgService;

    @Autowired
    private ISiteBaseService siteService;

    @Autowired
    @Qualifier("messageSyncSendSrv")
    private IMessageSyncSendService syncSendService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void transferMessageAsyn(MessageInfo message)
    {
        checkMessageParam(message);

        // 保存到数据库中
        dao.saveMessage(message);

        // 存放在消息队列中
        originalMsgService.putMessageForTransfer(message);
    }

    private boolean checkSiteValid(String siteId)
    {
        return null != siteService.getSiteById(siteId);
    }

    /**
     * {@inheritDoc}
     */
    public String transferMessageSync(MessageInfo message)
    {
        checkMessageParam(message);

        // 保存到数据库中
        dao.saveMessage(message);
        
        // 发送消息
        Map<String, Map<String, String>> siteRet = sendSyncMsgToSite(message);

        // 说明没有消息接收者
        if (siteRet.isEmpty())
        {
            LOG.error("there is no receiver for message:{}.", message);
            throw new IslandUncheckedException(ErrConstants.ERR_NO_RECEIVER);
        }

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put(Constants.KEY_MESSAGEID, message.getMessageId());
        ret.put(Constants.KEY_RESULT, siteRet);
        return JSONObject.toJSONString(ret);
    }
    
    private Map<String, Map<String, String>> sendSyncMsgToSite(MessageInfo message)
    {
        Map<String, Map<String, String>> siteRet = new HashMap<String, Map<String, String>>();
        List<BusinessSite> sites = siteService.getAvalibleSite();
        for (BusinessSite site : sites)
        {
            // 消息不能自己发给自己
            if (site.getSiteId().equalsIgnoreCase(message.getFromSite()))
            {
                continue;
            }

            // 如果指定了目标站点，则按照目标站点进行
            List<String> dstSites = message.getReceiveSites();
            if (!CollectionUtils.isEmpty(dstSites))
            {
                if (!dstSites.contains(site.getSiteId()))
                {
                    continue;
                }
            }

            Map<String, String> temp = new HashMap<String, String>();
            try
            {
                // 向每个站点发送消息
                SoftHttpResponse response = syncSendService.sendToSite(message, site.getSiteId());
                temp.put(Constants.KEY_CONTENT, response.getContent());
                temp.put(Constants.KEY_STATUS,
                        HttpStatus.SC_OK == response.getStatus() ? Constants.KEY_SUCCESS : Constants.KEY_FAILED);
                siteRet.put(site.getSiteId(), temp);
            }
            catch (Exception e)
            {
                temp.put(Constants.KEY_CONTENT, "");
                temp.put(Constants.KEY_STATUS, Constants.KEY_FAILED);
                siteRet.put(site.getSiteId(), temp);
            }
        }
        
        return siteRet;
    }

    private void checkMessageParam(MessageInfo message)
    {
        // 检查消息来源站点是否有效
        if (!checkSiteValid(message.getFromSite()))
        {
            LOG.error("parameter fromSite is invalid, message={}.", message);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }

        // 检查目标站点是否有效
        if (!CollectionUtils.isEmpty(message.getReceiveSites()))
        {
            // 消息不能自己发给自己
            if (message.getReceiveSites().contains(message.getFromSite()))
            {
                LOG.error("the receive site contains fromSite, message={}.", message);
                throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
            }

            for (String dstSite : message.getReceiveSites())
            {
                if (!checkSiteValid(dstSite))
                {
                    LOG.error("the one receive site is invalid, message={}.", message);
                    throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
                }
            }
        }
    }
}
