package com.xl.frame;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FrameSelfDAOImpl implements FrameSelfDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map> selectFrame_role(Map params) {
		return sqlSession.selectList("selectFrame_role", params);
	}
}
