/**
 * 
 */
package com.softisland.message.business.message.bean;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 同步消息发送的请求
 * 
 * @author Administrator
 *
 */
public class MessageSyncRequest
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

    /**
     * 检查消息是否无效
     * 
     * @return 无效返回true，有效返回false
     */
    public boolean isInvalid()
    {
        return StringUtils.isEmpty(messageId) || StringUtils.isEmpty(fromSite) || StringUtils.isEmpty(message)
                || StringUtils.isEmpty(messageType) || StringUtils.isEmpty(salt) || StringUtils.isEmpty(timestamp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("MessageSyncRequest [messageId=");
        builder.append(messageId);
        builder.append(", fromSite=");
        builder.append(fromSite);
        builder.append(", receiveSites=");
        builder.append(receiveSites);
        builder.append(", messageType=");
        builder.append(messageType);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append("]");
        return builder.toString();
    }
}
