package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 用户-服务层-实现类
 */
@Service
public class UserServiceImpl implements UserService , CommunityConstant {
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MailClient mailClient;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Value("${community.path.domain}")
	private String domain;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	@Override
	public User findById(Integer id) {
		return userMapper.selectById(id);
	}
	
	@Override
	public Map<String, Object> register(User user) {
		Map<String, Object> map = new HashMap<>();
		
		//空值判断
		if (user == null) {
		    throw new IllegalArgumentException("参数不允许空！");
		}
		if (StringUtils.isBlank(user.getUsername())) {
			map.put("usernameMsg", "账号不能为空！");
			return map;
		}
		if (StringUtils.isBlank(user.getPassword())) {
			map.put("passwordMsg", "密码不能为空！");
			return map;
		}
		if (StringUtils.isBlank(user.getEmail())) {
			map.put("emailMsg", "邮箱不能为空！");
			return map;
		}
		
		//验证账号
		User selectUser = userMapper.selectByName(user.getUsername());
		if (selectUser != null){
			map.put("usernameMsg", "该账号已存在！");
			return map;
		}
		
		//验证邮箱
		selectUser = userMapper.selectByEmail(user.getEmail());
		if (selectUser != null){
			map.put("emailMsg", "该邮箱已被使用！");
			return map;
		}
		
		//注册用户
		user.setSalt(CommunityUtil.generateUUID().substring(0 , 5));
		user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
		user.setType(0);
		user.setStatus(0);
		user.setActivationCode(CommunityUtil.generateUUID());
		user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		user.setCreateTime(new Date());
		userMapper.insertUser(user);
		
		//激活邮件
		Context context = new Context();
		context.setVariable("email",user.getEmail());
		String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
		context.setVariable("url" , url);
		String content = templateEngine.process("/mail/activation", context);
		mailClient.sendMail(user.getEmail(), "激活账号", content);
		return map;
	}
	
	public Integer activation(Integer userId, String code){
		User user = userMapper.selectById(userId);
		if (user.getStatus() == 1) {
			return ACTIVATION_REPEAT;
		} else if (user.getActivationCode().equals(code)) {
			userMapper.updateStatus(userId,1);
			return ACTIVATION_SUCCESS;
		} else {
			return ACTIVATION_FAILURE;
		}
	}
}
