/**
 * 
 */
package com.softisland.message.business.message.bean;

import java.util.Map;

/**
 * ���͸�վ�����Ϣ
 * 
 * @author Administrator
 *
 */
public class MessageForSend
{
    // ��ϢID
    private String messageId;

    // ��Ϣ��Դվ��
    private String fromSite;

    // ��Ϣ����
    private String messageType;

    // ��Ϣ�壬JSON��ʽ
    private Map<String, Object> message;

    // ��Ϣ������ʱ���
    private String timestamp;

    // У����
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
