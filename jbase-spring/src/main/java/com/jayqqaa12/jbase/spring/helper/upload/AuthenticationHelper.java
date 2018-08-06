package com.jayqqaa12.jbase.spring.helper.upload;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20170112.ImageSyncScanRequest;
import com.aliyuncs.green.model.v20170112.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.mvc.RespCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by qmw on 2017/7/25.
 * 文本垃圾检测和文本关键词检测
 * 图片风险检测
 */
public class AuthenticationHelper {

    public static final String PORN = "porn"; // 色情
    public static final String TERRORISM = "terrorism"; //暴恐
    public static final String QRCODE = "qrcode"; // 二维码
    public static final String AD = "ad"; //图片广告
    public static final String OCR = "ocr"; //文字识别


    public static final String SUGGESTION_PASS = "pass";//内容正常
    public static final String SUGGESTION_REVIEW = "review";//需要人工审核
    public static final String SUGGESTION_BLOCK = "block";//违规，可以直接删除或者做限制处理


    private final Logger logger = LoggerFactory.getLogger(AuthenticationHelper.class);
    private IClientProfile profile = null;
    private IAcsClient client = null;

    @Value("${config.aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${config.aliyun.accessKeySecret}")
    private String accessKeySecret;


    @Value("${config.aliyun.context.endpointRegion:cn-shanghai}")
    private String endpointRegion;

    @Value("${config.aliyun.context.endpointHost}")
    private String endpointHost;

    @PostConstruct
    public void init() {
        try {
            //设置accessKeyId、accessKeySecret
            profile = DefaultProfile.getProfile(endpointRegion,
                    accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint(endpointRegion, "cn-shanghai", "Green", endpointHost);
            client = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            logger.error("启动内容监测：出错e={}", e);
        }
    }

    /**
     * 图片检测
     *
     * @param imgUrl 图片地址
     *               图片类型
     *               normal	正常图片，无色情
     *               sexy	性感图片
     *               vulgar	低俗图片
     *               porn	色情图片
     */
    public Map<String, String> imageScanRequest(String imgUrl, String... checkType) {
        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        imageSyncScanRequest.setContentType(FormatType.JSON);
        imageSyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        imageSyncScanRequest.setEncoding("utf-8");
        imageSyncScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task = new LinkedHashMap<String, Object>();
        task.put("dataId", UUID.randomUUID().toString());
        task.put("url", imgUrl);
        task.put("time", new Date());
        tasks.add(task);
        JSONObject data = new JSONObject();

        data.put("scenes", Arrays.asList(checkType));
        data.put("tasks", tasks);
        try {
            imageSyncScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        } catch (UnsupportedEncodingException e) {
            logger.error("图片内容检测设置请求头内容：出错e={}", e);
        }
        /**
         * 设置超时时间
         */
        imageSyncScanRequest.setConnectTimeout(3000);
        imageSyncScanRequest.setReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(imageSyncScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
                logger.info("图片响应参数={}", JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            for (int i = 0; i < sceneResults.size(); i++) {
                                String scene = ((JSONObject) sceneResults.get(i)).getString("scene");
                                String suggestion = ((JSONObject) sceneResults.get(i)).getString("suggestion");
                                String label = ((JSONObject) sceneResults.get(i)).getString("label");
                                logger.info("图片内容检测场景={},图片分类={},图片建议={}", scene, label, suggestion);
                                //根据scene和suggetion做相关的处理
                                if (sceneResults.size() > 1) {
                                    if (i == sceneResults.size() - 1) {
                                        return dealScanContent(scene, label, suggestion);
                                    }
                                    if (suggestion.equals("pass")) continue;
                                    return dealScanContent(scene, label, suggestion);
                                }
                                return dealScanContent(scene, label, suggestion);
                            }
                        } else {
                            logger.info("task process fail:{}", ((JSONObject) taskResult).getInteger("code"));
                        }
                    }
                } else {
                    logger.info("detect not success. code:{}", scrResponse.getInteger("code"));
                }
            } else {
                logger.info("response not success. status::{}", httpResponse.getStatus());
            }
        } catch (ServerException e) {

            logger.error("图片内容检测服务:出错e={}", e);
        } catch (ClientException e) {
            logger.error("图片内容检测客户端:出错e={}", e);
        } catch (Exception e) {
            logger.error("图片内容检测:出错e={}", e);
        }
        return null;
    }


    /**
     * 文本内容检测
     *
     * @param content 用户输入的内容
     *                文本检测  antispam
     *                normal	正常文本
     *                spam	含垃圾信息
     *                ad	广告
     *                politics	渉政
     *                terrorism	暴恐
     *                abuse	辱骂
     *                porn	色情
     *                flood	灌水
     *                contraband	违禁
     */
    public Map<String, String> textScan(String content) {
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        task1.put("content", content);
        tasks.add(task1);
        JSONObject data = new JSONObject();
        /**
         * 文本垃圾检测： antispam
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        try {
            textScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        } catch (UnsupportedEncodingException e) {
            logger.error("文本内容检测设置请求头内容：出错e={}", e);
        }
        /**
         * 设置超时时间
         */
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
                logger.info("文本响应参数={}", JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            for (int i = 0; i < sceneResults.size(); i++) {
                                String scene = ((JSONObject) sceneResults.get(i)).getString("scene");
                                String suggestion = ((JSONObject) sceneResults.get(i)).getString("suggestion");
                                String label = ((JSONObject) sceneResults.get(i)).getString("label");
                                logger.info("文本内容检测场景={},文本命中垃圾分类={},文本建议={}", scene, label, suggestion);
                                //根据scene和suggetion做相关的处理
                                if (sceneResults.size() > 1) {
                                    if (i == sceneResults.size() - 1) {
                                        return dealScanContent(scene, label, suggestion);
                                    }
                                    if (suggestion.equals("pass")) continue;
                                    return dealScanContent(scene, label, suggestion);
                                }
                                return dealScanContent(scene, label, suggestion);
                            }
                        } else {
                            logger.info("task process fail:{}", ((JSONObject) taskResult).getInteger("code"));
                        }
                    }
                } else {
                    logger.info("detect not success. code:{}", scrResponse.getInteger("code"));
                }
            } else {
                logger.info("response not success. status::{}", httpResponse.getStatus());
            }
        } catch (ServerException e) {
            logger.error("文本内容检测服务:出错e={}", e);
        } catch (ClientException e) {
            logger.error("文本内容客户端:出错e={}", e);
        } catch (Exception e) {
            logger.error("文本内容检测:出错e={}", e);
        }
        return null;
    }

    /**
     * 处理检测的内容
     * 如果处理的是图片,正常图片和性感图片都通过
     *
     * @param scene      场景
     * @param label      类型
     * @param suggestion 建议
     */
    private Map<String, String> dealScanContent(String scene, String label, String suggestion) {
        Map<String, String> result = new HashMap<>();
        if (suggestion.equals(SUGGESTION_PASS) || suggestion.equals(SUGGESTION_REVIEW)) {//通过
            result.put("success", "success");
            result.put("label", label);
            return result;
        } else if (suggestion.equals(SUGGESTION_BLOCK)) {
            result.put("success", "fail");
            result.put("label", label);
            result.put("scene", scene);
            return result;
        }
        throw new BusinessException(RespCode.INNER__CONTENT_SCAN_ERROR, "找不到内容监测返回类型");
    }
}
