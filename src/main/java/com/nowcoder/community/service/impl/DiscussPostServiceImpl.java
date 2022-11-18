package com.nowcoder.community.service.impl;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.SensitiveFilter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 帖子-服务层-实现类
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
	
	private static final Logger log = LoggerFactory.getLogger(DiscussPostServiceImpl.class);
	@Autowired
	private DiscussPostMapper discussPostMapper;
	
	@Autowired
	private SensitiveFilter sensitiveFilter;
	
	@Value("${caffeine.posts.max-size}")
	private int maxSize;
	
	@Value("${caffeine.posts.expire-seconds}")
	private int expireSeconds;
	
	// 帖子列表缓存
	private LoadingCache<String, List<DiscussPost>> postListCache;
	
	// 帖子总数缓存
	private LoadingCache<Integer, Integer> postRowsCache;
	
	@PostConstruct
	public void init() {
		// 初始化帖子列表缓存
		postListCache = Caffeine.newBuilder()
				.maximumSize(maxSize)
				.expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
				.build(new CacheLoader<String, List<DiscussPost>>() {
					@Nullable
					@Override
					public List<DiscussPost> load(@NonNull String key) throws Exception {
						if (key == null || key.length() == 0) {
							throw new IllegalArgumentException("参数错误!");
						}
						
						String[] params = key.split(":");
						if (params == null || params.length != 2) {
							throw new IllegalArgumentException("参数错误!");
						}
						
						int offset = Integer.valueOf(params[0]);
						int limit = Integer.valueOf(params[1]);
						
						// 二级缓存: Redis -> mysql
						
						log.debug("load post list from DB.");
						return discussPostMapper.selectDiscussPosts(0, offset, limit, 1);
					}
				});
		// 初始化帖子总数缓存
		postRowsCache = Caffeine.newBuilder()
				.maximumSize(maxSize)
				.expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
				.build(new CacheLoader<Integer, Integer>() {
					@Nullable
					@Override
					public Integer load(@NonNull Integer key) throws Exception {
						log.debug("load post rows from DB.");
						return discussPostMapper.selectDiscussPostRows(key);
					}
				});
	}
	@Override
	public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode) {
		if (userId == 0 && orderMode == 1) {
			return postListCache.get(offset + ":" + limit);
		}
		
		log.debug("load post list from DB.");
		return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode);
	}
	@Override
	public int findDiscussPostRows(int userId) {
		if (userId == 0) {
			return postRowsCache.get(userId);
		}
		
		log.debug("load post rows from DB.");
		return discussPostMapper.selectDiscussPostRows(userId);
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

	@Override
	public List<DiscussPost> findDiscussPostByUserId(Integer userId, Integer offset, Integer limit) {
		List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPostByUserId(userId, offset, limit);
		return discussPosts;
	}
}
