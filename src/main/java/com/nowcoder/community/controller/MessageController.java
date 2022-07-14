package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author 伏辰
 * @date 2022/7/14
 * 私信-控制层
 */
@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/letter/list")
	public String getLetterList(Model model, Page page){
		User user = hostHolder.getUser();
		//分页信息
		page.setLimit(5);
		page.setPath("/letter/list");
		page.setRows(messageService.findConversationCount(user.getId()));
		
		//查询会话列表
		List<Message> messages = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
		List<Map<String, Object>> conversations = new ArrayList<>();
		if (messages != null) {
			for (Message message : messages) {
				Map<String, Object> map = new HashMap<>();
				map.put("conversation", message);
				map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
				map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId() ));
				
				//查询私信用户的id
				int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
				map.put("target", userService.findById(targetId));
				
				conversations.add(map);
			}
			
		}
		model.addAttribute("conversations", conversations);
		
		//查询未读消息数量
		model.addAttribute("letterUnreadCount", messageService.findLetterUnreadCount(user.getId(), null));
		return "/site/letter";
	}
	
	@GetMapping("/letter/detail/{conversationId}")
	public String getLetterDetail(@PathVariable("conversationId") String conversationId, Model model, Page page){
		//分页信息
		page.setLimit(5);
		page.setPath("/letter/detail/" + conversationId);
		page.setRows(messageService.findLetterCount(conversationId));
		
		List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
		
		List<Map<String, Object>> letters = new ArrayList<>();
		if (letterList != null) {
		    for (Message message : letterList) {
			    Map<String, Object> map = new HashMap<>();
				map.put("letter", message);
				map.put("fromUser", userService.findById(message.getFromId()));
				letters.add(map);
		    }
		}
		model.addAttribute("letters", letters);
		
		//私信目标
		model.addAttribute("target", getLetterTarget(conversationId));
		
		//设置为已读
		List<Integer> ids = getLetterIds(letterList);
		if (!ids.isEmpty()) {
		    messageService.readMessage(ids);
		}
		return "/site/letter-detail";
	}
	
	/**
	 * 获取私信目标
	 * @param conversationId 私信id
	 * @return 私信目标
	 */
	private User getLetterTarget(String conversationId){
		String[] ids = conversationId.split("_");
		int id0 = Integer.parseInt(ids[0]);
		int id1 = Integer.parseInt(ids[1]);
		
		if (id0 == hostHolder.getUser().getId()) {
			return userService.findById(id1);
		} else {
			return userService.findById(id0);
		}
	}
	
	private List<Integer> getLetterIds(List<Message> letterList){
		List<Integer> ids = new ArrayList<>();
		
		if (letterList != null && letterList.size()>0) {
		    for (Message message : letterList) {
		        if(hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0){
					ids.add(message.getId());
		        }
		    }
		}
		return ids;
	}
	
	@PostMapping("/letter/send")
	@ResponseBody
	public String sendLetter(String toName, String content){
		User target = userService.findUserByName(toName);
		if (target == null) {
		    return CommunityUtil.getJsonString(1, "目标用户不存在!");
		}
		
		Message message = new Message();
		message.setFromId(hostHolder.getUser().getId());
		message.setToId(target.getId());
		if (message.getFromId() < message.getToId()) {
			message.setConversationId(message.getFromId() + "_" + message.getToId());
		} else {
			message.setConversationId(message.getToId() + "_" + message.getFromId());
		}
		message.setContent(content);
		message.setCreateTime(new Date());
		
		messageService.addMessage(message);
		
		return CommunityUtil.getJsonString(0);
	}
}
