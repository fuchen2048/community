package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.aspectj.weaver.ast.Literal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	//@Autowired
	//private LoginTicketMapper loginTicketMapper;
	
	@Value("${community.path.domain}")
	private String domain;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	@Override
	public User findById(Integer id) {
		//return userMapper.selectById(id);
		User user = getCache(id);
		if (user == null) {
			user = initCache(id);
		}
		return user;
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
			clearCache(userId);
			return ACTIVATION_SUCCESS;
		} else {
			return ACTIVATION_FAILURE;
		}
	}
	
	@Override
	public Map<String, Object> login(String username, String password, Integer expiredSeconds) {
		Map<String, Object> map = new HashMap<>();
		
		//空值判断
		if (StringUtils.isBlank(username)) {
			map.put("usernameMsg", "老表，账号呢？");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("passwordMsg", "你这没有密码，可不好登录啊！");
			return map;
		}
		
		//验证账号
		User user = userMapper.selectByName(username);
		if (user == null) {
		    map.put("usernameMsg", "老表，账号是不是搞错了？");
			return map;
		}
		if (user.getStatus() == 0) {
			map.put("usernameMsg","请老表先去激个活吧！");
			return map;
		}
		
		
		//验证密码
		password = CommunityUtil.md5(password + user.getSalt());
		if (!password.equals(user.getPassword())) {
			map.put("passwordMsg", "？？？,密码好像有点问题。。。你说是吧，老表");
			return map;
		}
		
		
		//生成登录凭证
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(user.getId());
		loginTicket.setTicket(CommunityUtil.generateUUID());
		loginTicket.setStatus(0);
		loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
		//Integer integer = loginTicketMapper.insertLoginTicket(loginTicket);
		
		String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
		redisTemplate.opsForValue().set(redisKey, loginTicket);
		
		map.put("ticket", loginTicket.getTicket());
		
		return map;
	}
	
	@Override
	public void logout(String ticket) {
		//loginTicketMapper.updateStatus(ticket, 1);
		String redisKey = RedisKeyUtil.getTicketKey(ticket);
		LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
		loginTicket.setStatus(1);
		redisTemplate.opsForValue().set(redisKey, loginTicket);
	}
	
	@Override
	public LoginTicket findLoginTicket(String ticket) {
		//return loginTicketMapper.selectByTicket(ticket);
		String redisKey = RedisKeyUtil.getTicketKey(ticket);
		return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
	}
	
	@Override
	public Integer updateHeader(Integer userId, String header) {
		Integer rows = userMapper.updateHeader(userId, header);
		clearCache(userId);
		return rows;
	}
	
	@Override
	public User findUserByName(String username) {
		return userMapper.selectByName(username);
	}
	
	/**
	 * 优先从缓存中取值
	 * @param userId
	 * @return
	 */
	private User getCache(Integer userId){
		String redisKey = RedisKeyUtil.getUserKey(userId);
		return (User) redisTemplate.opsForValue().get(redisKey);
	}
	
	/**
	 * 取不到时初始化缓存数据
	 * @param userId
	 * @return
	 */
	private User initCache(Integer userId){
		User user = userMapper.selectById(userId);
		String redisKey = RedisKeyUtil.getUserKey(userId);
		redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
		return user;
	}
	
	/**
	 * 数据变更时删除数据
	 * @param userId
	 * @return
	 */
	private void clearCache(Integer userId){
		String redisKey = RedisKeyUtil.getUserKey(userId);
		redisTemplate.delete(redisKey);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(Integer userId) {
		User user = this.findById(userId);
		List<GrantedAuthority> list = new ArrayList<>();
		list.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				switch (user.getType()){
					case 1:
						return  AUTHORITY_ADMIN;
					case 2:
						return AUTHORITY_MODERATOR;
					default:
						return AUTHORITY_USER;
				}
			}
		});
		return list;
	}
}
