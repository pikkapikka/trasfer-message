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
 * ��Ϣ�м��ϵͳ����
 * 
 * @author Administrator
 *
 */
@SpringBootApplication(scanBasePackages = "com.softisland")
public class MessagePlatApplication
{
    private static final Logger LOG = LoggerFactory.getLogger(MessagePlatApplication.class);

    /**
     * ʵ����redis�����Ӷ���
     * 
     * @return redis���Ӷ���
     */
    @Bean
    public JRedisUtils jRedisUtils()
    {
        final RedisProperties redisProperties = getRedisProperties();
        return JRedisUtils.getInstance(redisProperties);
    }

    /**
     * ʵ����redis����������
     * 
     * @return redis��������
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
     * ʵ����������
     * 
     * @return ������
     */
    @Bean
    public Filter customFilter()
    {
        return new CustomErrorFilter();
    }

    /**
     * ע�������
     * 
     * @return ע����
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
     * �����ʼ���beanʵ��
     * 
     * @return JavaMailSenderImpl
     */
    @Bean
    public JavaMailSenderImpl getJavaMailSenderImpl()
    {
        return new JavaMailSenderImpl();
    }

    /**
     * ���ݿ����ӹ���
     * 
     * @return ���ݿ�����
     */
    @Bean
    public JDBCUtils jdbcUtils()
    {
        return new JDBCUtils();
    }

    /**
     * �����ʼ��Ĺ���
     * 
     * @return EmailUtils
     */
    @Bean
    public EmailUtils emailUtils()
    {
        return new EmailUtils();
    }

    /**
     * ϵͳ��������
     * 
     * @param args ��������
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

        // ����session��ʱʱ�䣬���ֹ��Ϻ�15�����������л�
        System.setProperty("curator-default-session-timeout", "15000");

        ApplicationContext context = SpringApplication.run(MessagePlatApplication.class);
        LOG.info("bean initialize complete....");

        // �����߳�
        InitSystemService initService = context.getBean("initSystemService", InitSystemService.class);
        initService.init();

        LOG.info("system start up now....");
    }

}
