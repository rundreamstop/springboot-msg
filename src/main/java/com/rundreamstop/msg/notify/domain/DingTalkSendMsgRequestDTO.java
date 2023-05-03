package com.rundreamstop.msg.notify.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author rundreams.blog.csdn.net
 * @email zhangzihaopk@gmail.com
 * @date 2023/5/3 21:37
 */
@Data
public class DingTalkSendMsgRequestDTO {

    /**
     * 加签密钥
     */
    @NotBlank
    private String secret;

    /**
     * Webhook 地址
     */
    @NotBlank
    private String webhook;

    /**
     * 发送内容
     */
    @NotBlank
    private String content;

    /**
     * 是否通知所有人
     */
    private Boolean isAtAll;

    /**
     * 通知具体人的手机号码列表
     */
    private List<String> mobileList;
}
