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
	
	List<Map> selectBs_h_need_service(String hr_id);

}
