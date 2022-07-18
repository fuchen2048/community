package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 伏辰
 * @date 2022/7/18
 * 关注-业务层实现类
 */
@Service
public class FollowServiceImpl implements FollowService , CommunityConstant {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void follow(Integer userId, Integer entityType, Integer entityId) {
		redisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations redisOperations) throws DataAccessException {
				String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
				String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
				
				redisOperations.multi();
				
				redisOperations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
				redisOperations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
				return redisOperations.exec();
			}
		});
	}
	
	@Override
	public void unfollow(Integer userId, Integer entityType, Integer entityId) {
		redisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations redisOperations) throws DataAccessException {
				String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
				String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
				
				redisOperations.multi();
				
				redisOperations.opsForZSet().remove(followeeKey, entityId);
				redisOperations.opsForZSet().remove(followerKey, userId);
				return redisOperations.exec();
			}
		});
	}
	
	@Override
	public long findFolloweeCount(Integer userId, Integer entityType) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return redisTemplate.opsForZSet().zCard(followeeKey);
	}
	
	@Override
	public long findFollowerCount(Integer entityType, Integer entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return redisTemplate.opsForZSet().zCard(followerKey);
	}
	
	@Override
	public Boolean hasFollowed(Integer userId, Integer entityType, Integer entityId) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		if (redisTemplate.opsForZSet().score(followeeKey, entityId) != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public List<Map<String, Object>> findFollowees(Integer userId, Integer offset, Integer limit) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
		Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);
		
		if (targetIds == null && targetIds.size() ==  0) {
		    return  null;
		}
		
		List<Map<String, Object>> list = new ArrayList<>();
		for (Integer targetId : targetIds) {
			Map<String, Object> map = new HashMap<>();
			User user = userService.findById(targetId);
			Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
			map.put("user", user);
			map.put("followTime", new Date(score.longValue()));
			
			list.add(map);
		}
		
		return list;
	}
	
	@Override
	public List<Map<String, Object>> findFollowers(Integer userId, Integer offset, Integer limit) {
		String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
		Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
		
		if (targetIds == null && targetIds.size() == 0) {
			return  null;
		}
		
		List<Map<String, Object>> list = new ArrayList<>();
		for (Integer targetId : targetIds) {
			Map<String, Object> map = new HashMap<>();
			User user = userService.findById(targetId);
			Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
			map.put("user", user);
			map.put("followTime", new Date(score.longValue()));
			
			list.add(map);
		}
		
		return list;
	}
}
