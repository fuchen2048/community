package com.fuchen.travel.service.impl;

import com.fuchen.travel.service.LikeService;
import com.fuchen.travel.util.RedisKeyUtil;
//import jdk.dynalink.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
	public void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId) {
		redisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations redisOperations) throws DataAccessException {
				String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
				String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
				Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
				redisOperations.multi();
				if (isMember) {
					redisOperations.opsForSet().remove(entityLikeKey, userId);
					redisOperations.opsForValue().decrement(userLikeKey);
				} else {
					redisOperations.opsForSet().add(entityLikeKey, userId);
					redisOperations.opsForValue().increment(userLikeKey);
				}
				return redisOperations.exec();
			}
		});
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
	
	@Override
	public Integer findUserLikeCount(Integer userId) {
		String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
		Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
		if (count == null) {
		    return 0;
		}
		return count.intValue();
	}
}
