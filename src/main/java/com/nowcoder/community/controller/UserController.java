package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author 伏辰
 * @date 2022/7/5
 *
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
	
	@Value("${community.path.upload}")
	private String uploadPath;
	
	@Value("${community.path.domain}")
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
	private HostHolder hostHolder;
	
	@Value("${qiniu.key.access}")
	private String accessKey;
	
	@Value("${qiniu.key.secret}")
	private String secretKey;
	
	@Value("${qiniu.bucket.header.name}")
	private String headerBucketName;
	
	@Value("${qiniu.bucket.header.url}")
	private String headerBucketUrl;
	
	/**
	 * 去setting页面
	 * @return 返回setting页面
	 */
	@LoginRequired
	@RequestMapping(path = "/setting", method = RequestMethod.GET)
	public String getSettingPage(Model model) {
		// 上传文件名称
		String fileName = CommunityUtil.generateUUID();
		// 设置响应信息
		StringMap policy = new StringMap();
		policy.put("returnBody", CommunityUtil.getJsonString(0));
		// 生成上传凭证
		Auth auth = Auth.create(accessKey, secretKey);
		String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);
		
		model.addAttribute("uploadToken", uploadToken);
		model.addAttribute("fileName", fileName);
		
		return "/site/setting";
	}
	
	/**
	 * 更新头像路径
	 * @param fileName 头像的文件名
	 * @return
	 */
	@RequestMapping(path = "/header/url", method = RequestMethod.POST)
	@ResponseBody
	public String updateHeaderUrl(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return CommunityUtil.getJsonString(1, "文件名不能为空!");
		}
		
		String url = headerBucketUrl + "/" + fileName;
		userService.updateHeader(hostHolder.getUser().getId(), url);
		
		return CommunityUtil.getJsonString(0);
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
		    model.addAttribute("error","老表，给我个图，中不中？");
			return "/site/setting";
		}
		
		String filename = headerImg.getOriginalFilename();
		String suffix = filename.substring(filename.lastIndexOf("."));
		if (StringUtils.isBlank(suffix)) {
			model.addAttribute("error","老表，你这图格式不正常啊！");
			return "/site/setting";
		}
		
		//生成随机文件命
		filename = CommunityUtil.generateUUID() + suffix;
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

}
