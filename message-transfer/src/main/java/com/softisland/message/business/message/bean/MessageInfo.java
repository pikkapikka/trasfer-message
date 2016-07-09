/**
 * 
 */
package com.softisland.message.business.message.bean;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Administrator
 *
 */
public class MessageInfo
{
    // 消息ID
    private String messageId;

    // 消息来源站点
    private String fromSite;

    // 接收站点列表。 如果此值为空，则向所有的站点（除了自身）转发消息
    private List<String> receiveSites;

    // 消息类型
    private String messageType;

    // 消息体，JSON格式
    private String message;

    // 消息产生的时间戳
    private String timestamp;

    // 校验码
    private String salt;

    // 消息是否可以延迟，暂未使用
    private String delay;

    // 是否需要通知
    private boolean needNotify;

    private String uuid;

    private long receiveTime;

    private boolean asyn;

    public MessageInfo()
    {

    }

    public MessageInfo(MessageAsynRequest request)
    {
        setDelay(request.getDelay());
        setFromSite(request.getFromSite());
        setMessage(JSONObject.toJSONString(request.getMessage()));
        setMessageId(request.getMessageId());
        setMessageType(request.getMessageType());
        setNeedNotify(request.isNeedNotify());
        setReceiveSites(request.getReceiveSites());
        setSalt(request.getSalt());
        setTimestamp(request.getTimestamp());
    }

    public MessageInfo(MessageSyncRequest request)
    {
        setFromSite(request.getFromSite());
        setMessage(JSONObject.toJSONString(request.getMessage()));
        setMessageId(request.getMessageId());
        setMessageType(request.getMessageType());
        setNeedNotify(true);
        setReceiveSites(request.getReceiveSites());
        setSalt(request.getSalt());
        setTimestamp(request.getTimestamp());
    }

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public String getFromSite()
    {
        return fromSite;
    }

    public void setFromSite(String fromSite)
    {
        this.fromSite = fromSite;
    }

    public List<String> getReceiveSites()
    {
        return receiveSites;
    }

    public void setReceiveSites(List<String> receiveSites)
    {
        this.receiveSites = receiveSites;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public String getDelay()
    {
        return delay;
    }

    public void setDelay(String delay)
    {
        this.delay = delay;
    }

    public boolean isNeedNotify()
    {
        return needNotify;
    }

    public void setNeedNotify(boolean needNotify)
    {
        this.needNotify = needNotify;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public long getReceiveTime()
    {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime)
    {
        this.receiveTime = receiveTime;
    }

    public boolean isAsyn()
    {
        return asyn;
    }

    public void setAsyn(boolean asyn)
    {
        this.asyn = asyn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("MessageInfo [uuid=");
        builder.append(uuid);
        builder.append(", receiveTime=");
        builder.append(receiveTime);
        builder.append(", messageId=");
        builder.append(getMessageId());
        builder.append(", fromSite=");
        builder.append(getFromSite());
        builder.append(", receiveSites=");
        builder.append(getReceiveSites());
        builder.append(", delay=");
        builder.append(getDelay());
        builder.append(", needNotify=");
        builder.append(isNeedNotify());
        builder.append(", messageType=");
        builder.append(getMessageType());
        builder.append(", timestamp=");
        builder.append(getTimestamp());
        builder.append(", asyn=");
        builder.append(asyn);
        builder.append("]");
        return builder.toString();
    }
}
