package com.fuchen.travel.mapper;

import com.fuchen.travel.entity.Favorite;
import com.fuchen.travel.entity.Scenic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/12/4
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 用户收藏景点
     * @param userId 用户id
     * @param scenicId 景点id
     * @param createTime 收藏时间
     * @return
     */
    Integer insertCollection(@Param("userId") Integer userId, @Param("scenicId") Integer scenicId, @Param("createTime")Date createTime);


    /**
     * 查询用户指定的景点
     * @param userId 用户id
     * @param scenicId 景点id
     * @return
     */
    Favorite selectByUserIdAndScenicId(@Param("userId") Integer userId, @Param("scenicId") Integer scenicId);

    /**
     * 删除用户指定景点收藏信息
     * @param userId
     * @param scenicId
     * @return
     */
    Integer deleteCollection(@Param("userId") Integer userId, @Param("scenicId") Integer scenicId);

    /**
     * 查询景点收藏次数
     * @return
     */
    Integer selectCollectionCount(@Param("scenicId") Integer scenicId);

    /**
     * 查询收藏次数大的四个收藏信息
     * @return
     */
    List<Scenic> selectCollectionByCount(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 查询用户的收藏景点信息
     * @param userId 用户id
     * @return
     */
    List<Scenic> selectCollectionAllByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 查询用户收藏景点的个数
     * @param userId 用户id
     * @return
     */
    Integer selectCollectionCountByUserId(@Param("userId") Integer userId);

    /**
     * 查询所有用户收藏景点个数
     * @return
     */
    Integer selectCollectionCountAll();
}
