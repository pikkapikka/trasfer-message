/**
 * 
 */
package com.softisland.message.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.lang3.RandomStringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Administrator
 *
 */
public class TestMain
{

    /**
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args)
    {
        Set<String> sites = new ConcurrentSkipListSet<String>();
        sites.add("aa");
        sites.add("aa");
        sites.add("aa");
        System.out.println(sites.size());
        
        Map<String, String> aaa = new HashMap<String, String>();
        aaa.put("dstSite", "aa");
        aaa.put("content", "<this is a test>");
        
        String mapStr = JSONObject.toJSONString(aaa);
        System.out.println(mapStr);
        
        Map<String, String> temp = JSONObject.parseObject(mapStr, Map.class);
        System.out.println(temp);
        
        System.out.println(RandomStringUtils.randomAscii(30));
        System.out.println(RandomStringUtils.randomAlphabetic(30));
        System.out.println(RandomStringUtils.randomAlphanumeric(30));

    }

}
