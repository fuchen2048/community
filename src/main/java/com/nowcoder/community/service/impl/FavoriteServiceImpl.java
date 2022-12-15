package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.Favorite;
import com.nowcoder.community.entity.Scenic;
import com.nowcoder.community.mapper.FavoriteMapper;
import com.nowcoder.community.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/12/4
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public Integer addCollection(Integer userId, Integer scenicId, Date createTime) {
        return favoriteMapper.insertCollection(userId, scenicId, createTime);
    }

    @Override
    public Favorite findByUserIdAndScenicId(Integer userId, Integer scenicId) {
        return favoriteMapper.selectByUserIdAndScenicId(userId, scenicId);
    }

    @Override
    public Integer removeCollection(Integer userId, Integer scenicId) {
        return favoriteMapper.deleteCollection(userId, scenicId);
    }

    @Override
    public Integer getCollectionCount(Integer scenicId) {
        return favoriteMapper.selectCollectionCount(scenicId);
    }

    @Override
    public List<Scenic> findCollectionByCount(Integer offset, Integer limit) {
        return favoriteMapper.selectCollectionByCount(offset, limit);
    }

    @Override
    public List<Scenic> findCollectionAllByUserId(Integer userId, Integer offset, Integer limit) {
        return favoriteMapper.selectCollectionAllByUserId(userId, offset, limit);
    }

    @Override
    public Integer findCollectionCountByUserId(Integer userId) {
        return favoriteMapper.selectCollectionCountByUserId(userId);
    }

    @Override
    public Integer findtCollectionCountAll() {
        return favoriteMapper.selectCollectionCountAll();
    }
}
