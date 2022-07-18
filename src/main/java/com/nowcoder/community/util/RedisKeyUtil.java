package com.nowcoder.community.util;

/**
 * @author 伏辰
 * @date 2022/7/16
 * redis工具类
 */
public class RedisKeyUtil {
	
	private static final String SPLIT = ":";
	private static final String PREFIX_ENTITY_LIKE = "like:entity";
	private static final String PREFIX_USER_LIKE = "like:user";
	private static final String PREFIX_FOLLOWER = "follower";
	private static final String PREFIX_FOLLOWEE = "followee";
	
	/**
	 * 某个实体类的赞
	 * @param entityType 实体类型
	 * @param entityId 实体id
	 * @return
	 */
	public static String getEntityLikeKey(Integer entityType, Integer entityId){
		return PREFIX_ENTITY_LIKE + SPLIT  + entityType + SPLIT +entityId;
	}
	
	/**
	 * 某个用户的赞
	 * @param userId 用户id
	 * @return
	 */
	public static String getUserLikeKey(Integer userId){
		return PREFIX_USER_LIKE + SPLIT + userId;
	}
	
	/**
	 * 某个用户关注的实体
	 * @param userId
	 * @param entityType
	 * @return
	 */
	public static String getFolloweeKey(Integer userId, Integer entityType){
		return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
	}
	
	/**
	 * 某个实体拥有的粉丝
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public static String getFollowerKey(Integer entityType, Integer entityId){
		return   PREFIX_FOLLOWER + SPLIT + + entityType + SPLIT + entityId;
	}
}
