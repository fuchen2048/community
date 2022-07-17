package com.nowcoder.community.mapper;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/14
 * 私信-持久层
 */
@Mapper
public interface MessageMapper {
	
	/**
	 * 查询当前用户的会话列表，针对每个会话只返回最新的私信
	 * @param userId 当前用户的id
	 * @param offset 当前页的起始行
	 * @param limit 当前页的显示条数
	 * @return Message的List集合
	 */
	List<Message> selectConversations(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
	
	/**
	 * 查询用户的会话数量
	 * @param userId 当前用户id
	 * @return 查询数量
	 */
	Integer selectConversationCount(@Param("userId") Integer userId);
	
	/**
	 * 查询某个会话所包含的私信列表
	 * @param conversationId 私信id
	 * @param offset 当前起始行号
	 * @param limit 当前显示条数
	 * @return Message的List集合
	 */
	List<Message> selectLetters(@Param("conversationId") String conversationId, @Param("offset") Integer offset, @Param("limit") Integer limit);
	
	/**
	 * 查询某个会话所包含的私信数量
	 * @param conversationId 私信id
	 * @return 私信数量
	 */
	Integer selectLetterCount(@Param("conversationId")String conversationId);
	
	/**
	 * 查询用户私信未读数量
	 * @param userId 当前用户id
	 * @param conversationId  私信id
	 * @return 未读私信数量
	 */
	Integer selectLetterUnreadCount(@Param("userId") Integer userId, @Param("conversationId")String conversationId);
	
	/**
	 * 新增消息
	 * @param message 需要新增的消息实体
	 * @return 增加行数
	 */
	Integer insertMessage(Message message);
	
	/**
	 * 修改消息的状态
	 * @param ids id的List集合
	 * @param status 状态码
	 * @return 修改行数
	 */
	Integer updateStatus(@Param("ids") List<Integer> ids, @Param("status") Integer status);
	
}
