/**
 * 
 */
package com.softisland.message.app;

import javax.servlet.Filter;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;

import com.softisland.bean.utils.EmailUtils;
import com.softisland.bean.utils.JDBCUtils;
import com.softisland.bean.utils.JRedisUtils;
import com.softisland.message.filter.CustomErrorFilter;
import com.softisland.message.init.InitSystemService;
import com.softisland.message.util.Constants;

/**
 * 消息中间件系统启动
 * 
 * @author Administrator
 *
 */
@SpringBootApplication(scanBasePackages = "com.softisland")
public class MessagePlatApplication
{
    private static final Logger LOG = LoggerFactory.getLogger(MessagePlatApplication.class);

    /**
     * 实例化redis的连接对象
     * 
     * @return redis连接对象
     */
    @Bean
    public JRedisUtils jRedisUtils()
    {
        final RedisProperties redisProperties = getRedisProperties();
        return JRedisUtils.getInstance(redisProperties);
    }

    /**
     * 实例化redis的配置属性
     * 
     * @return redis配置属性
     */
    @Bean
    public RedisProperties getRedisProperties()
    {
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "tomcat.datasource")
    public PoolConfiguration getPoolProperties()
    {
        return new PoolProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "tomcat.datasource")
    public javax.sql.DataSource dataSource()
    {
        final PoolConfiguration poolProperties = getPoolProperties();
        final org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(
                poolProperties);
        return dataSource;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager()
    {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate()
    {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate()
    {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public SimpleJdbcCall simpleJdbcCall()
    {
        return new SimpleJdbcCall(jdbcTemplate());
    }

    /**
     * 实例化过滤器
     * 
     * @return 过滤器
     */
    @Bean
    public Filter customFilter()
    {
        return new CustomErrorFilter();
    }

    /**
     * 注册过滤器
     * 
     * @return 注册器
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean()
    {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(customFilter());
        bean.addUrlPatterns("/softisland/*");
        bean.setName("CustomFilter");
        return bean;
    }

    /**
     * 发送邮件的bean实例
     * 
     * @return JavaMailSenderImpl
     */
    @Bean
    public JavaMailSenderImpl getJavaMailSenderImpl()
    {
        return new JavaMailSenderImpl();
    }

    /**
     * 数据库连接工具
     * 
     * @return 数据库连接
     */
    @Bean
    public JDBCUtils jdbcUtils()
    {
        return new JDBCUtils();
    }

    /**
     * 发送邮件的工具
     * 
     * @return EmailUtils
     */
    @Bean
    public EmailUtils emailUtils()
    {
        return new EmailUtils();
    }

    /**
     * 系统启动函数
     * 
     * @param args 启动参数
     */
    public static void main(final String[] args)
    {
        if ((null != args) && (args.length >= 1))
        {
            System.setProperty("server.port", args[0]);
        }

        if ((null != args) && (args.length >= 2))
        {
            String appId = args[1];
            Constants.setAppId(appId);
        }

        // 设置session超时时间，出现故障后，15秒后进行主备切换
        System.setProperty("curator-default-session-timeout", "15000");

        ApplicationContext context = SpringApplication.run(MessagePlatApplication.class);
        LOG.info("bean initialize complete....");

        // 启动线程
        InitSystemService initService = context.getBean("initSystemService", InitSystemService.class);
        initService.init();

        LOG.info("system start up now....");
    }

}
