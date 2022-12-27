package com.fuchen.travel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author 伏辰
 * @Date 2022/11/28
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ScenicImage {
    private Integer id;
    private Integer scenicId;
    private String imageUrl;
}
