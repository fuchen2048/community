package com.nowcoder.community.service;

import com.mysql.cj.log.Log;

import java.util.List;
import java.util.Map;

/**
 * @author 伏辰
 * @date 2022/7/18
 * 关注-业务层
 */
public interface FollowService {
	
	/**
	 * 关注
	 * @param userId
	 * @param entityType
	 * @param entityId
	 */
	void follow(Integer userId, Integer entityType, Integer entityId);
	
	/**
	 * 取消关注
	 * @param userId
	 * @param entityType
	 * @param entityId
	 */
	void unfollow(Integer userId, Integer entityType, Integer entityId);
	
	/**
	 * 查询关注的实体数量
	 * @param userId 用户id
	 * @param entityType 实体
	 * @return
	 */
	long findFolloweeCount(Integer userId, Integer entityType);
	
	/**
	 * 查询实体的粉丝数量
	 * @param entityType 实体
	 * @param entityId
	 * @return
	 */
	long findFollowerCount(Integer entityType, Integer entityId);
	
	/**
	 * 查询当前用户的关注状态
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	Boolean hasFollowed(Integer userId, Integer entityType, Integer entityId);
	
	/**
	 * 查询某用户关注的人
	 * @param userId 某用户的id
	 * @param offset 起始行
	 * @param limit 显示条数
	 * @return
	 */
	List<Map<String, Object>> findFollowees (Integer userId, Integer offset, Integer limit);
	
	/**
	 * 查询某用户的粉丝
	 * @param userId 某用户的id
	 * @param offset 起始行
	 * @param limit 显示条数
	 * @return
	 */
	List<Map<String, Object>> findFollowers (Integer userId, Integer offset, Integer limit);
	
}
