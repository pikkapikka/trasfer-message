/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.common.utils.bean.SoftHttpResponse;
import com.softisland.message.business.message.bean.MessageInfo;

/**
 * 发送同步消息
 * @author Administrator
 *
 */
public interface IMessageSyncSendService
{
    /**
     * 发送消息
     * @param message 消息
     * @param dstSite 目标站点
     * @throws Exception 异常
     * @return response 响应消息
     */
    SoftHttpResponse sendToSite(MessageInfo message, String dstSite) throws Exception;
}
