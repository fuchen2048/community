package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "discusspost", type = "_doc", shards = 6, replicas = 3)
public class DiscussPost {
	@Id
	private Integer id;
	@Field(type = FieldType.Integer)
	private int userId;
	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
	private String title;
	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
	private String content;
	@Field(type = FieldType.Integer)
	private int type;
	@Field(type = FieldType.Integer)
	private int status;
	@Field(type = FieldType.Date)
	private Date createTime;
	@Field(type = FieldType.Integer)
	private int commentCount;
	@Field(type = FieldType.Double)
	private Double score;
}
