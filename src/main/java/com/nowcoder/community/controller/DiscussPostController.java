package com.nowcoder.community.controller;

import com.nowcoder.community.entity.*;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author 伏辰
 * @date 2022/7/7
 * 发布帖子-控制层
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
	@Autowired
	private DiscussPostService discussPostService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private EventProducer eventProducer;
	
	@Autowired
	private ElasticsearchService elasticsearchService;
	
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
		
		discussPostService.addDiscussPost(discussPost);
		
		//触发帖子事件
		Event event = new Event()
				.setTopic(TOPIC_PUBLISH)
				.setUserId(user.getId())
				.setEntityType(ENTITY_TYPE_POST)
				.setEntityId(discussPost.getId());
		eventProducer.fireEvent(event);
		
		//elasticsearchService.saveDiscussPost(discussPost);
		
		//报错的情况将来统一处理
		return CommunityUtil.getJsonString(0, "发布成功！");
	}
	
	@GetMapping("/detail/{discussPostId}")
	public String getDiscussPost (@PathVariable("discussPostId") Integer discussPostId, Model model, Page page) {
		//帖子
		DiscussPost discussPost = discussPostService.findDiscussPost(discussPostId);
		model.addAttribute("post",discussPost);
		//作者
		User user = userService.findById(discussPost.getUserId());
		model.addAttribute("user",user);
		//点赞数量
		long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
		model.addAttribute("likeCount",likeCount);
		//点赞状态
		Integer likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
		model.addAttribute("likeStatus",likeStatus);
		
		//评论分页信息
		page.setLimit(5);
		page.setPath("/discuss/detail/" + discussPostId);
		page.setRows(discussPost.getCommentCount());
		
		//评论
		List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());
		List<Map<String,Object>> commentVoList = new ArrayList<>();
		if (commentList != null){
			for (Comment comment : commentList) {
				Map<String, Object> map = new HashMap<>();
				map.put("comment", comment);
				map.put("user",userService.findById(comment.getUserId()));
				//回复
				//评论数量
				likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
				map.put("likeCount",likeCount);
				//评论数量
				likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
				map.put("likeStatus",likeStatus);
				
				List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
				List<Map<String, Object>> replyListVo = new ArrayList<>();
				if(replyList != null){
					for (Comment reply : replyList) {
						Map<String, Object> replyMap = new HashMap<>();
						replyMap.put("reply", reply);
						replyMap.put("user", userService.findById(reply.getUserId()));
						
						//点赞数量
						likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
						replyMap.put("likeCount",likeCount);
						//点赞数量
						likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
						replyMap.put("likeStatus",likeStatus);
						
						User target = reply.getTargetId() == 0 ? null : userService.findById(reply.getTargetId());
						replyMap.put("target", target);
						
						replyListVo.add(replyMap);
					}
				}
				map.put("replys", replyListVo);
				
				//回复数量
				Integer replyCount = commentService.findCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
				map.put("replyCount", replyCount);
				
				commentVoList.add(map);
			}
		}
		
		model.addAttribute("comments", commentVoList);
		
		return "/site/discuss-detail";
	}
}
