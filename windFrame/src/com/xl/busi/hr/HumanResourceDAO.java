package com.xl.busi.hr;

import java.util.List;
import java.util.Map;

public interface HumanResourceDAO {

	List<Map> selectBusi_hr(Map params);

	Map selectBusi_hr(String hr_id);

	Map selectBusi_hrByIdcard(String idcard);

	List<Map> selectBs_job_nojob(Map params);

	List<Map> selectBs_job_nojob(String hr_id);

	Map selectV_jobnojob_last_list(String hr_id);

	List<Map> selectBs_h_want_train_type(String hr_id);

	void insertBs_hr_imp_pre(String batchId, List<String[]> rows);

	boolean f_hr_imp_deal(String batchId, String opr_id, String opr_type, String opr_area);

	List<Map> selectBs_hr_imp_pre(String batchId);

	List<Map> selectHr_cbxx(String hr_id);

	Map selectV_last_h_job(String hr_id);

	List<Map> selectV_hr_position(Map params);

	List<Map> selectBs_c_hire(Map params);

}
