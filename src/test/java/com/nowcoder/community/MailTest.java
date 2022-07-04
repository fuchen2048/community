package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author 伏辰
 * @date 2022/7/1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
	@Autowired
	private MailClient mailClient;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Test
	public void testTextMail(){
		mailClient.sendMail("3213821843@qq.com", "TEST", "掖吊吧，小虚逼！！！");
	}
	
	@Test
	public void testHTMLMail(){
		Context context = new Context();
		context.setVariable("username", "sunday");
		
		String content = templateEngine.process("/mail/demo", context);
		System.out.println(content);
		
		mailClient.sendMail("fuchen1024@qq.com", "HTML", "测试");
	}
	
}
