/**
 * 
 */
package com.softisland.message.business.message.bean;

/**
 * @author Administrator
 *
 */
public class MessageInfo extends MessageAsynRequest
{
    private String uuid;
    
    private long receiveTime;
    
    private boolean asyn;
    
    public MessageInfo()
    {
        
    }
    
    public MessageInfo (MessageAsynRequest request)
    {
        setDelay(request.getDelay());
        setFromSite(request.getFromSite());
        setMessage(request.getMessage());
        setMessageId(request.getMessageId());
        setMessageType(request.getMessageType());
        setNeedNotify(request.isNeedNotify());
        setReceiveSites(request.getReceiveSites());
        setSalt(request.getSalt());
        setTimestamp(request.getTimestamp());
    }
    
    public MessageInfo (MessageSyncRequest request)
    {
        setFromSite(request.getFromSite());
        setMessage(request.getMessage());
        setMessageId(request.getMessageId());
        setMessageType(request.getMessageType());
        setNeedNotify(true);
        setReceiveSites(request.getReceiveSites());
        setSalt(request.getSalt());
        setTimestamp(request.getTimestamp());
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

    /* (non-Javadoc)
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
