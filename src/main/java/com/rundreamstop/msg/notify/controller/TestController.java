package com.rundreamstop.msg.notify.controller;

import com.rundreamstop.msg.notify.domain.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rundreams.blog.csdn.net
 * @email zhangzihaopk@gmail.com
 * @date 2023/5/3 21:47
 */
@RestController
public class TestController {

    @GetMapping(value = "test")
    public R test() {
        // 异常代码
        System.out.print(1 / 0);
        return R.ok();
    }
}
