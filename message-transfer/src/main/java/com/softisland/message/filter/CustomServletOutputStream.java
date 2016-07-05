/**
 * 
 */
package com.softisland.message.filter;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * @author Administrator
 *
 */
public class CustomServletOutputStream extends ServletOutputStream
{
    private ServletOutputStream byteStream;
    
    public CustomServletOutputStream(ServletOutputStream byteStream)
    {
        this.byteStream = byteStream;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#isReady()
     */
    @Override
    public boolean isReady()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#setWriteListener(javax.servlet.WriteListener)
     */
    @Override
    public void setWriteListener(WriteListener paramWriteListener)
    {
        
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int date) throws IOException
    {
        byteStream.write(date);
    }

}
