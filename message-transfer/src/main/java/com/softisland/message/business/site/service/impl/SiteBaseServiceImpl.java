/**
 * 
 */
package com.softisland.message.business.site.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.softisland.bean.utils.JRedisUtils;
import com.softisland.message.business.site.service.ISiteBaseService;
import com.softisland.message.entity.BusinessSite;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.Constants;

/**
 * վ���������
 * 
 * @author Administrator
 *
 */
@Service("baseSiteService")
public class SiteBaseServiceImpl implements ISiteBaseService
{
    private static final Logger LOG = LoggerFactory.getLogger(SiteBaseServiceImpl.class);

    @Autowired
    private JRedisUtils redisUtil;

    /**
     * {@inheritDoc}
     */
    public List<BusinessSite> getAvalibleSite()
    {
        // TODO ֧�ּ�ȺӦ���޸Ĵ˴�
        Map<Object, Object> sitesInRedis = null;
        try
        {
            sitesInRedis = redisUtil.getMapEntries(Constants.SITE_REGISTER_INFO);
        }
        catch (Exception e)
        {
            LOG.error("get data of all site from redis failed.", e);
            throw new IslandUncheckedException(e);
        }
        
        // �����ѯ��������Ϊ��,˵��û��ע��վ��
        if (MapUtils.isEmpty(sitesInRedis))
        {
            LOG.info("there is no data about site from redis.");
            return new ArrayList<BusinessSite>();
        }
        
        List<BusinessSite> sites = new ArrayList<BusinessSite>();
        for (Object object : sitesInRedis.values())
        {
            BusinessSite site  = JSONObject.parseObject(String.valueOf(object), BusinessSite.class);
            sites.add(site);
        }
        
        return sites;
    }

    /**
     * {@inheritDoc}
     */
    public BusinessSite getSiteById(String siteId)
    {
        try
        {
            String siteInfo = (String)redisUtil.getHashValueByKey(Constants.SITE_REGISTER_INFO, siteId);
            if (null == siteInfo)
            {
                LOG.error("can not find the data of site, siteId={}.", siteId);
                return null;
            }
            
            return JSONObject.parseObject(siteInfo, BusinessSite.class);
        }
        catch (Exception e)
        {
            LOG.error("get data of simple site from redis failed. siteId=", siteId);
            LOG.error("get data of simple site from redis exception.", e);
            throw new IslandUncheckedException(e);
        }
    }
    
}
