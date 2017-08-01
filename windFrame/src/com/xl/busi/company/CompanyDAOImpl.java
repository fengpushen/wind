package com.xl.busi.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAOImpl implements CompanyDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map> selectBs_company(Map params) {
		return sqlSession.selectList("selectBs_company", params);
	}

	public Map selectBs_companyByName(String c_name, String c_type) {
		Map params = new HashMap();
		params.put("C_NAME", c_name);
		params.put("C_TYPE", c_type);
		return sqlSession.selectOne("selectBs_company", params);
	}

	public Map selectBs_companyById(String c_id) {
		Map params = new HashMap();
		params.put("C_ID", c_id);
		return sqlSession.selectOne("selectBs_company", params);
	}

	public List<Map> selectBs_h_job(Map params) {
		return sqlSession.selectList("selectBs_job_nojob", params);
	}

	public List<Map> selectBs_job_nojob_tj(Map params) {
		return sqlSession.selectList("selectBs_job_nojob_tj", params);
	}

	public List<Map> selectV_c_area_video_last(Map params) {
		return sqlSession.selectList("selectV_c_area_video_last", params);
	}

	public Map selectV_c_area_video_last(String c_area_video_id) {
		Map params = new HashMap();
		params.put("c_area_video_id", c_area_video_id);
		return sqlSession.selectOne("selectV_c_area_video_last", params);
	}

}
