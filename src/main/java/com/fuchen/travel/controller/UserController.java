package com.fuchen.travel.controller;

import com.fuchen.travel.annotation.LoginRequired;
import com.fuchen.travel.entity.Comment;
import com.fuchen.travel.entity.DiscussPost;
import com.fuchen.travel.entity.Page;
import com.fuchen.travel.entity.User;
import com.fuchen.travel.service.*;
import com.fuchen.travel.util.RedisKeyUtil;
import com.fuchen.travel.util.TravelConstant;
import com.fuchen.travel.util.TravelUtil;
import com.fuchen.travel.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 伏辰
 * @date 2022/7/5
 * 用户个人信息
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController implements TravelConstant {
	
	@Value("${travel.path.upload}")
	private String uploadPath;
	
	@Value("${travel.path.domain}")
	private String domain;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private FollowService followService;

	@Autowired
	private DiscussPostService discussPostService;

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private HostHolder hostHolder;

	
	/**
	 * 去setting页面
	 * @return 返回setting页面
	 */
	@LoginRequired
	@RequestMapping(path = "/setting", method = RequestMethod.GET)
	public String getSettingPage() {
		return "/site/setting";
	}

	@PostMapping("/updatePassword")
	@ResponseBody
	public String updatePassword(String oldPassword, String newPassword) {

		User user = userService.findById(hostHolder.getUser().getId());

		if (oldPassword == null || oldPassword.isEmpty()) {
			return TravelUtil.getJsonString(2, "密码为空！！");
		}
		if (newPassword == null || newPassword.isEmpty()) {
			return TravelUtil.getJsonString(2, "新密码为空！");
		}
		System.out.println(TravelUtil.md5(oldPassword + user.getSalt()));
		if (!user.getPassword().equals(TravelUtil.md5(oldPassword + user.getSalt()))) {
			return TravelUtil.getJsonString(1, "密码错误！");
		}
		user.setPassword(TravelUtil.md5(newPassword + user.getSalt()));
		userService.updatePasswordByUserId(user);



		return TravelUtil.getJsonString(0, "修改成功，请重新登录！");
	}
	
	/**
	 * 用户上传头像
	 * @param headerImg 上传的头像
	 * @param model 视图模板
	 * @return 响应页面
	 *
	 * 废弃
	 */
	@LoginRequired
	@PostMapping("/upload")
	public String uploadHeader(MultipartFile headerImg, Model model){
		if (headerImg == null) {
		    model.addAttribute("error","请上传图片！");
			return "/site/setting";
		}
		
		String filename = headerImg.getOriginalFilename();
		String suffix = filename.substring(filename.lastIndexOf("."));
		if (StringUtils.isBlank(suffix)) {
			model.addAttribute("error","图片格式错误！");
			return "/site/setting";
		}
		
		//生成随机文件名
		filename = TravelUtil.generateUUID() + suffix;
		File dest = new File(uploadPath + "/" + filename);
		
		try {
			headerImg.transferTo(dest);
		} catch (IOException e) {
			log.error("上传文件失败" + e.getMessage());
			throw new RuntimeException("上传文件失败，服务发生异常");
		}
		
		//更新用户头像的路径(web访问路径)
		User user = hostHolder.getUser();
		String headerUrl = domain + contextPath + "/user/header/" + filename;
		userService.updateHeader(user.getId(), headerUrl);
		
		return "redirect:/index";
	}
	
	/**
	 * 获取头像
	 * @param filename 从请求路径中获取文件名
	 * @param response 用于响应图片
	 */
	@GetMapping("/header/{filename}")
	public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
		//服务器存放路径
		filename = uploadPath + "/" + filename;
		//文件后缀
		String suffix = filename.substring(filename.lastIndexOf("."));
		//响应图片
		response.setContentType("image/" + suffix);
		
		try (FileInputStream inputStream = new FileInputStream(filename);) {
			
			OutputStream outputStream = response.getOutputStream();
			
			byte[] buffer = new byte[1024];
			int b = 0;
			while ((b = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, b);
			}
		} catch (IOException e) {
			log.error("读写图像失败！" + e.getMessage());
		}
	}
	
	/**
	 * 个人主页
	 * @param userId 用户id
	 * @param model
	 * @return
	 */
	@GetMapping("/profile/{userId}")
	public String getProfilePage(@PathVariable("userId") Integer userId, Model model){
		User user = userService.findById(userId);
		if (user == null) {
		    throw new RuntimeException("用户不存在");
		}
		model.addAttribute("user", user);
		//点赞数量
		Integer userLikeCount = likeService.findUserLikeCount(userId);
		model.addAttribute("likeCount", userLikeCount);
		
		//关注数量
		Long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
		//粉丝数量
		Long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
		//是否关注
		Boolean hasFollowed = false;
		if (hostHolder.getUser() != null) {
			hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
		}
		
		model.addAttribute("followeeCount", followeeCount);
		model.addAttribute("followerCount", followerCount);
		model.addAttribute("hasFollowed", hasFollowed);
		
		return "/site/profile";
	}


	/**
	 * 我的帖子
	 * @param model
	 * @param page
	 * @return
	 */
	@GetMapping("/my-post")
	public String getMyPost(Model model, Page page){
		User user = hostHolder.getUser();

		//查询拼团帖数量
		int count = discussPostService.findDiscussPostRows(user.getId());

		//分页信息
		page.setLimit(10);
		page.setPath("/user/my-post");
		page.setRows(count);

		//查询用户发帖
		List<DiscussPost> list = discussPostService.findDiscussPostByUserId(user.getId(), page.getOffset(), page.getLimit());

		List<Map<String, Object>> myDiscussPosts = new ArrayList<>();
		if (list != null && list.size()>0) {
			for (DiscussPost post : list) {
				Map<String, Object> map = new HashMap<>();
				map.put("post", post);

				long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
				map.put("likeCount", likeCount);
				myDiscussPosts.add(map);
			}
		}
		model.addAttribute("myDiscussPosts", myDiscussPosts);
		model.addAttribute("myDiscussPostCount", count);
		return "/site/my-post";
	}

	/**
	 * 我的回复
	 * @param model
	 * @param page
	 * @return
	 */
	@GetMapping("/my-reply")
	public String getMyReply(Model model, Page page){
		User user = hostHolder.getUser();

		//查询用户评论数量
		Integer count = commentService.findCommentByUserIdCount(user.getId());

		//分页信息
		page.setLimit(10);
		page.setPath("/user/my-reply");
		page.setRows(count);

		//查询回复
		List<Comment> commentList = commentService.findCommentByUserId(user.getId(), page.getOffset(), page.getLimit());

		List<Map<String, Object>> myReplyList = new ArrayList<>();
		if (commentList != null && commentList.size()>0) {
			for (Comment comment : commentList) {
				Map<String, Object> map = new HashMap<>();
				DiscussPost post = discussPostService.findDiscussPost(comment.getEntityId());
				map.put("post",post);
				map.put("comment", comment);
				myReplyList.add(map);
			}
		}

		model.addAttribute("myReply", myReplyList);
		model.addAttribute("myReplyCount", count);

		return "/site/my-reply";
	}

}
