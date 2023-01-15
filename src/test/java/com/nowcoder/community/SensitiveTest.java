package com.nowcoder.community;

import com.fuchen.travel.TravelApplication;
import com.fuchen.travel.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 伏辰
 * @date 2022/7/6
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TravelApplication.class)
public class SensitiveTest {

	@Autowired
	private SensitiveFilter sensitiveFilter;
	
	@Test
	public void testSensitive(){
		String text = "这里可以赌博，可以开票！！";
		String result = sensitiveFilter.filter(text);
		System.out.println(result);
		
		text = "这里可以！※赌※博y※，可以※f开※※票※&！！";
		result = sensitiveFilter.filter(text);
		System.out.println(result);
	}
}
