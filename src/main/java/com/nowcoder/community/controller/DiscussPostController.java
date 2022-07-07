package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.NestingKind;
import java.util.Date;

/**
 * @author 伏辰
 * @date 2022/7/7
 * 发布帖子-控制层
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
	@Autowired
	private DiscussPostService discussPostService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@PostMapping("/add")
	@ResponseBody
	public String addDiscussPost(String title, String content) {
		User user = hostHolder.getUser();
		if (user == null){
			return CommunityUtil.getJsonString(403, "你还没有登录！");
		}
		DiscussPost discussPost = new DiscussPost();
		discussPost.setUserId(user.getId());
		discussPost.setTitle(title);
		discussPost.setContent(content);
		discussPost.setCreateTime(new Date());
		//自己添加
		discussPost.setType(0);
		discussPost.setStatus(0);
		
		discussPostService.addDiscussPost(discussPost);
		
		//报错的情况将来统一处理
		return CommunityUtil.getJsonString(0, "发布成功！");
	}
	
	@GetMapping("/detail/{discussPostId}")
	public String getDiscussPost (@PathVariable("discussPostId") Integer discussPostId, Model model) {
		//帖子
		DiscussPost discussPost = discussPostService.findDiscussPost(discussPostId);
		model.addAttribute("post",discussPost);
		//作者
		User user = userService.findById(discussPost.getUserId());
		model.addAttribute("user",user);
		
		return "/site/discuss-detail";
		
		
	}
	
}
