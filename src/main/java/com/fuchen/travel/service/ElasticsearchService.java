package com.fuchen.travel.service;

import com.fuchen.travel.entity.DiscussPost;
import org.springframework.data.domain.Page;

/**
 * @author 伏辰
 * @date 2022/7/21
 */
public interface ElasticsearchService {
	
	/**
	 * 修改
	 * @param post
	 */
	void saveDiscussPost(DiscussPost post);
	
	/**
	 * 删除
	 * @param id
	 */
	void deleteDiscussPost(Integer id);
	
	/**
	 * 查询
	 * @param keyword 关键字
	 * @param current
	 * @param limit
	 * @return
	 */
	Page<DiscussPost> searchDiscussPost(String keyword, Integer current, Integer limit);
	

}
