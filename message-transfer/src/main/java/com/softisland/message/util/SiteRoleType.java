/**
 * 
 */
package com.softisland.message.util;

/**
 * վ������
 * @author Administrator
 *
 */
public enum SiteRoleType
{
    // վ��ֻ��Ϊ��Ϣ������
    SENDER("SENDER"),
    
    // ��Ϣֻ��Ϊ��Ϣ������
    RECEIVER("RECEIVER"),
    
    // ͬʱ��Ϊ�����ߺͽ�����
    ALL("ALL");
    
    private String roleName;
    
    private SiteRoleType(String roleName)
    {
        this.roleName = roleName;
    }
    
    /**
     * ��ȡ��Ϣվ���ɫ����
     * @return
     */
    public String getName()
    {
        return roleName;
    }

}
