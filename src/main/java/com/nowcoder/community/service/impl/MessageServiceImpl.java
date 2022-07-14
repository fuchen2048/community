package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.mapper.MessageMapper;
import com.nowcoder.community.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/14
 */
@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Override
	public List<Message> findConversations(Integer userId, Integer offset, Integer limit) {
		return messageMapper.selectConversations(userId, offset, limit);
	}
	
	@Override
	public Integer findConversationCount(Integer userId) {
		return messageMapper.selectConversationCount(userId);
	}
	
	@Override
	public List<Message> findLetters(String conversationId, Integer offset, Integer limit) {
		return messageMapper.selectLetters(conversationId, offset, limit);
	}
	
	@Override
	public Integer findLetterCount(String conversationId) {
		return messageMapper.selectLetterCount(conversationId);
	}
	
	@Override
	public Integer findLetterUnreadCount(Integer userId, String conversationId) {
		return messageMapper.selectLetterUnreadCount(userId, conversationId);
	}
}
