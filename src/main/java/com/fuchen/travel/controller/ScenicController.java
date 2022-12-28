package com.fuchen.travel.controller;

import com.fuchen.travel.entity.*;
import com.fuchen.travel.service.FavoriteService;
import com.fuchen.travel.service.ScenicImageService;
import com.fuchen.travel.service.ScenicService;
import com.fuchen.travel.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 伏辰
 * @Date 2022/11/10
 * 景点列表
 */
@Slf4j
@Controller
public class ScenicController {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private ScenicService scenicService;

    @Autowired
    private ScenicImageService scenicImageService;

    @Autowired
    private FavoriteService favoriteService;

    @Value("${scenic.path.image}")
    private String scenicImage;

    /**
     * 获取景区详情信息
     * @param scenicId 景区id
     * @param model
     * @return
     */
    @GetMapping("/scenic-detail/{scenicId}")
    public String getScenicDetail (@PathVariable("scenicId") Integer scenicId, Model model){

        //查询景区详情
        Scenic scenic = scenicService.findScenicById(scenicId);

        //查询景区图片
        List<ScenicImage> images = scenicImageService.findScenicImageByScenicId(scenicId);

        List<Map<String, Object>> imageList = new ArrayList<>();
        if (images != null && images.size() > 0) {
            for (ScenicImage image : images) {
                Map<String, Object> map = new HashMap<>();
                map.put("image", image);
                imageList.add(map);
            }
        }

        model.addAttribute("scenicImg", imageList);
        model.addAttribute("scenic", scenic);

        User user = hostHolder.getUser();

        if (user != null) {
            //查询用户收藏信息
            Favorite collection = favoriteService.findByUserIdAndScenicId(user.getId(), scenic.getId());
            model.addAttribute("collection", collection);

            //查询景点收藏次数
            Integer collectionCount = favoriteService.getCollectionCount(scenicId);
            model.addAttribute("collectionCount", collectionCount);
            return "/site/scenic-detail";
        } else {

            model.addAttribute("collectionCount",  favoriteService.getCollectionCount(scenicId));
            return "/site/scenic-detail";
        }
    }

    /**
     * 请求景点列表
     * @return 景点列表页面
     */
    @GetMapping("/scenic-list")
    public String scenicList(Model model, Page page){
        Integer scenicCountAll = scenicService.findScenicCountAll();
        //分页信息
        page.setLimit(5);
        page.setPath("/scenic_list");
        page.setRows(scenicCountAll);

        //查询景区
        List<Scenic> allScenic = scenicService.findAllScenic(page.getOffset(),page.getLimit());

        List<Map<String, Object>> scenicList = new ArrayList<>();
        if (allScenic != null && allScenic.size()>0) {
            for (Scenic scenic : allScenic) {
                Map<String, Object> map = new HashMap<>();
                map.put("scenic", scenic);
                scenicList.add(map);
            }
        }

        model.addAttribute("scenicList", scenicList);
        model.addAttribute("scenicCount", scenicCountAll);


        List<Scenic> scenicConllectionList = favoriteService.findCollectionByCount(0, 4);
        List<Map<String, Object>> scenicListByCount = new ArrayList<>();

        if (scenicConllectionList != null && scenicConllectionList.size() > 0) {
            for (Scenic scenic : scenicConllectionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("scenic", scenic);
                scenicListByCount.add(map);
            }
        }
        model.addAttribute("scenicsHot", scenicListByCount);

        return "/site/scenic-list";
    }

    /**
     * 获取景区图片
     * @param imageName 景区图片的名称
     * @param response 响应体
     */
    @GetMapping("/scenicImg/{imageName}")
    public void getScenicImage(@PathVariable("imageName") String imageName, HttpServletResponse response) {
        //服务器存放路径
        imageName = scenicImage + "/" + imageName;
        //文件后缀
        String suffix = imageName.substring(imageName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);

        try (FileInputStream inputStream = new FileInputStream(imageName);) {

            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            log.error("读写图像失败！" + e.getMessage());
        }
    }
}
