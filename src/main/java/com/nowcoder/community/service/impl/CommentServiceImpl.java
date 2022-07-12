package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/12
 * 显示评论实现类
 */
@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Override
	public List<Comment> findCommentsByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit) {
		return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
	}
	
	@Override
	public Integer findCountByEntity(Integer entityType, Integer entityId) {
		return commentMapper.selectCountByEntity(entityType, entityId);
	}
}
