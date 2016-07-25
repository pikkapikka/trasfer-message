/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.message.business.message.bean.MessageInfo;

/**
 * 消息响应结果通知服务
 * @author qxf
 *
 */
public interface IMessageNotifyService
{
    /**
     * 添加响应消息到内存中
     * @param dstSite 目标站点
     * @param result 响应结果
     * @param message 消息内容
     * @param isSuc 是否成功
     */
    void addMessageResult(String dstSite, String result, MessageInfo message, boolean isSuc);
    
    /**
     * 设置消息响应的URL和地址
     * @param fromSite 来源站点
     * @param messageUuid 消息UUID
     * @param dstNum 目标站点数量
     */
    void setNotifyUrl(String fromSite, String messageUuid, int dstNum);
    
    /**
     * 获取目标站点数量
     * @param messageUuid 消息唯一标示符
     * @return 目标站点数量
     */
    Integer getDstSiteNumber(String messageUuid);
    
    /**
     * 获取消息通知url
     * @param messageUuid 消息唯一标识符
     * @return URL
     */
    String getNotifyUrl(String messageUuid);
    
    /**
     * 获取响应结果数量
     * @param messageUuid 消息唯一标识符
     * @return 消息响应结果
     */
    int getMessageResponseSize(String messageUuid);
    
    /**
     * 移除消息的结果
     * @param fromSite 消息来源站点
     * @param messageUuid 消息唯一标识符
     */
    void removeMessageResponse(String fromSite, String messageUuid);
    
    /**
     * 获取欲通知的消息UUID。 如果没有，则此接口阻塞
     * @param fromSite 消息发送站点
     * @return 消息UUID
     */
    String takeMessageUuid(String fromSite);
    
    /**
     * 发送消息
     * @param notifyUrl
     * @param messageUuid
     */
    void sendNotifyMessage(String notifyUrl, String messageUuid) throws Exception;
}
