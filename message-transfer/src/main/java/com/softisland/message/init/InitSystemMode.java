/**
 * 
 */
package com.softisland.message.init;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.softisland.curator.client.CuratorFrameworkUtils;
import com.softisland.curator.framework.CuratorCrudUtils;
import com.softisland.message.business.message.initialize.InitializeUnReceiveMsg;
import com.softisland.message.util.Constants;
import com.softisland.message.util.MD5Util;

/**
 * 初始化系统模式
 * 
 * @author Administrator
 *
 */
@Component
public class InitSystemMode
{
    private static final Logger LOG = LoggerFactory.getLogger(InitSystemMode.class);

    private static final String MODE_SINGLETON = "SINGLETON";

    private static final String MODE_HA = "HA";

    private static final int SLEEP_TIME = 1000;

    @Value("${system.mode}")
    private String systemMode;

    @Value("${zookeeper.connection.str}")
    private String zkConStr;
    
    @Value("${message.sign.salt}")
    private String md5Key;

    private CuratorFramework client;

    @Autowired
    private InitializeUnReceiveMsg initUnreceiveMsg;

    @PostConstruct
    public void initSystemMode()
    {
        if (StringUtils.isEmpty(systemMode))
        {
            LOG.error("there is no system mode confige, system exit.");
            System.exit(0);
        }
        
        // 设置加密的盐值
        MD5Util.setMd5Key(md5Key);

        LOG.info("system start mode is {}.", systemMode);

        // 对于单例模式，啥也不需要做
        if (MODE_SINGLETON.equalsIgnoreCase(systemMode))
        {
            initUnreceiveMsg.postInitData();
            return;
        }

        if (MODE_HA.equalsIgnoreCase(systemMode))
        {
            initClient();
            String appId = Constants.getAppId();
            if (StringUtils.isEmpty(appId))
            {
                LOG.error("there is no appId parameter with HA mode, system exit.");
                System.exit(0);
            }

            if (!initHA())
            {
                System.exit(0);
            }
            else
            {
                initUnreceiveMsg.postInitData();
            }
        }
    }

    private boolean initHA()
    {
        boolean isFirst = true;
        while (true)
        {
            try
            {
                createTempData();
                LOG.info("this is the master system.");
                return true;
            }
            catch (Exception e)
            {
                // 如果节点已经存在，则说明此为备节点
                if (e instanceof KeeperException.NodeExistsException)
                {
                    if (isFirst)
                    {
                        LOG.info("this is the slave system.");
                    }

                    isFirst = false;

                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                        continue;
                    }
                    catch (InterruptedException ie)
                    {
                        LOG.info("sleep catch exception:", ie);
                        return false;
                    }
                }

                LOG.error("create temp date exception, system exit.", e);
                return false;
            }
        }
    }
    
    private void initClient()
    {
        CuratorFramework client = CuratorFrameworkUtils.createSimple(zkConStr);
        client.start();
        
        this.client = client;
    }

    private void createTempData() throws Exception
    {
        CuratorCrudUtils.createEphemeral(this.client, "/message-ha-" + Constants.getAppId() + "-session",
                Constants.getAppId().getBytes());
    }

    @PreDestroy
    public void destroyCuratorClient()
    {
        if (null != this.client)
        {
            this.client.close();
        }
    }

}
