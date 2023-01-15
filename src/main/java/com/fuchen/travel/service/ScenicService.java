package com.fuchen.travel.service;

import com.fuchen.travel.entity.Scenic;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/11/27
 * 景区-业务层-接口
 */

public interface ScenicService {

    /**
     * 查找景区信息
     * @return
     */
    List<Scenic> findAllScenic(Integer offset, Integer limit);

    /**
     * 根据景点id查询景点信息
     * @param id 景点id
     * @return
     */
    Scenic findScenicById(Integer id);

    /**
     * 查询景点个数
     * @return
     */
    Integer findScenicCountAll();

    /**
     * 获取推荐的景点
     * @return
     */
    List<Scenic> getScenicRe();
}
