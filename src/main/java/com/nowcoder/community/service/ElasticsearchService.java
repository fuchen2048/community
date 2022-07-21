package com.nowcoder.community.service;

import com.nowcoder.community.dao.elasticesearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

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
