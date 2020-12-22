package com.xl.busi.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StaffDAOImpl implements StaffDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map> selectV_busi_staff_account(Map params) {
		return sqlSession.selectList("selectV_busi_staff_account", params);
	}

	public Map selectV_busi_staff_accountById(String staff_id) {
		Map params = new HashMap();
		params.put("staff_id", staff_id);
		return sqlSession.selectOne("selectV_busi_staff_account", params);
	}

	public Map selectV_busi_staff_accountByAccountId(String account_id) {
		Map params = new HashMap();
		params.put("account_id", account_id);
		return sqlSession.selectOne("selectV_busi_staff_account", params);
	}
}
