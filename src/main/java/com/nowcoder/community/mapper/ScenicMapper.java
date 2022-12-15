package com.nowcoder.community.mapper;


import com.nowcoder.community.entity.Scenic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/11/26
 */
@Mapper
public interface ScenicMapper {

    /**
     * 查询所有景点信息
     * @return
     */
    List<Scenic> selectAllScenic();

    /**
     * 根据id查询景点信息
     * @param id 景点id
     * @return
     */
    Scenic selectScenicById(@Param("id") Integer id);

    /**
     * 查询所有景点的个数
     * @return
     */
    Integer selectScenicCountAll();

}
