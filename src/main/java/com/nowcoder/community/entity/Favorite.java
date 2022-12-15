package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author 伏辰
 * @Date 2022/12/4
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Favorite {
    private Integer id;
    private Integer userId;
    private Integer scenicId;
    private Date createTime;
}
