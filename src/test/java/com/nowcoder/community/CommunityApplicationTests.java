package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
	
	@Autowired
	private DiscussPostService discussPostService;
	
	@Autowired
	private UserService userService;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);
		System.out.println(applicationContext.getBean(AlphaDao.class).select());
	}

	
	@Test
	public void testConfig(){
		System.out.println(applicationContext.getBean(SimpleDateFormat.class).format(new Date()));;
	}
	
	@Test
	public void fun(){
		//帖子
		DiscussPost discussPost = discussPostService.findDiscussPost(286);
		System.out.println(discussPost);
		//作者
		User user = userService.findById(discussPost.getUserId());
		System.out.println(user);
		
	}
	

	
}
