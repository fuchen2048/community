package com.fuchen.travel.service.impl;

import com.fuchen.travel.entity.Comment;
import com.fuchen.travel.mapper.CommentMapper;
import com.fuchen.travel.service.CommentService;
import com.fuchen.travel.service.DiscussPostService;
import com.fuchen.travel.util.TravelConstant;
import com.fuchen.travel.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/12
 * 显示评论实现类
 */
@Service
public class CommentServiceImpl implements CommentService, TravelConstant {
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private SensitiveFilter sensitiveFilter;
	
	@Autowired
	private DiscussPostService discussPostService;


	@Override
	public List<Comment> findCommentByUserId(Integer userId, Integer offset, Integer limit) {
		return commentMapper.selectCommentByUserId(userId, offset, limit);
	}

	@Override
	public Integer findCommentByUserIdCount(Integer userId) {
		return commentMapper.selectCommentByUserIdCount(userId);
	}

	@Override
	public List<Comment> findCommentsByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit) {
		return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
	}
	
	@Override
	public Integer findCountByEntity(Integer entityType, Integer entityId) {
		return commentMapper.selectCountByEntity(entityType, entityId);
	}
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Integer addComment(Comment comment) {
		//过滤敏感词
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		comment.setContent(sensitiveFilter.filter(comment.getContent()));
		
		//添加评论
		int row = commentMapper.insertComment(comment);
		
		//更新帖子的评论数量
		if (comment.getEntityType() == ENTITY_TYPE_POST) {
			int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
			discussPostService.updateCommentCount(comment.getId(), count);
		}
		
		return row;
	}
	
	@Override
	public Comment findCommentById(Integer id) {
		return commentMapper.selectCommentById(id);
	}
}
