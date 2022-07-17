package com.nowcoder.community.service;

/**
 * @author 伏辰
 * @date 2022/7/16
 * 点赞--业务层
 */
public interface LikeService {
	
	/**
	 * 点赞
	 * @param userId 谁点的赞
	 * @param entityType 点赞的实体
	 * @param entityId 实体的id
	 */
	void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId);
	
	/**
	 * 某实体点赞数量
	 * @param entityType 点赞的实体
	 * @param entityId 实体的id
	 * @return
	 */
	long findEntityLikeCount(Integer entityType, Integer entityId);
	
	/**
	 * 查询某人对某实体的点赞状态
	 * @param userId 某人id
	 * @param entityType 某实体
	 * @param entityId 某实体id
	 * @return
	 */
	Integer findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId);
	
	/**
	 * 查询某个用户的赞
	 * @param userId 用户的id
	 * @return
	 */
	Integer findUserLikeCount(Integer userId);
	
	
}
