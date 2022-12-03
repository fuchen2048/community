package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.ScenicImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/11/28
 * 景区图片-mapper层
 */
@Mapper
public interface ScenicImageMapper {

    /**
     * 根据景区id查询景区图片
     * @param scenicId 景区id
     * @return
     */
    List<ScenicImage> selectScenicImageByScenicId(@Param("scenicId") Integer scenicId);
}
