package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author 伏辰
 * @Date 2022/11/12
 */
@Controller
public class FavoriteController {

    @GetMapping("/favorite")
    public String getFavorite(){
        return "/site/myfavorite";
    }
}
