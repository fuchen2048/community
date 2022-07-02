package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 帖子-持久层
 */
@Mapper
public interface DiscussPostMapper {
	/**
	 * 查询用户帖子
	 * @param userId 用户id
	 * @param offset 每页起始行行号
	 * @param limit 每页显示条数
	 * @return 帖子的集合
	 */
	List<DiscussPost> selectDiscussPosts(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
	
	/**
	 * 查询用户发帖条数
	 * @param userId 用户id
	 * @return 用户发帖条数
	 */
	Integer selectDiscussPostRows(@Param("userId") Integer userId);
}
