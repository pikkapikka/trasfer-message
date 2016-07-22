/**
 * 
 */
package com.softisland.message.business.ping;

import java.net.URI;
import java.util.TimerTask;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softisland.bean.utils.EmailUtils;
import com.softisland.message.util.Constants;

/**
 * 站点ping的任务类
 * 
 * @author qxf
 *
 */
public class SitePingTask extends TimerTask
{
    private static final Logger LOG = LoggerFactory.getLogger(SitePingTask.class);

    private String siteId;

    private String pingUrl;

    private EmailUtils emailUtils;

    private boolean isSendEmail = false;

    private int timeoutTimes = 0;

    /**
     * 构造函数
     * 
     * @param siteId 站点ID
     * @param pingUrl ping的地址
     * @param emailUtils 邮件发送工具
     */
    public SitePingTask(String siteId, String pingUrl, EmailUtils emailUtils)
    {
        this.siteId = siteId;
        this.pingUrl = pingUrl;
        this.emailUtils = emailUtils;
    }

    public String getSiteId()
    {
        return siteId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            IOUtils.toString(new URI(pingUrl));
            isSendEmail = false;
            timeoutTimes = 0;
        }
        catch (Exception e)
        {
            handlePingFailed(ExceptionUtils.getStackTrace(e));
        }
    }

    private void handlePingFailed(String errorMsg)
    {
        // LOG.error("ping site failed. url={}.", pingUrl);
        if (!isSendEmail)
        {
            LOG.error("ping site failed. url={}, errorMsg={}", pingUrl, errorMsg);
            timeoutTimes++;
            if (timeoutTimes >= Constants.PING_MAX_TIMES)
            {
                LOG.error("ping site failed reach the max time, error is {}.", errorMsg);
                emailUtils.sendErrorEmail("site " + siteId + " offline", errorMsg);
                isSendEmail = true;
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        return result;
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

        SitePingTask other = (SitePingTask) obj;
        if (siteId == null)
        {
            if (other.siteId != null)
            {
                return false;
            }
        }
        else if (!siteId.equals(other.siteId))
        {
            return false;
        }
        return true;
    }

}
