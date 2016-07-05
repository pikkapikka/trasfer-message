/**
 * 
 */
package com.softisland.message.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softisland.message.exception.IslandUncheckedException;

/**
 * @author Administrator
 *
 */
public class CustomErrorFilter implements Filter
{
    private static final Logger LOG = LoggerFactory.getLogger(CustomErrorFilter.class);

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        // TODO Auto-generated method stub
        
        CustomResponseWrapper responseWrapper = new CustomResponseWrapper((HttpServletResponse) response); 
        
        try
        {
            chain.doFilter(request, responseWrapper);
        }
        catch (ServletException e)
        {
            if (e.getCause() instanceof IslandUncheckedException)
            {
                LOG.error("filter catch exception:", e);
                responseWrapper.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"errorMsg\":\"" + e.getCause().getMessage() + "\"}");
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig arg0) throws ServletException
    {
        // TODO Auto-generated method stub

    }

}
