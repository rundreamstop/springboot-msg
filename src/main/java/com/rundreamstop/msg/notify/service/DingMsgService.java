package com.rundreamstop.msg.notify.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.rundreamstop.msg.notify.domain.DingTalkSendMsgRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rundreams.blog.csdn.net
 * @email zhangzihaopk@gmail.com
 * @date 2023/5/3 21:37
 */
@Slf4j
@Service
public class DingMsgService {

    @Value("${config.dingtalk.secret}")
    private String dingtalkSecret;

    @Value("${config.dingtalk.webhook}")
    private String dingtalkWebhook;


    public void sendDingTalkMsg(String content) {

        // 钉钉最大只能发送2000个字符
        if (content.length() >= 1990) {
            content = content.substring(0, 1990);
        }

        DingTalkSendMsgRequestDTO requestDTO = new DingTalkSendMsgRequestDTO();
        requestDTO.setSecret(dingtalkSecret);
        requestDTO.setWebhook(dingtalkWebhook);
        requestDTO.setContent(content);

        //消息内容
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("content", requestDTO.getContent());
        //通知人
        Map<String, Object> atMap = new HashMap<>();
        //1.是否通知所有人
        atMap.put("isAtAll", requestDTO.getIsAtAll());
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", requestDTO.getMobileList());
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);
        requestDTO.setContent(JSON.toJSONString(reqMap));

        try {
            String secret = requestDTO.getSecret();
            //获取系统时间戳
            long timestamp = Instant.now().toEpochMilli();
            //拼接
            String stringToSign = timestamp + "\n" + secret;
            //使用HmacSHA256算法计算签名
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            //进行Base64 encode 得到最后的sign，可以拼接进url里
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            //钉钉机器人地址（配置机器人的webhook）
            String dingUrl = requestDTO.getWebhook() + "&timestamp=" + timestamp + "&sign=" + sign;

            String result = HttpUtil.post(dingUrl, requestDTO.getContent());
            log.info("re={}", result);
        } catch (Exception e) {
            log.error("钉钉推送消息出现异常", e);
        }
    }
}
