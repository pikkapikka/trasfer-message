/**
 * 
 */
package com.softisland.message.business.site.bean;

import java.lang.String;

import org.apache.commons.lang3.StringUtils;

/**
 * վ��ע���������Ϣ��
 * 
 * @author qxf
 *
 */
public class RegisterSiteReq
{
    // վ���ʶ��
    private String siteId;

    // վ������
    private String siteName;

    // վ��IP
    private String siteIp;

    // վ���֪ͨURL
    private String notifyUrl;

    // վ�������Ϣ��URL
    private String receiveMsgUrl;

    // �м���쳣ʱ���Ӹ�ҵ�������ȡû�з��͵���Ϣ
    private String getUnsendDataUrl;

    // �м����ʱ��ҵ��ƽ̨������Ƿ����е�URL
    private String pingUrl;

    // У��ֵ
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
     * �������Ƿ���Ч
     * @return trueΪ��Ч��falseΪ��Ч
     */
    public boolean isInvalid()
    {
        return StringUtils.isEmpty(siteId) || StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(receiveMsgUrl)
                || StringUtils.isEmpty(getUnsendDataUrl) || StringUtils.isEmpty(pingUrl) || StringUtils.isEmpty(sign);
    }
}
