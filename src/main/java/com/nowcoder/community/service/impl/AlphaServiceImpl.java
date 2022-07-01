package com.nowcoder.community.service.impl;

import com.nowcoder.community.service.AlphaService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author 伏辰
 * @date 2022/6/27
 */
@Service
public class AlphaServiceImpl implements AlphaService {
	
	public AlphaServiceImpl(){
		System.out.println("实例化AlphaService");
	}
	
	@PostConstruct  //构造方法之后调用
	@Override
	public void init() {
		System.out.println("初始化AlphaService");
	}
	
	@PreDestroy //销毁之前调用
	public void destroy(){
		System.out.println("销毁AlphaService");
	}
	
	@Override
	public String find() {
		return "Service中的find方法";
	}
}
