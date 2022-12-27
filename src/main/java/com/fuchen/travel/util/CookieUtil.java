package com.fuchen.travel.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 伏辰
 * @date 2022/7/4
 * 获取Cookie工具类
 */
public class CookieUtil {
	
	public static String getValue(HttpServletRequest request, String name){
		if (request == null || name == null) {
		    throw new IllegalArgumentException("参数为空！");
		}
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if (cookie.getName().equals(name)) {
					return cookie.getValue();
		        }
		    }
		}
		return null;
	}
}
