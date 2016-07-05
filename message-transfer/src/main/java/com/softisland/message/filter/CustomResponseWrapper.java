/**
 * 
 */
package com.softisland.message.filter;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Administrator
 *
 */
public class CustomResponseWrapper extends HttpServletResponseWrapper
{
    private CustomPrintWriter myWriter;  
    private CustomServletOutputStream myOutputStream;

    public CustomResponseWrapper(HttpServletResponse response)
    {
        super(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override  
    public CustomPrintWriter getWriter() throws IOException {  
        myWriter = new CustomPrintWriter(super.getWriter());  
        return myWriter;  
    }  
    
    /**
     * {@inheritDoc}
     */
    @Override  
    public ServletOutputStream getOutputStream() throws IOException {  
        myOutputStream = new CustomServletOutputStream(super.getOutputStream());  
        return myOutputStream;  
    }  
  
    /**
     * 获取writer
     * @return writer
     */
    public CustomPrintWriter getMyWriter() {  
        return myWriter;  
    }  
  
    /**
     * 获取输出流
     * @return 输出流
     */
    public CustomServletOutputStream getMyOutputStream() {  
        return myOutputStream;  
    }  

}
