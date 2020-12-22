package com.xl.busi.staff;

import java.util.List;
import java.util.Map;

public interface StaffDAO {

	List<Map> selectV_busi_staff_account(Map params);

	Map selectV_busi_staff_accountById(String staff_id);

	Map selectV_busi_staff_accountByAccountId(String account_id);

}
