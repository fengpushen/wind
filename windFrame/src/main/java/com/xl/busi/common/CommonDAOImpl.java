package com.xl.busi.common;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDAOImpl implements CommonDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map> selectCom_area() {
		return sqlSession.selectList("selectCom_area");
	}

	public List<Map> selectV_com_area(Map params) {
		return sqlSession.selectList("selectV_com_area", params);
	}
}
