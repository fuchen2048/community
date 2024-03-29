package com.nowcoder.community;

import com.fuchen.travel.TravelApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 伏辰
 * @date 2022/7/1
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TravelApplication.class)
public class LoggerTest {
	private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
	
	@Test
	public void testLogger(){
		System.out.println(logger.getName());
		
		logger.debug("debug Log");
		logger.info("info log");
		logger.warn("warn log");
		logger.error("error log");
		
	}
}
