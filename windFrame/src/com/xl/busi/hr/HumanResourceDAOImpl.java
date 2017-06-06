package com.xl.busi.hr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HumanResourceDAOImpl implements HumanResourceDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map> selectBusi_hr(Map params) {
		return sqlSession.selectList("selectBusi_hr", params);
	}

	public Map selectBusi_hr(String hr_id) {
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		return sqlSession.selectOne("selectBusi_hr", params);
	}
	
	public Map selectBusi_hrByIdcard(String idcard){
		Map params = new HashMap();
		params.put("IDCARD", idcard);
		return sqlSession.selectOne("selectBusi_hr", params);
	}

	public List<Map> selectBs_job_nojob(Map params) {
		return sqlSession.selectList("selectBs_job_nojob", params);
	}

	public List<Map> selectBs_job_nojob(String hr_id) {
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		return sqlSession.selectList("selectBs_job_nojob", params);
	}

	public Map selectV_jobnojob_last_list(String hr_id) {
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		return sqlSession.selectOne("selectV_jobnojob_last_list", params);
	}
	
	public List<Map> selectBs_h_need_service(String hr_id) {
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		return sqlSession.selectList("selectBs_h_need_service", params);
	}
}
