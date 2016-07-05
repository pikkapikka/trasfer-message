/**
 * 
 */
package com.softisland.message.observer;

import java.util.Observable;

import org.springframework.stereotype.Service;

/**
 * 
 * @author Administrator
 *
 */
@Service("siteObservable")
public class SiteObservable extends Observable
{
    /**
     * ֪ͨ��Ϣ���еĹ۲���
     * @param event �¼�
     */
    public void notifyAll(SiteObserverEvent event)
    {
        setChanged();
        notifyObservers(event);
    }

}
