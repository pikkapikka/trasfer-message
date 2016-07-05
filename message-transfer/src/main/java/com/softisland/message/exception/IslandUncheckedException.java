/**
 * 
 */
package com.softisland.message.exception;

/**
 * 自定义异常
 * @author qxf
 *
 */
public class IslandUncheckedException extends RuntimeException
{

    /*
     * 序列号 
     */
    private static final long serialVersionUID = 4862850223695414656L;
    
    /**
     * 构造函数
     * @param message 错误消息
     */
    public IslandUncheckedException(String message)
    {
        super(message);
    }
    
    /**
     * 构造函数
     * @param t 异常
     */
    public IslandUncheckedException(Throwable t)
    {
        super(t);
    }

}
