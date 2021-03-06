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

	public Map selectBusi_hrByIdcard(String idcard) {
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

	public List<Map> selectBs_h_want_train_type(String hr_id) {
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		return sqlSession.selectList("selectBs_h_want_train_type", params);
	}

	public List<Map> selectHr_cbxx(String hr_id) {
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		return sqlSession.selectList("selectHr_cbxx", params);
	}

	public Map selectV_last_h_job(String hr_id) {
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		return sqlSession.selectOne("selectV_last_h_job", params);
	}

	public void insertBs_hr_imp_pre(String batchId, List<String[]> rows) {
		Map params = new HashMap();
		params.put("batchId", batchId);
		for (String[] row : rows) {
			params.put("row", row);
			sqlSession.insert("insertBs_hr_imp_pre", params);
		}
	}

	public boolean f_hr_imp_deal(String batchId, String opr_id, String opr_type, String opr_area) {
		Map params = new HashMap();
		boolean rst = false;
		try {
			params.put("batch_id", batchId);
			params.put("opr_id", opr_id);
			params.put("opr_type", opr_type);
			params.put("opr_area", opr_area);
			params.put("result", "");
			sqlSession.selectOne("f_hr_imp_deal", params);
			String rstStr = (String) params.get("result");
			if ("1".equals(rstStr)) {
				rst = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rst;
	}

	public List<Map> selectBs_hr_imp_pre(String batchId) {
		return sqlSession.selectList("selectBs_hr_imp_pre", batchId);
	}

	public List<Map> selectV_hr_position(Map params) {
		return sqlSession.selectList("selectV_hr_position", params);
	}
	
	public List<Map> selectBs_c_hire(Map params) {
		return sqlSession.selectList("selectBs_c_hire", params);
	}
}
