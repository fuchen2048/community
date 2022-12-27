package com.fuchen.travel.controller;

import com.fuchen.travel.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author 伏辰
 * @date 2022/7/23
 */
@Controller
public class DataController {

	@Autowired
	private DataService dataService;
	
	/**
	 * 统计页面
	 * @return
	 */
	@RequestMapping(path = "/data", method = {RequestMethod.GET, RequestMethod.POST})
	public String getDataPage(){
		return "/site/admin/data";
	}
	
	/**
	 * 统计网站uv
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param model
	 * @return
	 */
	@PostMapping("/data/uv")
	public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
		long uv = dataService.calculateUV(start, end);
		model.addAttribute("uvResult", uv);
		model.addAttribute("uvStartDate", start);
		model.addAttribute("uvEndDate", end);
		return "forward:/data";
	}
	
	/**
	 * 统计网站dau
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param model
	 * @return
	 */
	@PostMapping("/data/dau")
	public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
		long dau = dataService.calculateDAU(start, end);
		model.addAttribute("dauResult", dau);
		model.addAttribute("dauStartDate", start);
		model.addAttribute("dauEndDate", end);
		return "forward:/data";
	}
}
