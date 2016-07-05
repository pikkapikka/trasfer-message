/**
 * 
 */
package com.softisland.message.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softisland.message.exception.IslandUncheckedException;

/**
 * MD5加密的工具类
 * 
 * @author Administrator
 *
 */
public final class MD5Util
{
    private static final Logger LOG = LoggerFactory.getLogger(MD5Util.class);

    private static String md5Key = "-softIsland";
    
    private MD5Util()
    {
    }

    /**
     * 签名字符串
     *
     * @param text 需要签名的字符串
     * @return 签名结果
     */
    public static String sign(String text)
    {
        text = text + md5Key;
        try
        {
            return DigestUtils.md5Hex(text.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("sign failed.", e);
            throw new IslandUncheckedException(ErrConstants.ERR_MD5_ENCRYPT);
        }
    }
    
    /**
     * 设置MD5的KEY
     * @param key
     */
    public static void setMd5Key(String key)
    {
        if (!StringUtils.isEmpty(key))
        {
            md5Key = key;
        }
    }
}
