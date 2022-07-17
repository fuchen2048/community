package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author 伏辰
 * @date 2022/7/17
 */
@Controller
public class LikeController {
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@PostMapping("/like")
	@ResponseBody
	public String like (Integer entityType, Integer entityId, Integer entityUserId) {
		User user = hostHolder.getUser();
		//点赞
		likeService.like(user.getId(), entityType, entityId, entityUserId);
		//点赞数量
		long likeCount = likeService.findEntityLikeCount(entityType, entityId);
		//状态
		Integer status = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
		HashMap<String, Object> map = new HashMap<>();
		map.put("likeCount", likeCount);
		map.put("likeStatus", status);
		
		return CommunityUtil.getJsonString(0, "null", map);
	}
}
