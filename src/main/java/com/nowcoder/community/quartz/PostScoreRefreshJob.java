package com.nowcoder.community.quartz;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 伏辰
 * @date 2022/7/25
 */
@Component
public class PostScoreRefreshJob implements Job, CommunityConstant {
	
	private static final Logger log = LoggerFactory.getLogger(PostScoreRefreshJob.class);
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private DiscussPostService discussPostService;
	
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private ElasticsearchService elasticsearchService;
	
	private static final Date epoch;
	
	static {
		try {
			epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
		} catch (ParseException e) {
			throw new RuntimeException("初始化纪年失败！");
		}
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String reidsKey = RedisKeyUtil.getPostScoreKey();
		BoundSetOperations operations = redisTemplate.boundSetOps(reidsKey);
		
		if (operations.size() == 0){
			log.info("[任务取消] 没有需要刷新的拼团信息！");
			return;
		}
		log.info("[任务开始] 正在刷新拼团信息分数：" + operations.size());
		while (operations.size() > 0) {
			this.refresh((Integer) operations.pop());
		}
		log.info("[任务结束] 拼团信息分数刷新完毕！");
	}
	
	private void refresh(Integer postId){
		DiscussPost post = discussPostService.findDiscussPost(postId);
		if (post == null) {
		    log.error("该拼团信息不存在：id = " + postId);
			return;
		}
		
		//是否加精
		boolean wonderful = post.getStatus() == 1;
		//评论数量
		int commentCount = post.getCommentCount();
		//点赞数量
		long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);
		
		//计算权重
		double w = (wonderful ? 75 : 0) + commentCount + 10 + likeCount * 2;
		//分数 = 帖子权重 + 距离天数
		double score = Math.log10(Math.max(w, 1))
				+ (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
		//更新帖子分数
		discussPostService.updateScore(postId, score);
		//同步搜索数据
		post.setScore(score);
		elasticsearchService.saveDiscussPost(post);
		
	}
}
