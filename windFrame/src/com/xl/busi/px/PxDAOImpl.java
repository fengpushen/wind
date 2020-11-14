package com.xl.busi.px;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PxDAOImpl implements PxDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	public Map selectBs_jcptpx_cp(Map params) {
		return sqlSession.selectOne("selectBs_jcptpx_cp", params);
	}

	public List selectBs_jcptpx_xm(Map params) {
		return sqlSession.selectList("selectBs_jcptpx_xm", params);
	}

	public List selectBs_jcptpxcp_xm_att(Map params) {
		return sqlSession.selectList("selectBs_jcptpxcp_xm_att", params);
	}

	public void insertBx_jcptcp_xm(Map params) {

		sqlSession.insert("insertBx_jcptcp_xm", params);
	}
	
	public Map selectBs_jcptpxcp_xm(Map params) {
		return sqlSession.selectOne("selectBs_jcptpxcp_xm", params);
	}
}
