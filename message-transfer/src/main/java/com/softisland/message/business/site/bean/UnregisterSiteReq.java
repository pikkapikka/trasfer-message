/**
 * 
 */
package com.softisland.message.business.site.bean;

/**
 * @author Administrator
 *
 */
public class UnregisterSiteReq
{
    // վ���ʶ��
    private String siteId;
    
    // ǩ��
    private String sign;

    public String getSiteId()
    {
        return siteId;
    }

    public void setSiteId(String siteId)
    {
        this.siteId = siteId;
    }
    
    public String getSign()
    {
        return sign;
    }
    
    public void setSign(String sign)
    {
        this.sign = sign;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("UnregisterSiteReq [siteId=");
        builder.append(siteId);
        builder.append("]");
        return builder.toString();
    }

}
