package com.fuchen.travel.controller;

import com.fuchen.travel.entity.Page;
import com.fuchen.travel.entity.Scenic;
import com.fuchen.travel.entity.User;
import com.fuchen.travel.service.FavoriteService;
import com.fuchen.travel.service.ScenicService;
import com.fuchen.travel.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 伏辰
 * @Date 2022/11/12
 */
@Slf4j
@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FavoriteService favoriteService;


    /**
     * 我的收藏
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/collection")
    public String getMyCollection(Model model, Page page){

        //获取并判断用户是否登录
        User user = hostHolder.getUser();
        if (user == null) {
            throw new IllegalArgumentException("user没有登录");
        }

        //根据用户id获取用户的收藏景点个数
        Integer myFavoriteCount = favoriteService.findCollectionCountByUserId(user.getId());
        //设置分页信息
        page.setPath("/favorite/collection");
        page.setLimit(8);
        page.setRows(myFavoriteCount);

        //获取用户收藏景点的信息集合
        List<Scenic> allScenic = favoriteService.findCollectionAllByUserId(user.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> scenicList = new ArrayList<>();
        if (allScenic != null && allScenic.size() > 0) {
            for (Scenic scenic : allScenic) {
                Map<String, Object> map = new HashMap<>();
                map.put("scenic", scenic);
                scenicList.add(map);
            }
        }

        model.addAttribute("myFavorite", scenicList);
        model.addAttribute("myFavoriteCount", myFavoriteCount);

        return "/site/my-favorite";
    }

    /**
     * 收藏排行榜
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/ranking")
    public String getCollectionRankingList(Model model, Page page){

        Integer collectionCountAll = favoriteService.findtCollectionCountAll();

        page.setLimit(8);
        page.setPath("/favorite/ranking");
        page.setRows(collectionCountAll);

        //根据景点收藏次数获取景点集合
        List<Scenic> scenicConllectionList = favoriteService.findCollectionByCount(page.getOffset(), page.getLimit());

        List<Map<String, Object>> scenicListByCount = new ArrayList<>();
        //遍历整个集合，将集合中景点放入map中
        if (scenicConllectionList != null && scenicConllectionList.size() > 0) {
            for (Scenic scenic : scenicConllectionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("scenic", scenic);
                scenicListByCount.add(map);
            }
        }

        model.addAttribute("favoriteRanking", scenicListByCount);
        model.addAttribute("favoriteCountAll", collectionCountAll);

        return "/site/favorite-rank";
    }
}
