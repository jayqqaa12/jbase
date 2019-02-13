package com.jayqqaa12.jbase.util.sdk;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 12
 * @create: 2018-09-30 09:46
 **/

public class PushUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PushUtil.class);


    /**
     * 默认发送所有平台的
     *
     * @param masterSecret
     * @param appKey
     * @param push
     */
    public static void push(String masterSecret, String appKey, Push push) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        final JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        final PushPayload payload = buildAlert(push);
        try {
            PushResult result = jpushClient.sendPush(payload);

            LOG.info("push {} finish get push result {} " ,push,result);

        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            LOG.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.error("Sendno: " + payload.getSendno());
        }
    }


    private static PushPayload buildAlert(Push push) {

        JsonObject jsonObject = new JsonObject();
        push.getData().forEach((k, v) -> {
            if (v instanceof Boolean)
                jsonObject.addProperty(k, (Boolean) v);
            else if (v instanceof String)
                jsonObject.addProperty(k, (String) v);
            else if (v instanceof Number)
                jsonObject.addProperty(k, (Number) v);
            else if (v instanceof Character)
                jsonObject.addProperty(k, (Character) v);
            else
                throw new IllegalArgumentException("推送设置错误-》不支持的数据类型，需要符合json格式标准");
        });

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(push.isSendAll() ? Audience.all() : Audience.registrationId(push.getDevices()))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(push.isProd())
                        .setSendno(ServiceHelper.generateSendno())
                        .build())
                .setNotification(Notification.newBuilder()
                        .setAlert(push.getContent())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(push.getTitle())
                                .addExtra("data",jsonObject)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)  //ios 角标的数字标示消息数量
                                .addExtra("data",jsonObject)
                                .build())
                        .build())

                .build();
    }

    private static PushPayload buildMessage(Push push) {

        Map map = new HashMap();
        map.put("data", push.getData());
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(push.isSendAll() ? Audience.all() : Audience.registrationId(push.getDevices()))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(push.isProd())
                        .setSendno(ServiceHelper.generateSendno())
                        .build())
                .setMessage(Message.newBuilder().setTitle(push.getTitle())
                        .setMsgContent(push.getContent())
                        .addExtras(map)
                        .build()) //透传消息 apns不支持
                .build();

    }

}

