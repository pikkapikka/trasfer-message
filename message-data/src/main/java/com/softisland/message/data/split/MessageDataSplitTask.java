/**
 * 
 */
package com.softisland.message.data.split;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.softisland.bean.utils.JDBCUtils;

/**
 * @author Administrator
 *
 */
@Component
@EnableScheduling
public class MessageDataSplitTask
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageDataSplitTask.class);

    private static final String SQL_INSERT_RECEIVE_TBL = "create table %s ( select * "
            + "from tbl_received_message m where m.receive_time < %d )";

    private static final String SQL_DELETE_RECEIVE_TBL = "delete from tbl_received_message where receive_time < %d limit 2000";

    private static final String SQL_SEND_RESULT_TBL = "create table %s (select * from tbl_message_send_result where"
            + " exists ( select 1 from %s m, tbl_message_send_result t where m.uuid = t.message_uuid))";

    private static final String SQL_DELETE_SEND_RESULT_TBL = "delete from tbl_message_send_result where"
            + " message_uuid in ( select message_uuid from %s) limit 2000";

    private static final String SQL_NOTIFY_RESULT_TBL = "create table %s (select * from tbl_message_notify_result where"
            + " exists ( select 1 from %s m, tbl_message_notify_result t where m.uuid = t.message_uuid))";

    private static final String SQL_DELETE_NOTIFY_RESULT_TBL = "delete from tbl_message_notify_result where"
            + " message_uuid in ( select message_uuid from %s) limit 2000";

    private static final int DELETE_SLEEP_TIME = 1000;

    @Value("${message.data.split.per.days}")
    private int days;

    @Autowired
    private JDBCUtils jdbcUtils;

    private long times = 0;

    @Scheduled(cron = "0 7 10 * * *")
    public void run()
    {
        if (days <= 0)
        {
            return;
        }

        if (times % days != 0)
        {
            return;
        }

        times++;
        Calendar nowCal = Calendar.getInstance();
        nowCal.add(Calendar.DAY_OF_MONTH, -days);

        long timestamp = nowCal.getTime().getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String tableDate = format.format(nowCal.getTime());

        try
        {
            String tableName = "tbl_received_message_" + tableDate;

            // 拆分消息接收表
            splitReceiveMessage(timestamp, tableName);

            // 拆分消息发送记录表
            splitReceiveSendResult(tableName, tableDate);

            // 拆分消息通知记录
            splitNotifyResult(tableName, tableDate);
        }
        catch (Exception e)
        {
            LOG.error("split data exception:", e);
        }
    }

    private void splitReceiveMessage(long timestamp, String tableName) throws Exception
    {
        String sql = String.format(SQL_INSERT_RECEIVE_TBL, tableName, timestamp);
        LOG.debug(sql);
        jdbcUtils.update(sql);

        String deleteSql = String.format(SQL_DELETE_RECEIVE_TBL, timestamp);
        LOG.debug(deleteSql);
        deleteData(deleteSql);
    }

    private void splitReceiveSendResult(String tableName, String tableDate) throws Exception
    {
        String sql = String.format(SQL_SEND_RESULT_TBL, "tbl_message_send_result_" + tableDate, tableName);
        LOG.debug(sql);
        jdbcUtils.update(sql);

        String deleteSql = String.format(SQL_DELETE_SEND_RESULT_TBL, "tbl_message_send_result_" + tableDate);
        LOG.debug(deleteSql);
        deleteData(deleteSql);
    }

    private void splitNotifyResult(String tableName, String tableDate) throws Exception
    {
        String sql = String.format(SQL_NOTIFY_RESULT_TBL, "tbl_message_notify_result_" + tableDate, tableName);
        LOG.debug(sql);
        jdbcUtils.update(sql);

        String deleteSql = String.format(SQL_DELETE_NOTIFY_RESULT_TBL, "tbl_message_notify_result_" + tableDate);
        LOG.debug(deleteSql);
        deleteData(deleteSql);
    }

    // 每次只删除2000条，以减少表被锁时间。
    private void deleteData(String deleteSql) throws Exception
    {
        while (true)
        {
            int ret = jdbcUtils.update(deleteSql);
            if (ret == 0)
            {
                break;
            }

            // 每次删除数据后，休眠一点时间给其余进程使用
            TimeUnit.MILLISECONDS.sleep(DELETE_SLEEP_TIME);
        }
    }
}
