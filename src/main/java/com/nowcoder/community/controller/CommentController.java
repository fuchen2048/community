package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.service.CommentService;
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
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@PostMapping("/add/{discussPostId}")
	public String addComment(@PathVariable("discussPostId")int discussPostId, Comment comment){
		if (comment == null) {
			throw new IllegalArgumentException("帖子为空");
		}
		comment.setUserId(hostHolder.getUser().getId());
		comment.setStatus(0);
		comment.setCreateTime(new Date());
		commentService.addComment(comment);
		
		return "redirect:/discuss/detail/" + discussPostId;
	}
	
	
}
