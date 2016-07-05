/**
 * 
 */
package com.softisland.message.entity;

import com.softisland.message.business.site.bean.RegisterSiteReq;

/**
 * 
 * @author qxf
 *
 */
public class BusinessSite extends RegisterSiteReq
{
    // 注册时间
    private String registerTime;

    public String getRegisterTime()
    {
        return registerTime;
    }

    public void setRegisterTime(String registerTime)
    {
        this.registerTime = registerTime;
    }
    
    /**
     * 构造方法
     */
    public BusinessSite()
    {
        
    }
    
    /**
     * 构造方法
     * @param request 参数
     */
    public BusinessSite(RegisterSiteReq request)
    {
        setSiteId(request.getSiteId());
        setSiteName(request.getSiteName());
        setSiteIp(request.getSiteIp());
        setNotifyUrl(request.getNotifyUrl());
        setReceiveMsgUrl(request.getReceiveMsgUrl());
        setGetUnsendDataUrl(request.getGetUnsendDataUrl());
        setPingUrl(request.getPingUrl());
        setSign(request.getSign());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        BusinessSite other = (BusinessSite) obj;
        if (getSiteId() == null)
        {
            if (other.getSiteId() != null)
            {
                return false;
            }
        }
        else if (!getSiteId().equals(other.getSiteId()))
        {
            return false;
        }
        return true;
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
        builder.append("BusinessSite [super=");
        builder.append(", registerTime=");
        builder.append(registerTime);
        builder.append("]");
        return builder.toString();
    }
}
