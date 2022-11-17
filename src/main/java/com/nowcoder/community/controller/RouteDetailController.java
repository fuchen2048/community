package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author 伏辰
 * @Date 2022/11/12
 */
@Controller
public class RouteDetailController {

    @GetMapping("/site/route_detail")
    public String getRouteDetail (){
        return "/site/route_detail";
    }
}
