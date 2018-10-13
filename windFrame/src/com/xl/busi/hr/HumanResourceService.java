package com.xl.busi.hr;

import java.io.File;
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

	ExecuteResult batchImpHrInfo(String batchId, File impFile, String oprId, String orpKind, String oprArea);

	String getBatchImpHrErrorFilePath(String batch_id);

	ExecuteResult searchHrListCom(Map<String, Object> params, String c_id);

	Map getHrInfoCom(String c_id, String hr_id) throws SQLException;

	ExecuteResult selectV_hr_position(Map params);

	ExecuteResult attentionHrs(String c_id, String[] hr_ids);

	ExecuteResult unattentionHrs(String[] attention_ids);

	ExecuteResult comHirePerson(String opr_id, String c_id, String hr_id, String hire_time, String sy_month, Map info);

	ExecuteResult turnRegularEmployees(String[] hire_ids);

	Map getHireHrInfo(String hire_id);

	ExecuteResult quitHire(String opr_id, String hire_id, String quit_time, String quit_reason);

	ExecuteResult loadHrJobTjList(String area_scope, String area_level, String area_type, String sort, String order);
}
