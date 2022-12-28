package com.fuchen.travel.service;

import com.fuchen.travel.entity.Scenic;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/12/28
 * 搜索业务层
 */
public interface SearchService {

    /**
     * 通过景区名称查找景区
     * @param scenicName 景区名称
     * @return 景区信息集合
     */
    List<Scenic> findToScenic (String scenicName, Integer offset, Integer limit);

    /**
     * 根据关键字查询景区数量
     * @param keyword
     * @return
     */
    Integer findToScenicCount(String keyword);
}
