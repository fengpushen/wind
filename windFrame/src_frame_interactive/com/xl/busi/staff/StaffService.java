package com.xl.busi.staff;

import java.sql.SQLException;
import java.util.Map;

import com.xl.frame.util.ExecuteResult;

public interface StaffService {

	ExecuteResult loadStaffList(Map<String, Object> params);

	ExecuteResult saveStaffInfo(String orp_id, String opr_type, Map<String, Object> staffInfo,
			Map<String, Object> accountInfo) throws SQLException;

	ExecuteResult selectStaffInfo(String staff_id);

	ExecuteResult checkMgeAccount(String staff_id, String staff_id_mgd);

	ExecuteResult delStaffs(String orp_staff_id, String[] staff_ids);
}
