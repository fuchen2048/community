package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author 伏辰
 * @date 2022/7/13
 * 评论-控制层
 */
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
	
	@PostMapping("/add/{discussPostId}")
	public String addComment(@PathVariable("discussPostId")int discussPostId, Comment comment){
		if (comment == null) {
			throw new IllegalArgumentException("帖子为空");
		}
		comment.setUserId(hostHolder.getUser().getId());
		comment.setStatus(0);
		comment.setCreateTime(new Date());
		commentService.addComment(comment);
		
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
		eventProducer.fireEvent(event);
		
		return "redirect:/discuss/detail/" + discussPostId;
	}
	
	
}
