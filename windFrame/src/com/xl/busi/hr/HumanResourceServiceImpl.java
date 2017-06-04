package com.xl.busi.hr;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xl.busi.BusiCommon;
import com.xl.frame.FrameDAO;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.ToolForIdcard;

@Service
public class HumanResourceServiceImpl implements HumanResourceService {

	private static Log log = LogFactory.getLog(HumanResourceServiceImpl.class);

	@Autowired
	private FrameDAO frameDAO;

	@Autowired
	private HumanResourceDAO humanResourceDAO;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveHrInfo(String orp_id, String opr_type, String opr_area, Map<String, Object> info)
			throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		String hr_id = FrameTool.getUUID();
		String idcard = (String) info.get("IDCARD");
		String HJ_AREA = (String) info.get("HJ_AREA");

		if (FrameTool.isEmpty(HJ_AREA)) {
			rtn.setDefaultValue("户籍地不能为空");
			return rtn;
		}
		ExecuteResult rst = ToolForIdcard.idcardValidate(idcard);
		if (!rst.isSucc()) {
			return rst;
		} else {
			idcard = idcard.toUpperCase();
		}
		if (!BusiCommon.isInScope(opr_area, HJ_AREA)) {
			rtn.setDefaultValue("不能管理非本地区人员");
			return rtn;
		}
		if (BusiCommon.isOverRetirementAgeByIdcardYear(idcard)) {
			rtn.setDefaultValue("不能录入超过退休年龄的人员");
			return rtn;
		}

		info.put("IDCARD", idcard);
		info.put("BIRTH", ToolForIdcard.getBirthFromIdcard(idcard, FrameConstant.busi_default_date_style));
		info.put("SEX", BusiCommon.getSexCodeFromIdcard(idcard));
		info.put("HR_ID", hr_id);
		info.put("OPR_ID", orp_id);
		info.put("OPR_TYPE", opr_type);
		FrameTool.replaceMapValue(info, new String[] { "JNTC" }, "，", ",");
		frameDAO.anyInsert("busi_hr", info);

		String isJob = (String) info.get("IS_JOB");
		if (FrameConstant.busi_com_boolean_true.equals(isJob)) {
			Map<String, Object> job = (Map<String, Object>) info.get("job");
			job.put("HR_ID", hr_id);
			job.put("H_JOB_ID", FrameTool.getUUID());
			job.put("OPR_ID", orp_id);
			job.put("OPR_TYPE", opr_type);
			frameDAO.anyInsert("bs_h_job", job);
		}

