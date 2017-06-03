package com.xl.busi.position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDAOImpl implements PositionDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map> selectBs_position(Map params) {
		return sqlSession.selectList("selectBs_position", params);
	}

	public Map selectBs_positionById(String id) {
		Map params = new HashMap();
		params.put("P_ID", id);
		return sqlSession.selectOne("selectBs_position", params);
	}

	public Map selectBs_positionByName(String c_id, String p_name) {
		Map params = new HashMap();
		params.put("C_ID", c_id);
		params.put("P_NAME", p_name);
		return sqlSession.selectOne("selectBs_position", params);
	}

	@Override
	public List<Map> selectBs_position_req(Map params) {
		return sqlSession.selectList("selectBs_position_req", params);
	}

	public Map selectBs_position_reqById(String id) {
		Map params = new HashMap();
		params.put("REQ_ID", id);
		return sqlSession.selectOne("selectBs_position_req", params);
	}
}
