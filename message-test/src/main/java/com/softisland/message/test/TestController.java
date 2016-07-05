/**
 * 
 */
package com.softisland.message.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/softisland/message/test")
public class TestController
{
    @RequestMapping(value = "/ping")
    public @ResponseBody String ping()
    {
        return "OK";
    }
    
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public @ResponseBody String notifyTest(HttpServletRequest request, HttpServletResponse response)
    {
        //throw new IslandUncheckedException("TEST");
        return "OK";
    }
    
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public @ResponseBody String receiveTest(HttpServletRequest request, HttpServletResponse response)
    {
        //throw new IslandUncheckedException("TEST");
        return "OK";
    }
    
    @RequestMapping(value = "/unsend", method = RequestMethod.GET)
    public @ResponseBody String unsendTest(HttpServletRequest request, HttpServletResponse response)
    {
        //throw new IslandUncheckedException("TEST");
        List<Map<String, Object>> rests = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 2; i++)
        {
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put("messageId", "id" + i);
            ret.put("fromSite", "site1");
            ret.put("needNotify", true);
            ret.put("messageType", "msgType_xxx");
            ret.put("message", "xxxxxxxxxx");
            ret.put("timestamp", "vvvvvvvvvv");
            ret.put("salt", "a1e418ec008b4f30321a749c2d085751");
            rests.add(ret);
        }
        
        
        return JSONObject.toJSONString(rests);
    }
}
