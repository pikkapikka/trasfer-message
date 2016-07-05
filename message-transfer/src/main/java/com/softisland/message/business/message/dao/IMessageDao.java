/**
 * 
 */
package com.softisland.message.business.message.dao;

import java.util.List;

import com.softisland.message.business.message.bean.MessageInfo;

/**
 * 数据存操作接口
 * @author Administrator
 *
 */
public interface IMessageDao
{
    /**
     * 保存消息到数据库
     * @param info 消息体
     */
    void saveMessage(MessageInfo info);
    
    /**
     * 保存没有发送的临时消息（这里是指没有发送到消息队列的数据）
     * @param info 消息
     * @param dstSite 目标站点
     */
    void saveUnSendTempMessage(MessageInfo info, String dstSite);
    
    /**
     * 从临时表中获取指定数量的未发送的消息
     * @param dstSite 目标站点
     * @param start 起始数量
     * @param num 条数
     * @return 返回结果
     */
    List<MessageInfo> fetchMessageFromTempDB(String dstSite, int start, int num);
    
    /**
     * 删除指定的临时消息
     * @param dstSite 目标站点
     * @param lastReceiveTime 消息的最后一个接收时间
     */
    void deleteTempMessage(String dstSite, String lastReceiveTime);
    
    /**
     * 统计没有发送的消息有多少
     * @param dstSite 目标站点
     */
    int countTempMessage(String dstSite);
    
    /**
     * 保存消息的通知结果
     * @param messageUuid 消息标识
     * @param fromSite 站点ID
     * @param siteUrl 站点URL
     * @param isSuc 是否成功
     */
    void saveNotifyResult(String messageUuid, String fromSite, String siteUrl, boolean isSuc);
    
    /**
     * 保存消息的保存
     * @param messageUuid 消息标示
     * @param dstSite 目标站点ID
     * @param siteUrl 目标站点URL
     * @param isSuc 是否成功
     */
    void saveTransferResult(String messageUuid, String dstSite, String siteUrl, boolean isSuc);
}
