package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Service;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 用户服务层接口
 */

public interface UserService {
	/**
	 * 通过用户id查询用户信息
	 * @param id 用户id
	 * @return User对象
	 */
	User findById(Integer id);
}
