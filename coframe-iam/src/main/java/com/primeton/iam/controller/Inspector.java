package com.primeton.iam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author huzd@primeton.com
 */
@Controller
public class Inspector {

    @RequestMapping("/iam/inspector")
    public Object inspector(@RequestParam(name = "display", defaultValue = "none") String display) {

        /*
         * 1. 检查登录状态
         * 2. 如果已经登录，则可以通过定时轮询调用此接口，
         *    来保持 token 持续有效(弊端，需要手动退出才可以进行退出，
         *    否则一直会处于在线状态，知道管理浏览器之后等待 session 失效，才会真正退出)
         */
        if (display == null || "none".equals(display) || "".equals(display)) {
            return ResponseEntity.noContent()
                    .build();
        }

        if ("iframe".equals(display)) {
            return "redirect:/iam/inspector_page.html";
        }

        return "";
    }
}
