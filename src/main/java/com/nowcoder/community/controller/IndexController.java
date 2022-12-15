package com.nowcoder.community.controller;

import com.nowcoder.community.aspect.ServiceLogAspect;
import com.nowcoder.community.controller.advice.ExceptionAdvice;
import com.nowcoder.community.entity.Favorite;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.Scenic;
import com.nowcoder.community.entity.ScenicImage;
import com.nowcoder.community.service.FavoriteService;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.PrivateKeyResolver;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

        return "/index";
    }
}
