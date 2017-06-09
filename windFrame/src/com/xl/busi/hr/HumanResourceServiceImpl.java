package com.xl.busi.hr;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.xl.busi.BusiCommon;
import com.xl.frame.FrameDAO;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.ToolForExcel;
import com.xl.frame.util.ToolForFile;
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
		if (BusiCommon.isTooYoungForWorkByIdcardYear(idcard)) {
			rtn.setDefaultValue("不能录入未到法定工作年龄的人员");
			return rtn;
		}
		Map idcardInfo = humanResourceDAO.selectBusi_hrByIdcard(idcard);
		if (!FrameTool.isEmpty(idcardInfo)) {
			rtn.setDefaultValue("相同的身份证号码已经存在");
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

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult updateHrInfoStaff(String orp_id, String opr_type, String opr_area, Map<String, Object> info)
			throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		String hr_id = (String) info.get("HR_ID");
		String idcard = (String) info.get("IDCARD");
		String hj_area = (String) info.get("HJ_AREA");

		if (FrameTool.isEmpty(hj_area)) {
			rtn.setDefaultValue("户籍地不能为空");
			return rtn;
		}
		Map infoOrg = humanResourceDAO.selectBusi_hr(hr_id);
		if (FrameTool.isEmpty(infoOrg)) {
			rtn.setDefaultValue("修改的数据不存在");
			return rtn;
		}
		ExecuteResult rst = ToolForIdcard.idcardValidate(idcard);
		if (!rst.isSucc()) {
			return rst;
		} else {
			idcard = idcard.toUpperCase();
		}
		boolean idcardChanged = true;
		if (idcard.equals(infoOrg.get("IDCARD"))) {
			idcardChanged = false;
		}
		String hj_area_org = (String) infoOrg.get("HJ_AREA");
		if (!BusiCommon.isInScope(opr_area, hj_area) || !BusiCommon.isInScope(opr_area, hj_area_org)) {
			rtn.setDefaultValue("不能管理非本地区人员");
			return rtn;
		}
		if (idcardChanged && BusiCommon.isOverRetirementAgeByIdcardYear(idcard)) {
			rtn.setDefaultValue("不能录入超过退休年龄的人员");
			return rtn;
		}
		if (idcardChanged) {
			Map idcardInfo = humanResourceDAO.selectBusi_hrByIdcard(idcard);
			if (!FrameTool.isEmpty(idcardInfo) && !hr_id.equals(idcardInfo.get("HR_ID"))
					&& idcard.equals(idcardInfo.get("IDCARD"))) {
				rtn.setDefaultValue("相同的身份证号码已经存在");
				return rtn;
			}
			info.put("BIRTH", ToolForIdcard.getBirthFromIdcard(idcard, FrameConstant.busi_default_date_style));
			info.put("SEX", BusiCommon.getSexCodeFromIdcard(idcard));
		}
		FrameTool.replaceMapValue(info, new String[] { "JNTC", "WANT_JOB_NAME" }, "，", ",");
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

		for (String hid : hids) {
			rtn = delHrInfoSingle(oprId, oprArea, hid);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delHrInfoSingle(String oprId, String oprArea, String hid) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map infoOrg = humanResourceDAO.selectBusi_hr(hid);
			String hj_area_org = (String) infoOrg.get("HJ_AREA");
			if (!BusiCommon.isInScope(oprArea, hj_area_org)) {
				rtn.setDefaultValue("不能管理非本地区人员");
				return rtn;
			}
			Map params = new HashMap();
			params.put("hr_id", hid);
			frameDAO.anyDelete("BS_H_JOB", params);
			frameDAO.anyDelete("BS_H_NEED_SERVICE", params);
			frameDAO.anyDelete("BS_H_NOJOB", params);
			frameDAO.anyDelete("BS_H_WANT_JOB_AREA", params);
			frameDAO.anyDeleteByPk("busi_hr", hid);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("delHrInfoSingle", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
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
				List<Map> needServices = humanResourceDAO.selectBs_h_need_service(hr_id);
				info.put("needServices", needServices);
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

	public ExecuteResult batchImpHrInfo(String batchId, File impFile, String oprId, String orpKind, String oprArea) {

		ExecuteResult rtn = new ExecuteResult();
		try {
			if (FrameTool.isEmpty(batchId) || FrameTool.isEmpty(impFile) || FrameTool.isEmpty(oprId)
					|| FrameTool.isEmpty(oprArea)) {
				rtn.setDefaultValue("错误的参数");
				return rtn;
			}
			if (!ToolForFile.isExcelFile(impFile)) {
				rtn.setDefaultValue("请用规定的模板导入");
				return rtn;
			}

			List<String[]> rows = ToolForExcel.buildListFromExcel(impFile, 4, 1, 24);
			if (FrameTool.isEmpty(rows)) {
				rtn.setDefaultValue("导入文件中没有数据");
				return rtn;
			}
			rtn.addInfo("allNum", rows.size());
			humanResourceDAO.insertBs_hr_imp_pre(batchId, rows);
			if (!humanResourceDAO.f_hr_imp_deal(batchId, oprId, orpKind, oprArea)) {
				List<Map> errors = humanResourceDAO.selectBs_hr_imp_pre(batchId);
				if (!FrameTool.isEmpty(errors)) {
					List<ToolForExcel.CellInfo> cellInfos = new ArrayList<ToolForExcel.CellInfo>();
					for (Map error : errors) {
						cellInfos.add(ToolForExcel.getNewCellInfo(Integer.parseInt((String) error.get("ROW_NO")), 25,
								(String) error.get("ERROR_INFO")));
					}
					if (!FrameTool.isEmpty(cellInfos)) {
						//先清除错误提示列的内容，以防有些用户直接用上次的错误文件直接改后导入
						ToolForExcel.cleanColumn(impFile, 4, 25);
						ToolForExcel.randomWriteExcelFile(impFile, cellInfos);
						rtn.addInfo("batchId", batchId);
						rtn.setDefaultValue(cellInfos.size() + "行数据错误");
					}
				}
			} else {
				rtn.setSucc(true);
			}

		} catch (Exception e) {
			log.error("batchImpHrInfo", e);
			rtn.setDefaultValue("系统内部错误");
		}
		return rtn;
	}

	public String getBatchImpHrErrorFilePath(String batch_id) {
		return BusiCommon.getFullPathOfTempDir() + batch_id + ".xls";
	}

}
