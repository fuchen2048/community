package com.nowcoder.community.service.impl;

import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 伏辰
 * @date 2022/7/16
 */
@Service
public class LikeServiceImpl implements LikeService {
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public void like(Integer userId, Integer entityType, Integer entityId) {
		String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
		Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
		if (isMember) {
		    redisTemplate.opsForSet().remove(entityLikeKey, userId);
		} else {
		    redisTemplate.opsForSet().add(entityLikeKey, userId);
		}
	}
	
	@Override
	public long findEntityLikeCount(Integer entityType, Integer entityId){
		String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
		return redisTemplate.opsForSet().size(entityLikeKey);
	}
	
	@Override
	public Integer findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId) {
		String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
		return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
	}
}
