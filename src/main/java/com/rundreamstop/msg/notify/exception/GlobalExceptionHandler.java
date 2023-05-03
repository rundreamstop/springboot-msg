package com.rundreamstop.msg.notify.exception;

import com.alibaba.fastjson.JSONObject;
import com.rundreamstop.msg.notify.domain.R;
import com.rundreamstop.msg.notify.service.DingMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author rundreams.blog.csdn.net
 * @email zhangzihaopk@gmail.com
 * @date 2023/5/3 21:45
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    DingMsgService dingMsgService;

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        try {
            // 后续数据可入库
            dingMsgService.sendDingTalkMsg(JSONObject.toJSONString(e));
        } catch (Exception ee) {
            log.error("发送异常信息接口异常", ee);
        }
        return R.fail(e.getMessage());
    }
}
