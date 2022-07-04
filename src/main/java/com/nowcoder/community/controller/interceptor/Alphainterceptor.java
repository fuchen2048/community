package com.nowcoder.community.controller.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 伏辰
 * @date 2022/7/4
 */
@Slf4j
@Component
public class Alphainterceptor implements HandlerInterceptor {
	
	//Controller之前执行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.debug("这里是：preHandler方法" +handler.toString());
		return true;
	}
	
	//Controller之后执行
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		log.debug("这里是：postHandle方法" + handler.toString());
	}
	
	//模板引擎之后执行
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		log.debug("这里是：afterCompletion方法" + handler.toString());
	}
}
