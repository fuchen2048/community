package com.fuchen.travel.mapper;


import com.fuchen.travel.entity.Scenic;
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
    List<Scenic> selectAllScenic(@Param("offset") Integer offset, @Param("limit") Integer limit);

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


    /**
     * 通过景区名称查询景区信息
     * @param scenicName 景区名称
     * @return 景区信息集合
     */
    List<Scenic> selectScenicByScenicName(@Param("scenicName") String scenicName, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 通过关键字查询景区数量
     * @param keyword 关键字
     * @return
     */
    Integer selectToScenicCount(@Param("keyword") String keyword);
}
