package com.fuchen.travel.controller;

import com.fuchen.travel.entity.Scenic;
import com.fuchen.travel.service.DiscussPostService;
import com.fuchen.travel.service.FavoriteService;
import com.fuchen.travel.service.ScenicService;
import com.fuchen.travel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

/**
 * 首页
 * @Author 伏辰
 * @Date 2022/11/10
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ScenicService scenicService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/index")
    public String index (Model model){

        //根据景点收藏次数获取景点集合，只取前四个景点信息
        List<Scenic> scenicConllectionList = favoriteService.findCollectionByCount(0, 4);

        List<Map<String, Object>> scenicListByCount = new ArrayList<>();
        //遍历整个集合，将集合中景点放入map中
        if (scenicConllectionList != null && scenicConllectionList.size() > 0) {
            for (Scenic scenic : scenicConllectionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("scenic", scenic);
                scenicListByCount.add(map);
            }
        }
        model.addAttribute("scenicConllectionList", scenicListByCount);

        //根据景点收藏次数获取景点集合，只取前四个景点信息
        List<Scenic> scenicRe = scenicService.getScenicRe();
        List<Map<String, Object>> scenicListByRe = new ArrayList<>();
        //遍历整个集合，将集合中景点放入map中
        if (scenicRe != null && scenicRe.size() > 0) {
            for (Scenic scenic : scenicRe) {
                Map<String, Object> map = new HashMap<>();
                map.put("scenic", scenic);
                scenicListByRe.add(map);
            }
        }
        model.addAttribute("scenicListByRe", scenicListByRe);
        return "/index";
    }
}
