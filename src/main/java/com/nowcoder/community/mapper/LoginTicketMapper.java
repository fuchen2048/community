package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 伏辰
 * @date 2022/7/4
 * 登录凭证-持久层
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {
	/**
	 * 添加登录凭证
	 * @param loginTicket LoginTicket对象
	 * @return 添加行数
	 */
	Integer insertLoginTicket(@Param("loginTicket") LoginTicket loginTicket);
	
	/**
	 * 根据凭证查询登录凭证信息
	 * @param ticket 凭证
	 * @return LoginTicket对象
	 */
	LoginTicket selectByTicket(@Param("ticket") String ticket);
	
	/**
	 * 根据凭据修改状态码
	 * @param ticket 凭证
	 * @param status 状态码
	 * @return 修改行数
	 */
	Integer updateStatus(@Param("ticket") String ticket, @Param("status") Integer status);
}
