package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author 伏辰
 * @date 2022/7/13
 * 评论-控制层
 */
@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private DiscussPostService discussPostService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private EventProducer eventProducer;
	
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 添加评论
	 * @param discussPostId 帖子id
	 * @param comment
	 * @return
	 */
	@PostMapping("/add/{discussPostId}")
	public String addComment(@PathVariable("discussPostId")int discussPostId, Comment comment, DiscussPost discussPost){

		if (comment == null) {
			throw new IllegalArgumentException("帖子为空");
		}

		if (discussPost == null) {
			throw new IllegalArgumentException("无帖子回复数量");
		}
		User user = hostHolder.getUser();

		comment.setUserId(user.getId());
		comment.setStatus(0);
		comment.setCreateTime(new Date());
		commentService.addComment(comment);

		discussPostService.updateCommentCount(discussPostId, discussPost.getCommentCount() + 1);

		log.info(discussPost.toString());
		
		//触发评论事件
		Event event = new Event()
				.setTopic(TOPIC_COMMENT)
				.setUserId(hostHolder.getUser().getId())
				.setEntityType(comment.getEntityType())
				.setEntityId(comment.getEntityId())
				.setData("postId", discussPostId);
		
		if (comment.getEntityType() == ENTITY_TYPE_POST) {
			DiscussPost target = discussPostService.findDiscussPost(comment.getEntityId());
			event.setEntityUserId(target.getUserId());
		} else if (comment.getEntityType() == ENTITY_TYPE_COMMENT){
			Comment target = commentService.findCommentById(comment.getEntityId());
			event.setEntityUserId(target.getUserId());
		}
		//触发帖子事件
		if (comment.getEntityType() == ENTITY_TYPE_POST) {
			event = new Event()
					.setTopic(TOPIC_PUBLISH)
					.setUserId(comment.getUserId())
					.setEntityType(ENTITY_TYPE_POST)
					.setEntityId(discussPostId);
			eventProducer.fireEvent(event);
			
			String redisKey = RedisKeyUtil.getPostScoreKey();
			redisTemplate.opsForSet().add(redisKey, discussPostId);
		}
		
		return "redirect:/discuss/detail/" + discussPostId;
	}
	
	
}
