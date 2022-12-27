package com.fuchen.travel.service;

import com.fuchen.travel.entity.LoginTicket;
import com.fuchen.travel.entity.User;
import org.springframework.security.core.GrantedAuthority;

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


	/**
	 * 发送邮箱验证码
	 * @param email 邮箱
	 */
	void verificationCode(String email);


	/**
	 * 根据邮箱修改密码
	 * @param email 邮箱
	 * @param password 需要修改的密码
	 * @return
	 */
	User updatePassword(String email, String password);

	/**
	 * 根据userId修改密码
	 * @param user 用户
	 * @param password 新密码
	 * @return
	 */
	Integer updatePasswordByUserId(User user);
	
	Collection<? extends GrantedAuthority> getAuthorities(Integer userId);
}