		String IS_WANT_JOB = (String) info.get("IS_WANT_JOB");
		if (FrameConstant.busi_com_boolean_true.equals(IS_WANT_JOB)) {
			Map<String, Object> want_job = (Map<String, Object>) info.get("want_job");
			FrameTool.replaceMapValue(want_job, new String[] { "WANT_JOB_NAME" }, "，", ",");
			String area = (String) want_job.get("WANT_JOB_AREA");
			if (!FrameTool.isEmpty(area)) {
				saveWantJobArea(hr_id, new String[] { area });
			}
			Object needServices = want_job.get("service_codes");
			if (!FrameTool.isEmpty(needServices)) {
				saveNeedService(hr_id, FrameTool.getStringArray(needServices));
			}
			String want_income = (String) want_job.get("WANT_INCOME");
			String want_job_name = (String) want_job.get("WANT_JOB_NAME");
			Map<String, Object> params = new HashMap<String, Object>();
			if (!FrameTool.isEmpty(want_income)) {
				params.put("WANT_INCOME", want_income);
			}
			if (!FrameTool.isEmpty(want_job_name)) {
				params.put("WANT_JOB_NAME", want_job_name);
			}
			if (!FrameTool.isEmpty(params)) {
				frameDAO.anyUpdateByPk("busi_hr", params, hr_id);
			}
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult updateHrInfo(String orp_id, String opr_type, String hr_id, Map<String, Object> info)
			throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		String idcard = (String) info.get("IDCARD");
		if (!FrameTool.isEmpty(idcard)) {
			info.put("IDCARD", idcard.toUpperCase());
		}
		info.put("HR_ID", hr_id);
		frameDAO.anyUpdateByPk("busi_hr", info, hr_id);

		Map<String, Object> want_job = (Map<String, Object>) info.get("want_job");
		String area = (String) want_job.get("WANT_JOB_AREA");
		if (!FrameTool.isEmpty(area)) {
			saveWantJobArea(hr_id, new String[] { area });
		}
		Object needServices = want_job.get("service_codes");
		if (!FrameTool.isEmpty(needServices)) {
			saveNeedService(hr_id, FrameTool.getStringArray(needServices));
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveJobInfo(String orp_id, String opr_type, Map info) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map<String, Object> job = (Map<String, Object>) info.get("job");
		job.put("H_JOB_ID", FrameTool.getUUID());
		job.put("OPR_ID", orp_id);
		job.put("OPR_TYPE", opr_type);
		frameDAO.anyInsert("bs_h_job", job);
		String hr_id = (String) job.get("HR_ID");
		refreshHrJobStatus(hr_id);
		rtn.setSucc(true);
		return rtn;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveNOjobInfo(String orp_id, String opr_type, Map info) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map<String, Object> nojob = (Map<String, Object>) info.get("nojob");
		nojob.put("H_NOJOB_ID", FrameTool.getUUID());
		nojob.put("OPR_ID", orp_id);
		nojob.put("OPR_TYPE", opr_type);
		frameDAO.anyInsert("bs_h_nojob", nojob);
		String hr_id = (String) nojob.get("HR_ID");
		refreshHrJobStatus(hr_id);
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveWantJobArea(String hid, String[] areas) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("hr_id", hid);
		frameDAO.anyDelete("bs_h_want_job_area", params);
		for (String area : areas) {
			params.put("want_job_area_id", FrameTool.getUUID());
			params.put("want_job_area", area);
			frameDAO.anyInsert("bs_h_want_job_area", params);
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveNeedService(String hid, String[] services) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("hr_id", hid);
		frameDAO.anyDelete("bs_h_need_service", params);
		for (String service : services) {
			params.put("need_service_id", FrameTool.getUUID());
			params.put("service_code", service);
			frameDAO.anyInsert("bs_h_need_service", params);
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delHrInfo(String[] hids, String oprId, String oprArea) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("is_deled", FrameConstant.busi_com_boolean_true);
		for (String hid : hids) {
			frameDAO.anyUpdateByPk("busi_hr", params, hid);
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delHrJobNojobInfo(String hr_id, String[] ids, String[] is_jobs, String oprId, String oprArea)
			throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		for (int i = 0, len = ids.length; i < len; i++) {
			if (FrameConstant.busi_com_boolean_true.equals(is_jobs[i])) {
				frameDAO.anyDeleteByPk("bs_h_job", ids[i]);
			} else {
				frameDAO.anyDeleteByPk("bs_h_nojob", ids[i]);
			}
		}
		refreshHrJobStatus(hr_id);
		rtn.setSucc(true);
		return rtn;
	}

	public ExecuteResult loadHrList(Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectBusi_hr", params);
			info.put("total", total);
			List<Map> rows = humanResourceDAO.selectBusi_hr(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadHrList", e);
		}
		return rtn;
	}

	public ExecuteResult loadHrInfo(String hr_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = humanResourceDAO.selectBusi_hr(hr_id);
			if (!FrameTool.isEmpty(info)) {
				rtn.addInfo("hrInfo", info);
				rtn.setSucc(true);
			}
		} catch (Exception e) {
			log.error("loadHrInfo", e);
		}
		return rtn;
	}

	public ExecuteResult loadHrJobList(String hr_id) {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectBs_job_nojob", params);
			info.put("total", total);
			List<Map> rows = humanResourceDAO.selectBs_job_nojob(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadHrList", e);
		}
		return rtn;
	}

	public Map getHrInfo(String hr_id) {
		return humanResourceDAO.selectBusi_hr(hr_id);
	}

	public void refreshHrJobStatus(String hr_id) throws SQLException {
		Map jobInfo = humanResourceDAO.selectV_jobnojob_last_list(hr_id);
		if (!FrameTool.isEmpty(jobInfo)) {
			String is_job = (String) jobInfo.get("IS_JOB");
			Map params = new HashMap();
			params.put("is_job", is_job);
			frameDAO.anyUpdateByPk("busi_hr", params, hr_id);
		}
	}

	public ExecuteResult bindHr(String idcard, String hr_name) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			idcard = idcard.toUpperCase();
			Map info = humanResourceDAO.selectBusi_hrByIdcard(idcard);
			if (FrameTool.isEmpty(info)) {
				String hr_id = FrameTool.getUUID();
				Map params = new HashMap();
				params.put("HR_ID", hr_id);
				params.put("HR_NAME", hr_name);
				params.put("IDCARD", idcard);
				params.put("IS_WANT_JOB", FrameConstant.busi_com_boolean_true);
				params.put("OPR_ID", hr_id);
				params.put("OPR_TYPE", FrameConstant.busi_user_kind_hr);
				frameDAO.anyInsert("busi_hr", params);
				rtn.addInfo("hr_id", hr_id);
			} else {
				String hr_name_cur = (String) info.get("HR_NAME");
				if (!hr_name_cur.equals(hr_name)) {
					rtn.setDefaultValue("姓名与身份证不符，请确定后再操作");
					return rtn;
				}
				rtn.addInfo("hr_id", info.get("HR_ID"));
			}
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("bindHr", e);
		}
		return rtn;
	}

}
