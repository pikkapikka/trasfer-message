/**
 * 
 */
package com.softisland.message.data.app;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.softisland.bean.utils.JDBCUtils;

/**
 * Ϊָ��ʱ��֮ǰ����Ϣ���ݽ����ֱ��Լ���������
 * @author Administrator
 *
 */
@SpringBootApplication(scanBasePackages="com.softisland")
public class MessageDataApplication
{
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
     * ��ں���
     * @param args ���
     */
    public static void main(String[] args)
    {
        SpringApplication.run(MessageDataApplication.class);
    }

}
