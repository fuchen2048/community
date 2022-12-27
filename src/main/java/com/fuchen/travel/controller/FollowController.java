package com.fuchen.travel.controller;

import com.fuchen.travel.entity.Event;
import com.fuchen.travel.entity.Page;
import com.fuchen.travel.entity.User;
import com.fuchen.travel.event.EventProducer;
import com.fuchen.travel.service.FollowService;
import com.fuchen.travel.service.UserService;
import com.fuchen.travel.util.TravelConstant;
import com.fuchen.travel.util.TravelUtil;
import com.fuchen.travel.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author 伏辰
 * @date 2022/7/18
 */
@Controller
public class FollowController implements TravelConstant {
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private EventProducer eventProducer;
	
	/**
	 * 关注
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	@PostMapping("/follow")
	@ResponseBody
	public String follow(Integer entityType, Integer entityId){
		User user = hostHolder.getUser();
		
		followService.follow(user.getId(), entityType, entityId);
		
		//触发关注事件
		Event event = new Event()
				.setTopic(TOPIC_FOLLOW)
				.setUserId(hostHolder.getUser().getId())
				.setEntityType(entityType)
				.setEntityId(entityId)
				.setEntityUserId(entityId);
		eventProducer.fireEvent(event);
		
		return TravelUtil.getJsonString(0, "已关注");
	}
	
	/**
	 * 取消关注
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	@PostMapping("/unfollow")
	@ResponseBody
	public String unfollow(Integer entityType, Integer entityId){
		User user = hostHolder.getUser();
		
		followService.unfollow(user.getId(), entityType, entityId);
		
		return TravelUtil.getJsonString(0, "已取消关注");
	}
	
	@GetMapping("/followees/{userId}")
	public String getFollowees(@PathVariable("userId") Integer userId, Model model, Page page){
		User user = userService.findById(userId);
		
		if (user == null){
			throw new RuntimeException("用户不存在！");
		}
		
		model.addAttribute("user", user);
		
		page.setLimit(5);
		page.setPath("/followees/" + userId);
		page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));
		
		List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());
		if (userList.size()>0 && userList != null) {
		    for (Map<String, Object> map : userList) {
			    User u = (User) map.get("user");
				map.put("hasFollowed", hasFollowed(u.getId()));
		    }
		}
		model.addAttribute("users", userList);
		return "/site/followee";
	}
	
	@GetMapping("/followers/{userId}")
	public String getFollowers(@PathVariable("userId") Integer userId, Model model, Page page){
		User user = userService.findById(userId);
		
		if (user == null){
			throw new RuntimeException("用户不存在！");
		}
		
		model.addAttribute("user", user);
		
		page.setLimit(5);
		page.setPath("/followers/" + userId);
		page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));
		
		List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
		if (userList.size() > 0 && userList != null) {
			for (Map<String, Object> map : userList) {
				User u = (User) map.get("user");
				map.put("hasFollowed", hasFollowed(u.getId()));
			}
		}
		model.addAttribute("users", userList);
		return "/site/follower";
	}
	
	private boolean hasFollowed(int userId){
		if (hostHolder.getUser() == null) {
			return false;
		}
		return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
	}
}
