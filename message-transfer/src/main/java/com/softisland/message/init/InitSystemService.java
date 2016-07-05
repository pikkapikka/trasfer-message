/**
 * 
 */
package com.softisland.message.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softisland.message.business.message.initialize.InitializeMessageThread;
import com.softisland.message.business.site.service.InitSiteService;

/**
 * 系统初始化服务
 * 
 * @author qxf
 *
 */
@Component("initSystemService")
public class InitSystemService
{ 
    @Autowired
    private InitializeMessageThread initializeMsgThread;

    @Autowired
    private InitSiteService initSiteService;

    /**
     * 初始化接口
     */
    public void init()
    {
        // 初始化注册的站点数据到内存
        initSiteService.init();

        // 初始化消息线程
        initializeMsgThread.init();
    }
}
