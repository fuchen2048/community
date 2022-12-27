package com.fuchen.travel.service;


import com.fuchen.travel.entity.Favorite;
import com.fuchen.travel.entity.Scenic;

import java.util.Date;
import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/12/4
 */
public interface FavoriteService {


    /**
     * 添加收藏
     * @param userId 用户id
     * @param scenicId 景点id
     * @param createTime 创建时间
     * @return
     */
    Integer addCollection(Integer userId, Integer scenicId, Date createTime);

    /**
     * 查询用户收藏指定的景点
     * @param userId 用户id
     * @param scenicId 景点id
     * @return
     */
    Favorite findByUserIdAndScenicId(Integer userId, Integer scenicId);

    /**
     * 取消收藏
     * @param userId 用户id
     * @param scenicId 景点id
     * @return
     */
    Integer removeCollection(Integer userId, Integer scenicId);

    /**
     * 获取收藏次数
     * @param scenicId
     * @return
     */
    Integer getCollectionCount(Integer scenicId);

    /**
     *  查询收藏次数大的四个收藏信息
     * @return
     */
    List<Scenic> findCollectionByCount(Integer offset, Integer limit);

    /**
     * 查询用户的收藏景点信息
     * @param userId 用户id
     * @return
     */
    List<Scenic> findCollectionAllByUserId(Integer userId, Integer offset, Integer limit);

    /**
     * 查询用户收藏景点的个数
     * @param userId 用户id
     * @return
     */
    Integer findCollectionCountByUserId(Integer userId);

    /**
     * 查询所有用户的收藏景点的个数
     * @return
     */
    Integer findtCollectionCountAll();
}
