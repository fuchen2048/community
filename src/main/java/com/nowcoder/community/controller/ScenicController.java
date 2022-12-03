package com.nowcoder.community.controller;

import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.ScenicImageService;
import com.nowcoder.community.service.ScenicService;
import com.nowcoder.community.util.HostHolder;
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

    @Value("${scenic.path.image}")
    private String scenicImage;

    /**
     * 获取景区详情信息
     * @param scenicId 景区id
     * @param model
     * @return
     */
    @GetMapping("/scenic_detail/{scenicId}")
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

        return "/site/scenic_detail";
    }

    /**
     * 请求景点列表
     * @return 景点列表页面
     */
    @GetMapping("/scenic_list")
    public String scenicList(Model model, Page page){

        //查询景区
        List<Scenic> allScenic = scenicService.findAllScenic();

        //分页信息
        page.setLimit(10);
        page.setPath("/scenic_list");
        page.setRows(allScenic.size());

        List<Map<String, Object>> scenicList = new ArrayList<>();
        if (allScenic != null && allScenic.size()>0) {
            for (Scenic scenic : allScenic) {
                Map<String, Object> map = new HashMap<>();
                map.put("scenic", scenic);
                scenicList.add(map);
            }
        }

        model.addAttribute("scenicList", scenicList);
        model.addAttribute("scenicCount", scenicList.size());
        return "/site/scenic_list";
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