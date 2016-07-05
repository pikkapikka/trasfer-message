/**
 * 
 */
package com.softisland.message.observer;

import com.softisland.message.entity.BusinessSite;
import com.softisland.message.util.SiteEventType;

/**
 * @author Administrator
 *
 */
public class SiteObserverEvent
{
    private SiteEventType eventType;
    
    private BusinessSite site;

    public SiteEventType getEventType()
    {
        return eventType;
    }

    public void setEventType(SiteEventType eventType)
    {
        this.eventType = eventType;
    }

    public BusinessSite getSite()
    {
        return site;
    }

    public void setSite(BusinessSite site)
    {
        this.site = site;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SiteObserverEvent [eventType=");
        builder.append(eventType);
        builder.append(", site=");
        builder.append(site);
        builder.append("]");
        return builder.toString();
    }
}
