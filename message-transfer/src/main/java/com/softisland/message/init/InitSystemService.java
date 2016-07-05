/**
 * 
 */
package com.softisland.message.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softisland.message.business.message.initialize.InitializeMessageThread;
import com.softisland.message.business.site.service.InitSiteService;

/**
 * ϵͳ��ʼ������
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
     * ��ʼ���ӿ�
     */
    public void init()
    {
        // ��ʼ��ע���վ�����ݵ��ڴ�
        initSiteService.init();

        // ��ʼ����Ϣ�߳�
        initializeMsgThread.init();
    }
}
