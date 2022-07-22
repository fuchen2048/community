package com.nowcoder.community.service;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 用户-服务层
 */

public interface UserService {
	/**
	 * 通过用户id查询用户信息
	 * @param id 用户id
	 * @return User对象
	 */
	User findById(Integer id);
	
	/**
	 * 用户注册
	 * @param user 接受一个User对象
	 * @return 返回一个map集合
	 */
	Map<String , Object> register(User user);
	
	/**
	 * 激活
	 * @param userId 用户id
	 * @param code 激活码
	 * @return 激活状态码
	 */
	Integer activation(Integer userId, String code);
	
	/**
	 * 用户登录
	 * @param username 用户名
	 * @param password 密码
	 * @param expiredSeconds 时间
	 * @return Map集合
	 */
	Map<String, Object> login(String username, String password, Integer expiredSeconds);
	
	/**
	 * 退出登录
	 * @param ticket 登录凭证
	 */
	void logout(String ticket);
	
	/**
	 * 查询登录凭证
	 * @param ticket
	 * @return
	 */
	LoginTicket findLoginTicket(String ticket);
	
	/**
	 * 修改header
	 * @param userId 用户id
	 * @param header
	 * @return 修改行数
	 */
	Integer updateHeader(Integer userId, String header);
	
	/**
	 * 通过用户名查询用户信息
	 * @param username 用户名
	 * @return 用户信息
	 */
	User findUserByName(String username);
	
	Collection<? extends GrantedAuthority> getAuthorities(Integer userId);
}
