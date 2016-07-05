/**
 * 
 */
package com.softisland.message.util;

/**
 * ���õĴ�����Ϣ����
 * @author qxf
 *
 */
public final class ErrConstants
{
    private ErrConstants()
    {
    }
    
    /**
     * �ɹ��Ĵ�����
     */
    public static final String RET_SUCCESS = "OK";
    
    /**
     * �����������Ϣ
     */
    public static final String ERR_PARAM_INVALID = "ERR_PARAMETER";
    
    /**
     * MD5����ʧ��
     */
    public static final String ERR_MD5_ENCRYPT = "ERR_ENCRYPT";
    
    public static final String ERR_ACCESS_REDIS = "ERR_REDIS";
    
    public static final String ERR_ACCESS_DB = "ERR_DB";
    
    public static final String ERR_NO_RECEIVER = "ERR_NO_MESSAGE_RECEIVER";
}
