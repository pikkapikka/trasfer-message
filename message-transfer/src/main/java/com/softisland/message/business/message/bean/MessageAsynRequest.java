/**
 * 
 */
package com.softisland.message.business.message.bean;

/**
 * 异步消息发送的请求
 * 
 * @author qxf
 *
 */
public class MessageAsynRequest extends MessageSyncRequest
{
    // 消息是否可以延迟，暂未使用
    private String delay;

    // 是否需要通知
    private boolean needNotify;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("MessageAsynRequest [messageId=");
        builder.append(getMessageId());
        builder.append(", fromSite=");
        builder.append(getFromSite());
        builder.append(", receiveSites=");
        builder.append(getReceiveSites());
        builder.append(", delay=");
        builder.append(delay);
        builder.append(", needNotify=");
        builder.append(needNotify);
        builder.append(", messageType=");
        builder.append(getMessageType());
        builder.append(", timestamp=");
        builder.append(getTimestamp());
        builder.append("]");
        return builder.toString();
    }
}
