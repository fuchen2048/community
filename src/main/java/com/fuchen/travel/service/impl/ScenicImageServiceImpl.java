package com.fuchen.travel.service.impl;

import com.fuchen.travel.entity.ScenicImage;
import com.fuchen.travel.mapper.ScenicImageMapper;
import com.fuchen.travel.service.ScenicImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/11/28
 * 景区图片-service层实现类
 */
@Service
public class ScenicImageServiceImpl implements ScenicImageService {

    @Autowired
    private ScenicImageMapper scenicImageMapper;
    @Override
    public List<ScenicImage> findScenicImageByScenicId(Integer scenicId) {
        return scenicImageMapper.selectScenicImageByScenicId(scenicId);
    }
}
