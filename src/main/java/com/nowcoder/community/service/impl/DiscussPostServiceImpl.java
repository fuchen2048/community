package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 帖子服务层实现类
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
	@Autowired
	private DiscussPostMapper discussPostMapper;
	@Override
	public List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit) {
		return discussPostMapper.selectDiscussPosts(userId, offset, limit);
	}
	
	@Override
	public Integer findDiscussPostRows(Integer userId) {
		System.out.println("这里是service" + userId);
		Integer rows = discussPostMapper.selectDiscussPostRows(userId);
		System.out.println(rows);
		return rows;
	}
}
