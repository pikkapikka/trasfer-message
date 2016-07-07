/**
 * 
 */
package com.softisland.message.util;

/**
 * 站点类型
 * @author Administrator
 *
 */
public enum SiteRoleType
{
    // 站点只作为消息发送者
    SENDER("SENDER"),
    
    // 消息只作为消息接收者
    RECEIVER("RECEIVER"),
    
    // 同时作为发送者和接收者
    ALL("ALL");
    
    private String roleName;
    
    private SiteRoleType(String roleName)
    {
        this.roleName = roleName;
    }
    
    /**
     * 获取消息站点角色类型
     * @return
     */
    public String getName()
    {
        return roleName;
    }

}
