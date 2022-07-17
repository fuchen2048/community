package com.nowcoder.community.util;

/**
 * @author 伏辰
 * @date 2022/7/16
 * redis工具类
 */
public class RedisKeyUtil {
	
	private static final String SPLIT = ":";
	private static final String PREFIX_ENTITY_LIKE = "like:entity";
	
	/**
	 * 某个实体类的赞
	 * @param entityType 实体类型
	 * @param entityId 实体id
	 * @return
	 */
	public static String getEntityLikeKey(Integer entityType, Integer entityId){
		return PREFIX_ENTITY_LIKE + SPLIT  + entityType + SPLIT +entityId;
	}
}
