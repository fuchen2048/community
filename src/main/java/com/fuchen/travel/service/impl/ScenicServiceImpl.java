package com.fuchen.travel.service.impl;

import com.fuchen.travel.entity.Scenic;
import com.fuchen.travel.mapper.ScenicMapper;
import com.fuchen.travel.service.ScenicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/11/27
 * 景区-业务层-实现类
 */
@Service
public class ScenicServiceImpl implements ScenicService {

    @Autowired
    private ScenicMapper scenicMapper;

    @Override
    public List<Scenic> findAllScenic(Integer offset, Integer limit) {
        return scenicMapper.selectAllScenic(offset, limit);
    }

    @Override
    public Scenic findScenicById(Integer id) {
        return scenicMapper.selectScenicById(id);
    }

    @Override
    public Integer findScenicCountAll() {
        return scenicMapper.selectScenicCountAll();
    }
}
