package com.nowcoder.community.dao.impl;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author 伏辰
 * @date 2022/6/27
 */
@Repository
@Primary
public class AlphaDaoHiberbateImpl implements AlphaDao {
	@Override
	public String select() {
		return "Hiberbate";
	}
}
