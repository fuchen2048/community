package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author 伏辰
 * @date 2022/6/29
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
	private Integer id;
	private String username;
	private String password;
	private String salt;
	private String email;
	private Integer type;
	private Integer status;
	private String activationCode;
	private String headerUrl;
	private Date createTime;
}
