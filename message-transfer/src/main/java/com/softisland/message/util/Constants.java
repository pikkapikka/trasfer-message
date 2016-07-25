/**
 * 
 */
package com.softisland.message.util;

import org.apache.http.HttpStatus;

/**
 * 常量定义文件
 * 
 * @author qxf
 *
 */
public final class Constants
{
    private Constants()
    {
    }

    private static String systemId = null;

    public static void setAppId(String appId)
    {
        systemId = appId;
    }

    public static String getAppId()
    {
        return systemId;
    }

    // 站点注册信息的KEY
    public static final String SITE_REGISTER_INFO = "register_site_island";

    // 保存正在发送的消息
    public static final String MESSAGE_SENDING_INFO = "message_sending_island";

    // 目标站点数量信息
    public static final String NOTIFY_DSTSITE_NUM_INFO = "notify_dstsite_num_island";

    // 原始站点信息
    public static final String NOTIFY_FROMSITE_INFO = "notify_fromsite_island";

    // 消息队列
    public static final String NOTIFY_MESSAGE_QUEUE_INFO = "notify_message_queue_island_";
    
    // 消息队列
    public static final String SEND_MESSAGE_FLAG = "island_message_send_";

    // 各站点响应消息
    public static final String NOTIFY_RESPONSE_INFO = "notify_response_island";

    // 消息临时队列
    public static final String MESSAGE_ORIGINALQUEUE_INFO = "message_original_queue_island";

    public static final String KEY_RESULT = "result";

    public static final String KEY_CONTENT = "content";

    public static final String KEY_MESSAGEID = "messageId";

    public static final String KEY_SUCCESS = "success";

    public static final String KEY_STATUS = "status";

    public static final String KEY_FAILED = "failed";
    
    public static final String QUERY_MESSAGE_LIMIT_PARAM = "?limit=200";

    public static final int PING_MAX_TIMES = 5;
    
    /**
     * 判断当前http返回码是否标示成功
     * @param status 状态码
     * @return 成功返回true，其余为false
     */
    public static boolean isHttpSuc(int status)
    {
        return status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED || status == HttpStatus.SC_ACCEPTED;
    }
}
