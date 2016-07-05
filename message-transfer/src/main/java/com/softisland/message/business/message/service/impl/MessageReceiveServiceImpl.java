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
 * ת����Ϣ����
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

        // ���浽���ݿ���
        dao.saveMessage(message);

        // �������Ϣ������
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

        // ���浽���ݿ���
        dao.saveMessage(message);
        
        // ������Ϣ
        Map<String, Map<String, String>> siteRet = sendSyncMsgToSite(message);

        // ˵��û����Ϣ������
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
            // ��Ϣ�����Լ������Լ�
            if (site.getSiteId().equalsIgnoreCase(message.getFromSite()))
            {
                continue;
            }

            // ���ָ����Ŀ��վ�㣬����Ŀ��վ�����
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
                // ��ÿ��վ�㷢����Ϣ
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
        // �����Ϣ��Դվ���Ƿ���Ч
        if (!checkSiteValid(message.getFromSite()))
        {
            LOG.error("parameter fromSite is invalid, message={}.", message);
            throw new IslandUncheckedException(ErrConstants.ERR_PARAM_INVALID);
        }

        // ���Ŀ��վ���Ƿ���Ч
        if (!CollectionUtils.isEmpty(message.getReceiveSites()))
        {
            // ��Ϣ�����Լ������Լ�
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
