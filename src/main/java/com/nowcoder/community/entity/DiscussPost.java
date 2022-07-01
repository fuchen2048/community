package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author 伏辰
 * @date 2022/6/30
 * 贴子实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DiscussPost {
	private Integer id;
	private Integer userId;
	private String title;
	private String content;
	private Integer type;
	private Integer status;
	private Date createTime;
	private Integer commentCount;
	private Double score;
}
