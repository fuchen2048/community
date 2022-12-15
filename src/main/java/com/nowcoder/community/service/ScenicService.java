package com.nowcoder.community.service;

import com.nowcoder.community.entity.Scenic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/11/27
 * 景区-业务层-接口
 */
@Service
public interface ScenicService {

    /**
     * 查找全部景区信息
     * @return
     */
    List<Scenic> findAllScenic();

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
}
