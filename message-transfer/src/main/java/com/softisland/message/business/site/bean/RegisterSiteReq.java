/**
 * 
 */
package com.softisland.message.business.site.bean;

import java.lang.String;

import org.apache.commons.lang3.StringUtils;

/**
 * 站点注册的请求消息体
 * 
 * @author qxf
 *
 */
public class RegisterSiteReq
{
    // 站点标识符
    private String siteId;

    // 站点名称
    private String siteName;

    // 站点IP
    private String siteIp;

    // 站点的通知URL
    private String notifyUrl;

    // 站点接受消息的URL
    private String receiveMsgUrl;

    // 中间件异常时，从各业务组件获取没有发送的消息
    private String getUnsendDataUrl;

    // 中间件定时向业务平台检测其是否运行的URL
    private String pingUrl;

    // 校验值
    private String sign;

    public String getSiteId()
    {
        return siteId;
    }

    public void setSiteId(String siteId)
    {
        this.siteId = siteId;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public String getSiteIp()
    {
        return siteIp;
    }

    public void setSiteIp(String siteIp)
    {
        this.siteIp = siteIp;
    }

    public String getNotifyUrl()
    {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl)
    {
        this.notifyUrl = notifyUrl;
    }

    public String getReceiveMsgUrl()
    {
        return receiveMsgUrl;
    }

    public void setReceiveMsgUrl(String receiveMsgUrl)
    {
        this.receiveMsgUrl = receiveMsgUrl;
    }

    public String getGetUnsendDataUrl()
    {
        return getUnsendDataUrl;
    }

    public void setGetUnsendDataUrl(String getUnsendDataUrl)
    {
        this.getUnsendDataUrl = getUnsendDataUrl;
    }

    public String getPingUrl()
    {
        return pingUrl;
    }

    public void setPingUrl(String pingUrl)
    {
        this.pingUrl = pingUrl;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
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
        builder.append("RegisterSiteReq [siteId=");
        builder.append(siteId);
        builder.append(", siteName=");
        builder.append(siteName);
        builder.append(", siteIp=");
        builder.append(siteIp);
        builder.append(", notifyUrl=");
        builder.append(notifyUrl);
        builder.append(", receiveMsgUrl=");
        builder.append(receiveMsgUrl);
        builder.append(", getUnsendDataUrl=");
        builder.append(getUnsendDataUrl);
        builder.append(", pingUrl=");
        builder.append(pingUrl);
        builder.append("]");
        return builder.toString();
    }

    /**
     * 检测参数是否无效
     * @return true为无效，false为有效
     */
    public boolean isInvalid()
    {
        return StringUtils.isEmpty(siteId) || StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(receiveMsgUrl)
                || StringUtils.isEmpty(getUnsendDataUrl) || StringUtils.isEmpty(pingUrl) || StringUtils.isEmpty(sign);
    }
}
