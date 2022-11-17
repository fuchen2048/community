package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author 伏辰
 * @Date 2022/11/10
 * 景点列表
 */
@Controller
public class RouteListController {

    /**
     * 请求景点列表
     * @return 景点列表页面
     */
    @GetMapping("/site/route_list")
    public String routeList(){
        return "/site/route_list";
    }
}
