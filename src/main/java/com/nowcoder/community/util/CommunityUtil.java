package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author 伏辰
 * @date 2022/7/2
 * 加密工具类
 */
public class CommunityUtil {
	
	/**
	 * 生成随机字符串
	 * @return UUID
	 */
	public static String generateUUID(){
		return UUID.randomUUID().toString().replaceAll("-","");
	}
	
	/**
	 * MD5加密算法
	 * @param key 盐值
	 * @return 加密后的结果
	 */
	public static String md5(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		return DigestUtils.md5DigestAsHex(key.getBytes());
	}
	
}
