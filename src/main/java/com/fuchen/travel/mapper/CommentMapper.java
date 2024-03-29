package com.fuchen.travel.mapper;

import com.fuchen.travel.entity.Comment;
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
	 * 根据用户id查询评论集合
	 * @param userId 用户id
	 * @return
	 */
	List<Comment> selectCommentByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset,
                                        @Param("limit") Integer limit);

	/**
	 * 查询用户的评论数量
	 * @param userId
	 * @return
	 */
	Integer selectCommentByUserIdCount(@Param("userId") Integer userId);

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
	
	/**
	 * 增加帖子
	 * @param comment 需要增加的实体类对象
	 * @return 添加条数
	 */
	int insertComment(Comment comment);
	
	/**
	 * 根据id查询comment
	 * @param id
	 * @return
	 */
	Comment selectCommentById(@Param("id") int id);
}
