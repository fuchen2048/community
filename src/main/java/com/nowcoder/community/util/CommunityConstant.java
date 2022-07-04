package com.nowcoder.community.util;

/**
 * @author 伏辰
 * @date 2022/7/2
 * 激活状态码
 */
public interface CommunityConstant {
	/**
	 * 激活成功
	 */
	int ACTIVATION_SUCCESS = 0;
	
	/**
	 * 重复激活
	 */
	int ACTIVATION_REPEAT = 1;
	
	/**
	 * 激活失败
	 */
	int ACTIVATION_FAILURE = 2;
	
	/**
	 * 默认状态的登录凭证的超时时间
	 */
	int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
	
	int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
	
}
