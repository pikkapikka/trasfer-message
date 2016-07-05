/**
 * 
 */
package com.softisland.message.business.message.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.softisland.bean.bean.PageInfo;
import com.softisland.bean.utils.JDBCUtils;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.business.message.dao.IMessageDao;
import com.softisland.message.exception.IslandUncheckedException;
import com.softisland.message.util.DBConstants;
import com.softisland.message.util.ErrConstants;

/**
 * 数据库操作的实现类
 * 
 * @author Administrator
 *
 */
@Repository("messageDao")
public class MessageDaoImpl implements IMessageDao
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageDaoImpl.class);

    // 数据库操作对象
    @Autowired
    private JDBCUtils jdbcUtils;

    /**
     * {@inheritDoc}
     */
    public void saveMessage(MessageInfo info)
    {
        try
        {
            int ret = jdbcUtils.insert(DBConstants.TBL_RECEIVE_MSG, convertMsgParams(info));
            if (ret <= 0)
            {
                LOG.error("save message to DB failed, message={}, ret={}.", info, ret);
                throw new IslandUncheckedException(ErrConstants.ERR_ACCESS_DB);
            }
        }
        catch (Exception e)
        {
            LOG.error("save message to DB failed, message={}.", info);
            LOG.error("save message to DB catch exception:", e);
            throw new IslandUncheckedException(e);
        }
    }

    private Map<String, Object> convertMsgParams(MessageInfo info)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(DBConstants.COL_UUID, info.getUuid());
        map.put(DBConstants.COL_MESSAGEID, info.getMessageId());
        map.put(DBConstants.COL_FROMSITE, info.getFromSite());
        map.put(DBConstants.COL_RECEIVESITES, JSONArray.toJSONString(info.getReceiveSites()));
        map.put(DBConstants.COL_DELAY, info.getDelay());
        map.put(DBConstants.COL_MSGTYPE, info.getMessageType());
        map.put(DBConstants.COL_NEEDNOTIFY, info.isNeedNotify() ? 1 : 0);
        map.put(DBConstants.COL_MESSAGE, info.getMessage());
        map.put(DBConstants.COL_TIMESTAMP, info.getTimestamp());
        map.put(DBConstants.COL_SALT, info.getSalt());
        map.put(DBConstants.COL_ASYN, info.isAsyn() ? 1 : 0);
        map.put(DBConstants.COL_RECEIVETIME, String.valueOf(info.getReceiveTime()));

        return map;
    }

    /**
     * {@inheritDoc}
     */
    public void saveUnSendTempMessage(MessageInfo info, String dstSite)
    {
        Map<String, Object> messageParam = convertMsgParams(info);
        messageParam.put(DBConstants.COL_DSTSITE, dstSite);

        try
        {
            int ret = jdbcUtils.insert(DBConstants.TBL_SEND_MSG_TEMP, messageParam);
            if (ret <= 0)
            {
                LOG.error("save unsend temp message to DB failed, message={}, ret={}.", info, ret);
                throw new IslandUncheckedException(ErrConstants.ERR_ACCESS_DB);
            }
        }
        catch (Exception e)
        {
            // 如果提示主键冲突，说明数据以及存入临时表，当做成功处理
            if (e instanceof DuplicateKeyException)
            {
                LOG.warn("save some duplicate message to temp, message={}, dstSite={}.", info, dstSite);
                return;
            }

            LOG.error("save unsend temp  message to DB failed, message={}.", info);
            LOG.error("save unsend temp  message to DB catch exception:", e);
            throw new IslandUncheckedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void saveNotifyResult(String messageUuid, String fromSite, String siteUrl, boolean isSuc)
    {
        try
        {
            int ret = jdbcUtils.insert(DBConstants.TBL_NOTIFY_MSG_RESULT,
                    convertNotifyParams(messageUuid, fromSite, siteUrl, isSuc));
            if (ret <= 0)
            {
                LOG.error("save notify message result to DB failed, messageUuid={}, ret={}.", messageUuid, ret);
                throw new IslandUncheckedException(ErrConstants.ERR_ACCESS_DB);
            }
        }
        catch (Exception e)
        {
            // 如果提示主键冲突，说明数据以及存入临时表，当做成功处理
            if (e instanceof DuplicateKeyException)
            {
                LOG.warn("save some duplicate message to notify message, messageUuid={}, fromSite={}.", messageUuid,
                        fromSite);
                return;
            }

            LOG.error("save notify message result to DB failed, messageUuid={}.", messageUuid);
            LOG.error("save notify message result to DB catch exception:", e);
            throw new IslandUncheckedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void saveTransferResult(String messageUuid, String dstSite, String siteUrl, boolean isSuc)
    {
        try
        {
            int ret = jdbcUtils.insert(DBConstants.TBL_SEND_MSG_RESULT,
                    convertSendMsgParams(messageUuid, dstSite, siteUrl, isSuc));
            if (ret <= 0)
            {
                LOG.error("save send message result to DB failed, messageUuid={}, ret={}.", messageUuid, ret);
                throw new IslandUncheckedException(ErrConstants.ERR_ACCESS_DB);
            }
        }
        catch (Exception e)
        {
            LOG.error("save send message result to DB failed, messageUuid={}.", messageUuid);
            LOG.error("save send message result to DB catch exception:", e);
            throw new IslandUncheckedException(e);
        }
    }

    private Map<String, Object> convertNotifyParams(String messageUuid, String siteId, String siteUrl, boolean isSuc)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(DBConstants.COL_MESSAGE_UUID, messageUuid);
        map.put(DBConstants.COL_FROMSITE, siteId);
        map.put(DBConstants.COL_SITEURL, siteUrl);
        map.put(DBConstants.COL_RESULT, isSuc ? 0 : 1);
        return map;
    }

    private Map<String, Object> convertSendMsgParams(String messageUuid, String siteId, String siteUrl, boolean isSuc)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(DBConstants.COL_MESSAGE_UUID, messageUuid);
        map.put(DBConstants.COL_DSTSITE, siteId);
        map.put(DBConstants.COL_SITEURL, siteUrl);
        map.put(DBConstants.COL_RESULT, isSuc ? 0 : 1);
        return map;
    }

    /**
     * {@inheritDoc}
     */
    public List<MessageInfo> fetchMessageFromTempDB(String dstSite, int start, int num)
    {
        // 过滤条件
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put(DBConstants.COL_DSTSITE, dstSite);

        PageInfo pageInfo = null;
        try
        {
            pageInfo = jdbcUtils.queryForPageListOrderByAsc(DBConstants.TBL_SEND_MSG_TEMP, conditions, start, num,
                    DBConstants.COL_RECEIVETIME);
        }
        catch (Exception e)
        {
            LOG.error("fetch unsend message failed, dstSite={}, start={}, limit={}.", dstSite, start, num);
            LOG.error("fetch unsend message catch exception:", e);
            throw new IslandUncheckedException(e);
        }

        // 返回空的消息列表
        if ((null == pageInfo) || CollectionUtils.isEmpty(pageInfo.getList()))
        {
            return new ArrayList<MessageInfo>();
        }

        LOG.debug("fetch {} temp messages from db.", pageInfo.getList().size());
        return convertToMessages(pageInfo.getList());
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTempMessage(String dstSite, String lastReceiveTime)
    {
        try
        {
            int ret = jdbcUtils.update(DBConstants.SQL_DELETE_TEMP_MESSAGE, dstSite, lastReceiveTime);
            LOG.debug("delete {} temp messages from db.", ret);
        }
        catch (Exception e)
        {
            LOG.error("delete unsend message failed, dstSite={}, lastReceiveTime={}.", dstSite, lastReceiveTime);
            LOG.error("delete unsend message catch exception:", e);
            throw new IslandUncheckedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int countTempMessage(String dstSite)
    {
        try
        {
            return jdbcUtils.queryForInt(DBConstants.SQL_COUNT_TEMP_MESSAGE, dstSite);
        }
        catch (Exception e)
        {
            LOG.error("count unsend message failed, dstSite={}, lastReceiveTime={},", dstSite);
            LOG.error("count unsend message catch exception:", e);
            throw new IslandUncheckedException(e);
        }
    }

    private List<MessageInfo> convertToMessages(List<Map<String, Object>> dbDatas)
    {
        List<MessageInfo> messageInfos = new ArrayList<MessageInfo>();
        for (Map<String, Object> data : dbDatas)
        {
            MessageInfo message = new MessageInfo();
            message.setDelay((String) data.get(DBConstants.COL_DELAY));
            message.setFromSite((String) data.get(DBConstants.COL_FROMSITE));
            message.setMessage((String) data.get(DBConstants.COL_MESSAGE));
            message.setMessageId((String) data.get(DBConstants.COL_MESSAGEID));
            message.setMessageType((String) data.get(DBConstants.COL_MSGTYPE));

            // 数据库中，1表示需要通知， 0表示不需要通知
            message.setNeedNotify("1".equals(String.valueOf(data.get(DBConstants.COL_NEEDNOTIFY))));

            String receiveSites = (String) data.get(DBConstants.COL_RECEIVESITES);
            if (!StringUtils.isEmpty(receiveSites))
            {
                message.setReceiveSites(JSONObject.parseArray(receiveSites, String.class));
            }

            message.setReceiveTime(Long.parseLong((String) data.get(DBConstants.COL_RECEIVETIME)));
            message.setSalt((String) data.get(DBConstants.COL_SALT));
            message.setTimestamp((String) data.get(DBConstants.COL_TIMESTAMP));
            message.setUuid((String) data.get(DBConstants.COL_UUID));

            // 1 标示异步，0 标示同步
            message.setAsyn("1".equals(String.valueOf(data.get(DBConstants.COL_ASYN))));
            
            messageInfos.add(message);
        }

        return messageInfos;
    }
}
