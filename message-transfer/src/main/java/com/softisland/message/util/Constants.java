/**
 * 
 */
package com.softisland.message.util;

import org.apache.http.HttpStatus;

/**
 * ���������ļ�
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

    // վ��ע����Ϣ��KEY
    public static final String SITE_REGISTER_INFO = "register_site_island";

    // �������ڷ��͵���Ϣ
    public static final String MESSAGE_SENDING_INFO = "message_sending_island";

    // Ŀ��վ��������Ϣ
    public static final String NOTIFY_DSTSITE_NUM_INFO = "notify_dstsite_num_island";

    // ԭʼվ����Ϣ
    public static final String NOTIFY_FROMSITE_INFO = "notify_fromsite_island";

    // ��Ϣ����
    public static final String NOTIFY_MESSAGE_QUEUE_INFO = "notify_message_queue_island_";
    
    // ��Ϣ����
    public static final String SEND_MESSAGE_FLAG = "island_message_send_";

    // ��վ����Ӧ��Ϣ
    public static final String NOTIFY_RESPONSE_INFO = "notify_response_island";

    // ��Ϣ��ʱ����
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
     * �жϵ�ǰhttp�������Ƿ��ʾ�ɹ�
     * @param status ״̬��
     * @return �ɹ�����true������Ϊfalse
     */
    public static boolean isHttpSuc(int status)
    {
        return status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED || status == HttpStatus.SC_ACCEPTED;
    }
}
