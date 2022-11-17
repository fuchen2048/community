package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页
 * @Author 伏辰
 * @Date 2022/11/10
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index (){
        return "/index";
    }


}
