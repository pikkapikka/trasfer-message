/**
 * 
 */
package com.softisland.message.business.health.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ͨ����URL���Լ�⵽�м���Ƿ�����
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/softisland/message/health")
public class HealthCheckController
{
    /**
     * �����������
     */
    @RequestMapping(value = "/check")
    public @ResponseBody String check()
    {
        return "OK";
    }
}
