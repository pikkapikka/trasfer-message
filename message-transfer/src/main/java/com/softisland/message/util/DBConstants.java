/**
 * 
 */
package com.softisland.message.util;

/**
 * 数据库的常量定义
 * 
 * @author qxf
 *
 */
public final class DBConstants
{
    private DBConstants()
    {

    }

    // ----table name ---
    public static final String TBL_RECEIVE_MSG = "tbl_received_message";

    public static final String TBL_SEND_MSG_TEMP = "tbl_message_send_temp";

    public static final String TBL_SEND_MSG_RESULT = "tbl_message_send_result";

    public static final String TBL_NOTIFY_MSG_RESULT = "tbl_message_notify_result";

    // --- colume name ---
    public static final String COL_UUID = "uuid";

    public static final String COL_MESSAGEID = "message_id";

    public static final String COL_FROMSITE = "from_site";

    public static final String COL_RECEIVESITES = "receive_sites";

    public static final String COL_DELAY = "delay";

    public static final String COL_NEEDNOTIFY = "need_notify";

    public static final String COL_MSGTYPE = "message_type";

    public static final String COL_MESSAGE = "message";

    public static final String COL_TIMESTAMP = "timestamp";

    public static final String COL_SALT = "salt";

    public static final String COL_RECEIVETIME = "receive_time";

    public static final String COL_MESSAGE_UUID = "message_uuid";

    public static final String COL_RESULT = "result";

    public static final String COL_SITEURL = "site_url";

    public static final String COL_DSTSITE = "dst_site";
    
    public static final String COL_ASYN = "asyn";

    // -- SQL ---
    public static final String SQL_DELETE_TEMP_MESSAGE = "delete from tbl_message_send_temp "
            + " where dst_site = ? and receive_time <= ? ";
    
    public static final String SQL_COUNT_TEMP_MESSAGE = "select count(1) from tbl_message_send_temp where dst_site = ? ";

}
