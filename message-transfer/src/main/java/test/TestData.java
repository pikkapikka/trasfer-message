/**
 * 
 */
package test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.softisland.message.business.message.bean.MessageInfo;
import com.softisland.message.util.MD5Util;

/**
 * @author Administrator
 *
 */
public class TestData
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        
        MD5Util.setMd5Key("uMy4XPWdmqODSuuadhBwmn49qj8VVP");
        System.out.println(MD5Util.sign("testMsgaaxxxxxxxxxx"));
        
        System.out.println(MD5Util.sign("site3"));
        System.out.println(MD5Util.sign("site2"));
        System.out.println(MD5Util.sign("site1"));
        
        MessageInfo info = new MessageInfo();
        info.setDelay("aa");
        info.setFromSite("bb");
        info.setMessage("xasxasxa");
        info.setMessageId("cc");
        info.setMessageType("33");
        info.setNeedNotify(true);
        
        List<String> sitess = new ArrayList<String>();
        sitess.add("xx1");
        sitess.add("xx2");
        
        info.setReceiveSites(sitess);
        info.setSalt("xx");
        info.setTimestamp("dsds");
        info.setUuid("rrr");
        
        System.out.println(info.toString());
        System.out.println(JSONObject.parseObject(JSONObject.toJSONString(info), MessageInfo.class).toString());;

    }

}
