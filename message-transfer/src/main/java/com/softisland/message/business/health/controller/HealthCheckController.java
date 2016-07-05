/**
 * 
 */
package com.softisland.message.business.health.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 通过此URL可以检测到中间件是否启动
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/softisland/message/health")
public class HealthCheckController
{
    /**
     * 服务启动检测
     */
    @RequestMapping(value = "/check")
    public @ResponseBody String check()
    {
        return "OK";
    }
}
