package com.nowcoder.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author 伏辰
 * @date 2022/7/26
 */
@Configuration
public class WkConfig {
	
	private static final Logger log = LoggerFactory.getLogger(WkConfig.class);
	
	@Value("${wk.image.storage}")
	private String WkImageStorage;

	@PostConstruct
	public void init() {
		//创建WK图片目录
		File file = new File(WkImageStorage);
		if (!file.exists()) {
			file.mkdirs();
			log.info("创建WK图片目录：" + WkImageStorage);
		}
	}
}
