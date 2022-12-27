package com.fuchen.travel.service;

import com.fuchen.travel.entity.ScenicImage;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/11/28
 * 景区图片-service层
 */
public interface ScenicImageService {

    /**
     * 根据景区id获取景区图片
     * @param scenicId 景区id
     * @return
     */
    List<ScenicImage> findScenicImageByScenicId(Integer scenicId);
}
