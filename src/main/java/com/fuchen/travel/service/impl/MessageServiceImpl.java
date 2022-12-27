package com.fuchen.travel.service.impl;

import com.fuchen.travel.entity.Message;
import com.fuchen.travel.mapper.MessageMapper;
import com.fuchen.travel.service.MessageService;
import com.fuchen.travel.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/14
 */
@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private SensitiveFilter sensitiveFilter;
	
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
	
	@Override
	public Integer addMessage(Message message) {
		message.setContent(HtmlUtils.htmlEscape(message.getContent()));
		message.setContent(sensitiveFilter.filter(message.getContent()));
		return messageMapper.insertMessage(message);
	}
	
	@Override
	public Integer readMessage(List<Integer> ids) {
		return messageMapper.updateStatus(ids, 1);
	}
	
	@Override
	public Message findLatestNotice(Integer userId, String topic) {
		return messageMapper.selectLatestNotice(userId, topic);
	}
	
	@Override
	public Integer findNoticeCount(Integer userId, String topic) {
		return messageMapper.selectNoticeCount(userId, topic);
	}
	
	@Override
	public Integer findNoticeUnreadCount(Integer userId, String topic) {
		return messageMapper.selectNoticeUnreadCount(userId, topic);
	}
	
	@Override
	public List<Message> findNotices(Integer userID, String topic, Integer offset, Integer limit) {
		return messageMapper.selectNotices(userID, topic, offset, limit);
	}
}
