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
     * 通知消息所有的观察者
     * @param event 事件
     */
    public void notifyAll(SiteObserverEvent event)
    {
        setChanged();
        notifyObservers(event);
    }

}
