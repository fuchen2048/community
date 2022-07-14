package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.SensitiveFilter;
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
public class CommentServiceImpl implements CommentService , CommunityConstant {
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private SensitiveFilter sensitiveFilter;
	
	@Autowired
	private DiscussPostService discussPostService;
	
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
}
