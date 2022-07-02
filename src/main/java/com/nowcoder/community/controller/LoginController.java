package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author 伏辰
 * @date 2022/7/2
 * 登录-控制层
 */
@Controller
public class LoginController implements CommunityConstant {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 前往注册页面
	 * @return 注册页面
	 */
	@GetMapping("/register")
	public String getRegisterPage(){
		return "/site/register";
	}
	
	/**
	 * 前往登录页面
	 * @return 登录页面
	 */
	@GetMapping("/login")
	public String getLoginPage(){
		return "/site/login";
	}
	
	/**
	 * 接收用户注册请求
	 * @param model 页面模型
	 * @param user User对象
	 * @return 相应页面
	 */
	@PostMapping("/register")
	public String register(Model model, User user){
		Map<String, Object> map = userService.register(user);
		
		if (map == null || map.isEmpty()) {
			model.addAttribute("msg","注册成功，请前往邮箱进行激活~~~");
			model.addAttribute("target","/index");
			return "/site/operate-result";
		} else {
			model.addAttribute("usernameMsg", map.get("usernameMsg"));
			model.addAttribute("passwordMsg", map.get("passwordMsg"));
			model.addAttribute("emailMsg", map.get("emailMsg"));
			return "/site/register";
		}
	}
	
	/**
	 * 用户激活请求
	 * @param model 模型
	 * @param userId 用户id
	 * @param code 激活码
	 * @return 激活结果页面
	 */
	@GetMapping("/activation/{userId}/{code}")
	public String activation(Model model, @PathVariable("userId") Integer userId, @PathVariable("code") String code){
		Integer result = userService.activation(userId, code);
		if (result == ACTIVATION_SUCCESS) {
			model.addAttribute("msg","激活成功！！！");
			model.addAttribute("target","/login");
		} else if (result == ACTIVATION_REPEAT) {
			model.addAttribute("msg","无效操作，请勿重复激活！！！");
			model.addAttribute("target","/index");
		} else {
			model.addAttribute("msg","激活失败,激活码有误！！！");
			model.addAttribute("target","/index");
		}
		return "/site/operate-result";
	}
}
