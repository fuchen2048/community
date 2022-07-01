package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
}
