/**
 * 
 */
package com.softisland.message.exception;

/**
 * �Զ����쳣
 * @author qxf
 *
 */
public class IslandUncheckedException extends RuntimeException
{

    /*
     * ���к� 
     */
    private static final long serialVersionUID = 4862850223695414656L;
    
    /**
     * ���캯��
     * @param message ������Ϣ
     */
    public IslandUncheckedException(String message)
    {
        super(message);
    }
    
    /**
     * ���캯��
     * @param t �쳣
     */
    public IslandUncheckedException(Throwable t)
    {
        super(t);
    }

}
