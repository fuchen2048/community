package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author 伏辰
 * @date 2022/7/2
 * 登录-控制层
 */
@Slf4j
@Controller
public class LoginController implements CommunityConstant {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Producer kaptchaProducer;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
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
	
	/**
	 * 验证码图片
	 * @param response
	 * @param session 用于将验证码存入session
	 */
	@GetMapping("/kaptcha")
	public void getKaptcha(HttpServletResponse response, HttpSession session){
		//生成验证码
		String text = kaptchaProducer.createText();
		BufferedImage image = kaptchaProducer.createImage(text);
		
		//将验证码存入session
		session.setAttribute("kaptcha", text);
		
		//将图片输出到浏览器
		response.setContentType("image/png");
		try {
			OutputStream outputStream = response.getOutputStream();
			ImageIO.write(image, "png", outputStream);
		} catch (IOException e) {
			log.error("响应验证码失败" + e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public String login(String username, String password, String code, boolean rememberMe, Model model, HttpSession session, HttpServletResponse response){
		
		//检查验证码
		String kaptcha = (String) session.getAttribute("kaptcha");
		if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
			model.addAttribute("codeMsg", "好兄弟，你的验证码可不对啊，快点再来一遍。。。");
			return "/site/login";
		}
		
		//检查账号、密码
		int expireSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
		Map<String, Object> map = userService.login(username, password, expireSeconds);
		if (map.containsKey("ticket")) {
			Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
			cookie.setPath(contextPath);
			cookie.setMaxAge(expireSeconds);
			response.addCookie(cookie);
			System.out.println("123");
			return "redirect:/index";
		} else {
			model.addAttribute("usernameMsg", map.get("usernameMsg"));
			model.addAttribute("passwordMsg", map.get("passwordMsg"));
			return "/site/login";
		}
	}
	@GetMapping("/logout")
	public String logout(@CookieValue("ticket") String ticket){
		userService.logout(ticket);
		return "redirect:/index";
	}
}
