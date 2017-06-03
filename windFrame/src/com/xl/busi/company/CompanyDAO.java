package com.xl.busi.company;

import java.util.List;
import java.util.Map;

public interface CompanyDAO {

	List<Map> selectBs_company(Map params);

	Map selectBs_companyByName(String c_name, String c_type);

	Map selectBs_companyById(String c_id);

}
