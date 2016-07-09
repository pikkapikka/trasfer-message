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

    // ��Ϣ�Ƿ�����ӳ٣���δʹ��
    private String delay;

    // �Ƿ���Ҫ֪ͨ
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
