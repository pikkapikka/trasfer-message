/**
 * 
 */
package com.softisland.message.filter;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * 自定义输出
 * @author Administrator
 *
 */
public class CustomPrintWriter extends PrintWriter
{
    public CustomPrintWriter(Writer outputStream)
    {
        super(outputStream);
    }
}
