package com.fuchen.travel.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 伏辰
 * @date 2022/7/23
 *
 */
public interface DataService {
	
	/**
	 * 将ip计入UV
	 * @param ip ip地址
	 */
	void recordUV(String ip);
	
	/**
	 * 统计日期范围内的UV
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return
	 */
	long calculateUV(Date start, Date end);
	
	/**
	 * 将指定用户计入DAU
	 * @param userId 用户id
	 */
	void recordDAU(Integer userId);
	
	/**
	 * 统计指定范围内的DAU
	 * @param start 开始时间
	 * @param end 开始时间
	 * @return
	 */
	long calculateDAU(Date start, Date end);
}