package com.xl.busi.hr;

import java.sql.SQLException;
import java.util.Map;

import com.xl.frame.util.ExecuteResult;

public interface HumanResourceService {

	ExecuteResult saveHrInfo(String orp_id, String opr_type, String opr_area, Map<String, Object> info)
			throws SQLException;

	ExecuteResult loadHrList(Map<String, Object> params);

	ExecuteResult loadHrInfo(String hr_id);

	ExecuteResult delHrInfo(String[] hids, String oprId, String oprArea) throws SQLException;

	ExecuteResult delHrJobNojobInfo(String hr_id, String[] ids, String[] is_jobs, String oprId, String oprArea)
			throws SQLException;

	Map getHrInfo(String hr_id);

	ExecuteResult loadHrJobList(String hr_id);

	ExecuteResult saveJobInfo(String orp_id, String opr_type, Map info) throws SQLException;

	ExecuteResult saveNOjobInfo(String orp_id, String opr_type, Map info) throws SQLException;

	ExecuteResult bindHr(String idcard, String hr_name);

	ExecuteResult updateHrInfo(String orp_id, String opr_type, String hr_id, Map<String, Object> info)
			throws SQLException;

	ExecuteResult updateHrInfoStaff(String orp_id, String opr_type, String opr_area, Map<String, Object> info)
			throws SQLException;
}
