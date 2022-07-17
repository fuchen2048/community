package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 伏辰
 * @date 2022/7/16
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Test
	public void testStrings(){
		String redisKey = "test:count";
		redisTemplate.opsForValue().set(redisKey, 1);
		
		System.out.println(redisTemplate.opsForValue().get(redisKey));
		System.out.println(redisTemplate.opsForValue().increment(redisKey));
		System.out.println(redisTemplate.opsForValue().decrement(redisKey));
		
	}
	
	@Test
	public void testHash(){
		String redisKey = "test:hash";
		redisTemplate.opsForHash().put(redisKey, "id", "1");
		redisTemplate.opsForHash().put(redisKey, "username", "xiao");
		
		System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
		System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
		
	}
	
	@Test
	public void testList(){
		String redisKey = "test:ids";
		
		redisTemplate.opsForList().leftPush(redisKey, 101);
		redisTemplate.opsForList().leftPush(redisKey, 102);
		redisTemplate.opsForList().leftPush(redisKey, 103);
		
		System.out.println(redisTemplate.opsForList().size(redisKey));
		System.out.println(redisTemplate.opsForList().index(redisKey,0));
		System.out.println(redisTemplate.opsForList().range(redisKey,0, 2));
		System.out.println(redisTemplate.opsForList().rightPop(redisKey));
	}
	
	@Test
	public void testSets(){
		String redisKey = "test:teachers";
		
		redisTemplate.opsForSet().add(redisKey, "刘", "关", "张");
		
		System.out.println(redisTemplate.opsForSet().size(redisKey));
		System.out.println(redisTemplate.opsForSet().pop(redisKey));
		System.out.println(redisTemplate.opsForSet().members(redisKey));
		
	}
	
	@Test
	public void testSortedSets(){
		String redisKey = "test:students";
		redisTemplate.opsForZSet().add(redisKey,"唐",80);
		redisTemplate.opsForZSet().add(redisKey,"孙", 90);
		redisTemplate.opsForZSet().add(redisKey, "猪", 100);
		
		System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
		System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey,"孙"));
		System.out.println(redisTemplate.opsForZSet().score(redisKey, "猪"));
		System.out.println(redisTemplate.opsForZSet().range(redisKey, 0, 2));
	}
}
