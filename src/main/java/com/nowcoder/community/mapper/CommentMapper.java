package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/12
 * 评论-持久层
 */
@Mapper
public interface CommentMapper {
	/**
	 * 查询评论的集合
	 * @param entityType 评论的类型
	 * @param entityId 评论id
	 * @param offset 每页起始行号
	 * @param limit 每页显示条数
	 * @return 返回评论集合
	 */
	List<Comment> selectCommentByEntity(@Param("entityType") Integer entityType, @Param("entityId") Integer entityId, @Param("offset") Integer offset, @Param("limit") Integer limit);
	
	/**
	 * 查询评论总数
	 * @param entityType 评论类型
	 * @param entityId 评论id
	 * @return 返回总数
	 */
	int selectCountByEntity(@Param("entityType") Integer entityType, @Param("entityId") Integer entityId);
}
