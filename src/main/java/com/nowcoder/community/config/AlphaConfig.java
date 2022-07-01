package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @author 伏辰
 * @date 2022/6/27
 */
@Configuration
public class AlphaConfig {
	@Bean
	public SimpleDateFormat simpleDateFormat () {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
}
