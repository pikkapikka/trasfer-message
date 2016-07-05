/**
 * 
 */
package com.softisland.message.util;

/**
 * 常用的错误信息定义
 * @author qxf
 *
 */
public final class ErrConstants
{
    private ErrConstants()
    {
    }
    
    /**
     * 成功的错误码
     */
    public static final String RET_SUCCESS = "OK";
    
    /**
     * 参数错误的信息
     */
    public static final String ERR_PARAM_INVALID = "ERR_PARAMETER";
    
    /**
     * MD5加密失败
     */
    public static final String ERR_MD5_ENCRYPT = "ERR_ENCRYPT";
    
    public static final String ERR_ACCESS_REDIS = "ERR_REDIS";
    
    public static final String ERR_ACCESS_DB = "ERR_DB";
    
    public static final String ERR_NO_RECEIVER = "ERR_NO_MESSAGE_RECEIVER";
}
