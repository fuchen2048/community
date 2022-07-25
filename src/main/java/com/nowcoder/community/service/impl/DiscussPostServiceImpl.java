package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 帖子-服务层-实现类
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
	@Autowired
	private DiscussPostMapper discussPostMapper;
	
	@Autowired
	private SensitiveFilter sensitiveFilter;
	
	@Override
	public List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit, Integer orderMode) {
		return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode);
	}
	
	@Override
	public Integer findDiscussPostRows(Integer userId) {
		Integer rows = discussPostMapper.selectDiscussPostRows(userId);
		return rows;
	}
	
	@Override
	public Integer addDiscussPost(DiscussPost post) {
		if (post == null) {
			throw new IllegalArgumentException("参数不能为空！！");
		}
		//转义HTML标记
		post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
		post.setContent(HtmlUtils.htmlEscape(post.getContent()));
		
		//过滤敏感词
		post.setTitle(sensitiveFilter.filter(post.getTitle()));
		post.setContent(sensitiveFilter.filter(post.getContent()));
		
		return discussPostMapper.insertDiscussPost(post);
	}
	
	@Override
	public DiscussPost findDiscussPost(Integer id) {
		return discussPostMapper.selectDiscussPost(id);
	}
	
	@Override
	public Integer updateCommentCount(Integer id, Integer commentCount) {
		return discussPostMapper.updateCommentCount(id, commentCount);
	}
	
	@Override
	public Integer updateType(Integer id, Integer type) {
		return discussPostMapper.updateType(id, type);
	}
	
	@Override
	public Integer updateStatus(Integer id, Integer status) {
		return discussPostMapper.updateStatus(id, status);
	}
	
	@Override
	public Integer updateScore(Integer id, Double score) {
		return discussPostMapper.updateScore(id, score);
	}
}
