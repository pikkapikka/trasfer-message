/**
 * 
 */
package com.softisland.message.business.message.service;

import com.softisland.common.utils.bean.SoftHttpResponse;
import com.softisland.message.business.message.bean.MessageInfo;

/**
 * ����ͬ����Ϣ
 * @author Administrator
 *
 */
public interface IMessageSyncSendService
{
    /**
     * ������Ϣ
     * @param message ��Ϣ
     * @param dstSite Ŀ��վ��
     * @throws Exception �쳣
     * @return response ��Ӧ��Ϣ
     */
    SoftHttpResponse sendToSite(MessageInfo message, String dstSite) throws Exception;
}
