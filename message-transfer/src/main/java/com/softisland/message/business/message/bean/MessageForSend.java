/**
 * 
 */
package com.softisland.message.business.message.bean;

import java.util.Map;

/**
 * 发送给站点的消息
 * 
 * @author Administrator
 *
 */
public class MessageForSend
{
    // 消息ID
    private String messageId;

    // 消息来源站点
    private String fromSite;

    // 消息类型
    private String messageType;

    // 消息体，JSON格式
    private Map<String, Object> message;

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

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public Map<String, Object> getMessage()
    {
        return message;
    }

    public void setMessage(Map<String, Object> message)
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

}
