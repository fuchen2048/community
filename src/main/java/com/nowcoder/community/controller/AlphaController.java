package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 伏辰
 * @date 2022/6/26
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {
	
	@Autowired
	private AlphaService alphaService;
	
	@RequestMapping("/hello")
	@ResponseBody
	public String hello(){
		return "Hello , Spring boot ";
	}
	
	@RequestMapping("/find")
	@ResponseBody
	public String find(){
		return alphaService.find();
	}
	
	@GetMapping("/teacher")
	public ModelAndView teacher(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("name" , "琳琳");
		modelAndView.addObject("age" , "18");
		modelAndView.setViewName("/view");
		return modelAndView;
	}
	
	@GetMapping("/school")
	public String school(Model model){
		model.addAttribute("name" , "郑州大学");
		model.addAttribute("age" , 40);
		return "view";
	}
	
	@GetMapping("/emp")
	@ResponseBody
	public Map<String , Object> getEmp(){
		HashMap<String, Object> map = new HashMap<>();
		map.put("姓名" , "琳琳");
		map.put("年龄" , 18);
		map.put("薪水" , 10000.00);
		return map;
	}
	
	@GetMapping("/emps")
	@ResponseBody
	public List<Map<String , Object>> getEmps(){
		List<Map<String , Object>> list = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("姓名" , "琳琳");
		map.put("年龄" , 18);
		map.put("薪水" , 10000.00);
		list.add(map);
		
		map = new HashMap<>();
		map.put("姓名" , "小巫");
		map.put("年龄" , 19);
		map.put("薪水" , 100000.00);
		list.add(map);
		
		map = new HashMap<>();
		map.put("姓名" , "阿虚");
		map.put("年龄" , 17);
		map.put("薪水" , 10000.00);
		list.add(map);
		
		return list;
	}
	
	@GetMapping("/cookie/set")
	@ResponseBody
	public String setCookie(HttpServletResponse response){
		Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
		//设置cookie生效的范围
		cookie.setPath("/community");
		//设置cookie的生存时间
		cookie.setMaxAge(60 * 10);
		//发送cookid
		response.addCookie(cookie);
		return "set cookie";
	}
	
	@GetMapping("/cookie/get")
	@ResponseBody
	public String getCookie(@CookieValue("code") String code){
		System.out.println(code);
		return "get cookie";
	}
	
	@GetMapping("/session/set")
	@ResponseBody
	public String setSession(HttpSession session){
		session.setAttribute("id" , 1);
		session.setAttribute("name", "张三");
		return "set session";
	}
	
	@GetMapping("/session/get")
	@ResponseBody
	public String getSession(HttpSession session){
		System.out.println(session.getAttribute("id"));
		System.out.println(session.getAttribute("name"));
		return "get session";
	}
}
