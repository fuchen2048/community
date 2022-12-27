package com.fuchen.travel.controller.interceptor;

import com.fuchen.travel.entity.User;
import com.fuchen.travel.service.DataService;
import com.fuchen.travel.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 伏辰
 * @date 2022/7/23
 */
@Component
public class DataInterceptor implements HandlerInterceptor {
	
	@Autowired
	private DataService dateService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//统计UV
		String ip = request.getRemoteHost();
		dateService.recordUV(ip);
		
		//统计DAU
		User user = hostHolder.getUser();
		if (user != null) {
		    dateService.recordDAU(user.getId());
		}
		
		return true;
	}
}
