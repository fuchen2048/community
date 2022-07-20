package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;

import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/12
 * 显示评论-服务层
 */

public interface CommentService {
	
	/**
	 * 查询评论的集合
	 * @param entityType 评论的类型
	 * @param entityId 评论id
	 * @param offset 每页起始行号
	 * @param limit 每页显示条数
	 * @return 返回评论集合
	 */
	List<Comment> findCommentsByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit);
	
	/**
	 * 查询评论总数
	 * @param entityType 评论类型
	 * @param entityId 评论id
	 * @return 返回总数
	 */
	Integer findCountByEntity(Integer entityType, Integer entityId);
	
	/**
	 * 添加帖子
	 * @param comment 需要添加帖子的实体
	 * @return 添加的行数
	 */
	Integer addComment(Comment comment);
	
	/**
	 * 根据id查询comment
	 * @param id
	 * @return
	 */
	Comment findCommentById(Integer id);

}
