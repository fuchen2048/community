package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.Alphainterceptor;
import com.nowcoder.community.controller.interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 伏辰
 * @date 2022/7/4
 * 拦截器配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private Alphainterceptor alphainterceptor;
	
	@Autowired
	private LoginTicketInterceptor loginTicketInterceptor;
	
	@Autowired
	private LoginRequiredInterceptor loginRequiredInterceptor;
	
	
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(alphainterceptor)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
		
		registry.addInterceptor(loginTicketInterceptor)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
		
		registry.addInterceptor(loginRequiredInterceptor)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
		
	}
	
	
}
