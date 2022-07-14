package com.nowcoder.community.service;

import com.nowcoder.community.entity.Message;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/14
 * 私信-服务层
 */
public interface MessageService {
	
	/**
	 * 查询当前用户的会话列表，针对每个会话只返回最新的私信
	 * @param userId 当前用户的id
	 * @param offset 当前页的起始行
	 * @param limit 当前页的显示条数
	 * @return Message的List集合
	 */
	List<Message> findConversations(Integer userId, Integer offset, Integer limit);
	
	/**
	 * 查询用户的会话数量
	 * @param userId 当前用户id
	 * @return 查询数量
	 */
	Integer findConversationCount(Integer userId);
	
	/**
	 * 查询某个会话所包含的私信列表
	 * @param conversationId 私信id
	 * @param offset 当前起始行号
	 * @param limit 当前显示条数
	 * @return Message的List集合
	 */
	List<Message> findLetters(String conversationId, Integer offset, Integer limit);
	
	/**
	 * 查询某个会话所包含的私信数量
	 * @param conversationId 私信id
	 * @return 私信数量
	 */
	Integer findLetterCount(String conversationId);
	
	/**
	 * 查询用户私信未读数量
	 * @param userId 当前用户id
	 * @param conversationId  私信id
	 * @return 未读私信数量
	 */
	Integer findLetterUnreadCount(Integer userId, String conversationId);
}
