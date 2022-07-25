package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author 伏辰
 * @date 2022/7/17
 */
@Controller
public class LikeController implements CommunityConstant {
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private EventProducer eventProducer;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@PostMapping("/like")
	@ResponseBody
	public String like (Integer entityType, Integer entityId, Integer entityUserId, Integer postId) {
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
		
		//触发点赞事件
		if (status == 1) {
			Event event = new Event()
					.setTopic(TOPIC_LIKE)
					.setUserId(hostHolder.getUser().getId())
					.setEntityType(entityType)
					.setEntityId(entityId)
					.setEntityUserId(entityUserId)
					.setData("postId", postId);
			eventProducer.fireEvent(event);
		}
		
		if (entityType == ENTITY_TYPE_POST) {
			//计算帖子分数
			String redisKey = RedisKeyUtil.getPostScoreKey();
			redisTemplate.opsForSet().add(redisKey, postId);
		}
		
		return CommunityUtil.getJsonString(0, "null", map);
	}
}
