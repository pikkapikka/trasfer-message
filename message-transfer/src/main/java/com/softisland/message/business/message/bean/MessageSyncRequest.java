/**
 * 
 */
package com.softisland.message.business.message.bean;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * ͬ����Ϣ���͵�����
 * 
 * @author Administrator
 *
 */
public class MessageSyncRequest
{
    // ��ϢID
    private String messageId;

    // ��Ϣ��Դվ��
    private String fromSite;

    // ����վ���б� �����ֵΪ�գ��������е�վ�㣨��������ת����Ϣ
    private List<String> receiveSites;

    // ��Ϣ����
    private String messageType;

    // ��Ϣ�壬JSON��ʽ
    private String message;

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
     * �����Ϣ�Ƿ���Ч
     * 
     * @return ��Ч����true����Ч����false
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
