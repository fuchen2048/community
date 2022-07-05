package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class UserController {
	
	@Value("${community.path.upload}")
	private String uploadPath;
	
	@Value("${community.path.domain}")
	private String domain;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@GetMapping("/setting")
	public String getSettingPage(){
		return "/site/setting";
	}
	
	/**
	 * 用户上传头像
	 * @param headerImg 上传的头像
	 * @param model 视图模板
	 * @return 响应页面
	 */
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

}
