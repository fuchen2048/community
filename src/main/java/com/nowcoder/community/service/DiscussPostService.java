package com.nowcoder.community.service;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 帖子-服务层
 */

public interface DiscussPostService {
	
	/**
	 * 分页查询用户帖子
	 * @param userId 用户id
	 * @param offset 每页起始行行号
	 * @param limit 每页显示条数
	 * @return 帖子的集合
	 */
	List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit, Integer orderMode);
	
	/**
	 * 查询用户帖子总数
	 * @param userId 用户id
	 * @return 返回帖子数量
	 */
	Integer findDiscussPostRows(Integer userId);
	
	/**
	 * 发布的帖子
	 * @param post 帖子对象
	 * @return 添加行数
	 */
	Integer addDiscussPost(DiscussPost post);
	
	/**
	 * 查询帖子
	 * @param id 帖子的id
	 * @return 查询的结果DiscussPost对象
	 */
	DiscussPost findDiscussPost(Integer id);
	
	/**
	 * 修改帖子数量
	 * @param id 帖子id
	 * @param commentCount 需要修改字段的值
	 * @return 修改行数
	 */
	Integer updateCommentCount(Integer id, Integer commentCount);
	
	/**
	 * 修改类型
	 * @param id 帖子id
	 * @param type 帖子类型
	 * @return
	 */
	Integer updateType(Integer id, Integer type);
	
	/**
	 * 修改状态
	 * @param id 帖子id
	 * @param status 帖子状态
	 * @return
	 */
	Integer updateStatus(Integer id, Integer status);
	
	/**
	 * 修改分数
	 * @param id 帖子id
	 * @param score 分数
	 * @return
	 */
	Integer updateScore(Integer id, Double score);
}
