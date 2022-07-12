package com.nowcoder.community;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author 伏辰
 * @date 2022/6/29
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private DiscussPostMapper discussPostMapper;
	
	@Autowired
	private LoginTicketMapper loginTicketMapper;
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Test
	public void testSelect(){
		User user = userMapper.selectById(111);
		System.out.println(user.toString());
		
		User aaa = userMapper.selectByName("aaa");
		System.out.println(aaa.toString());
		
		User user1 = userMapper.selectByEmail("nowcoder111@sina.com");
		System.out.println(user1.toString());
	}
	
	@Test
	public void testInsertUser(){
		User user = new User();
		user.setUsername("shenmenglin");
		user.setPassword("123456789");
		user.setSalt("abc");
		user.setEmail("shenmenglin@qq.com");
		user.setHeaderUrl("https://www.nowcoder.com/101.png");
		user.setCreateTime(new Date());
		Integer rows = userMapper.insertUser(user);
		System.out.println(rows);
		System.out.println(user.getId());
	}
	
	@Test
	public void testUpdate(){
		Integer rows = userMapper.updatePassword(150, "000000");
		System.out.println(rows);
		
		rows = userMapper.updateHeader(150, "https://www.nowcoder.com/102.png");
		System.out.println(rows);
		
		rows = userMapper.updateStatus(150, 1);
		System.out.println(rows);
	}
	
	@Test
	public void testSelectPosts(){
		List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10);
		for (DiscussPost post : list) {
			System.out.println(post);
		}
		
		Integer rows = discussPostMapper.selectDiscussPostRows(0);
		System.out.println(rows);
	}
	
	@Test
	public void testInsertLoginTicket(){
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(150);
		loginTicket.setTicket("123456");
		loginTicket.setStatus(0);
		loginTicket.setExpired(new Date());
		Integer rows = loginTicketMapper.insertLoginTicket(loginTicket);
		System.out.println(rows);
	}
	
	@Test
	public void updateLoginTicket() {
		Integer rows = loginTicketMapper.updateStatus("123456", 1);
		System.out.println(rows);
	}
	
	@Test
	public void testSelectLoginTicket(){
		LoginTicket loginTicket = loginTicketMapper.selectByTicket("123456");
		System.out.println(loginTicket);
	}
	
	@Test
	public void testDiscussPost(){
		//'149', '测试', '测试',0, 0, '2019-05-20 17:41:30', 16, 1755.2095150145426)
		DiscussPost discussPost = new DiscussPost();
		discussPost.setUserId(149);
		discussPost.setTitle("再测试");
		discussPost.setContent("再测试");
		discussPost.setType(0);
		discussPost.setStatus(0);
		discussPost.setCreateTime(new Date());
		discussPost.setCommentCount(15);
		discussPost.setScore(1755.2095150145426);
		discussPostMapper.insertDiscussPost(discussPost);
	}
	
	@Test
	public void testComment(){
		List<Comment> list = commentMapper.selectCommentByEntity(1, 228, 0, 10);
		for (Comment comment : list) {
			System.out.println(comment);
		}
		
		int count = commentMapper.selectCountByEntity(1, 228);
		System.out.println(count);
	}
	
}
