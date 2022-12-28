package com.fuchen.travel.service.impl;

import com.fuchen.travel.entity.Scenic;
import com.fuchen.travel.mapper.ScenicMapper;
import com.fuchen.travel.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/12/28
 * 搜索业务层-实现类
 */
@Service
public class SearchServiceImpl implements SearchService {

    /**
     * 注入景区持久层对象
     */
    @Autowired
    private ScenicMapper scenicMapper;

    @Override
    public List<Scenic> findToScenic(String scenicName, Integer offset, Integer limit) {
        //调用持久层根据名称查询景区信息的集合
        return scenicMapper.selectScenicByScenicName(scenicName, offset, limit);
    }

    @Override
    public Integer findToScenicCount(String keyword) {
        return scenicMapper.selectToScenicCount(keyword);
    }
}
