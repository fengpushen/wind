package com.xl.busi.company;

import java.util.List;
import java.util.Map;

public interface CompanyDAO {

	List<Map> selectBs_company(Map params);

	Map selectBs_companyByName(String c_name, String c_type);

	Map selectBs_companyById(String c_id);

	List<Map> selectBs_h_job(Map params);

	List<Map> selectBs_job_nojob_tj(Map params);

	List<Map> selectV_c_area_video_last(Map params);

	Map selectV_c_area_video_last(String c_area_video_id);
	
	Map selectBs_s_area_phone(String area_code);

}
