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
		String idcard = (String) info.get("idcard");
		String HJ_AREA = (String) info.get("hj_area");

		if (FrameTool.isEmpty(HJ_AREA)) {
			rtn.setDefaultValue("户籍地不能为空");
			return rtn;
		}
		if (FrameTool.isEmpty(idcard)) {
			rtn.setDefaultValue("身份证不能为空");
			return rtn;
		}
		idcard = idcard.toUpperCase();
		ExecuteResult rst = ToolForIdcard.idcardValidate(idcard);
		if (!rst.isSucc()) {
			return rst;
		}
		if (!BusiCommon.isInScope(opr_area, HJ_AREA)) {
			rtn.setDefaultValue("不能管理非本地区人员");
			return rtn;
		}
		if (BusiCommon.isOverRetirementAgeByIdcardYear(idcard)) {
			rtn.setDefaultValue("不能录入超过退休年龄的人员");
			return rtn;
		}
		if (BusiCommon.isTooYoungFoInputByIdcardYear(idcard)) {
			rtn.setDefaultValue("不能录入太小年龄的人员");
			return rtn;
		}
		Map idcardInfo = humanResourceDAO.selectBusi_hrByIdcard(idcard);
		if (!FrameTool.isEmpty(idcardInfo)) {
			rtn.setDefaultValue("相同的身份证号码已经存在");
			return rtn;
		}

		info.put("idcard", idcard);
		info.put("birth", ToolForIdcard.getBirthFromIdcard(idcard, FrameConstant.busi_default_date_style));
		info.put("sex", BusiCommon.getSexCodeFromIdcard(idcard));
		info.put("hr_id", hr_id);
		info.put("opr_id", orp_id);
		info.put("opr_type", opr_type);
		FrameTool.replaceMapValue(info, new String[] { "jntc" }, "，", ",");
		if (BusiCommon.isTooYoungForWorkByIdcardYear(idcard)) {
			info.put("ld_type", "0");
		}
		frameDAO.anyInsert("busi_hr", info);

		String isJob = (String) info.get("is_job");
		if (FrameConstant.busi_com_boolean_true.equals(isJob)) {
			Map<String, Object> job = (Map<String, Object>) info.get("job");
			job.put("hr_id", hr_id);
			job.put("h_job_id", FrameTool.getUUID());
			job.put("opr_id", orp_id);
			job.put("opr_type", opr_type);
			frameDAO.anyInsert("bs_h_job", job);
		}

		Object want_train_types = info.get("want_train_type");
		if (!FrameTool.isEmpty(want_train_types)) {
			rtn = saveWant_train_type(hr_id, FrameTool.getStringArray(want_train_types));
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rtn;
			}
		}

		Object cbxx = info.get("cbxx");
		if (!FrameTool.isEmpty(cbxx)) {
			rtn = saveCbxx(hr_id, FrameTool.getStringArray(cbxx));
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rtn;
			}
		}

		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult updateHrInfoStaff(String orp_id, String opr_type, String opr_area, Map<String, Object> info)
			throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		String hr_id = (String) info.get("hr_id");
		String idcard = (String) info.get("idcard");
		String hj_area = (String) info.get("hj_area");

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
					&& idcard.equals(idcardInfo.get("idcard"))) {
				rtn.setDefaultValue("相同的身份证号码已经存在");
				return rtn;
			}
			info.put("birth", ToolForIdcard.getBirthFromIdcard(idcard, FrameConstant.busi_default_date_style));
			info.put("sex", BusiCommon.getSexCodeFromIdcard(idcard));
		}
		FrameTool.replaceMapValue(info, new String[] { "jntc", "want_job_name" }, "，", ",");
		frameDAO.anyUpdateByPk("busi_hr", info, hr_id);

		String isJob = (String) info.get("is_job");
		if (FrameConstant.busi_com_boolean_true.equals(isJob)) {

			Map<String, Object> job = (Map<String, Object>) info.get("job");

			Map params = new HashMap();
			params.put("hr_id", hr_id);
			params.put("job_time", job.get("job_time"));
			frameDAO.anyDelete("bs_h_job", params);

			job.put("hr_id", hr_id);
			job.put("h_job_id", FrameTool.getUUID());
			job.put("opr_id", orp_id);
			job.put("opr_type", opr_type);
			frameDAO.anyInsert("bs_h_job", job);
		}

		Object want_train_types = info.get("want_train_type");
		if (!FrameTool.isEmpty(want_train_types)) {
			rtn = saveWant_train_type(hr_id, FrameTool.getStringArray(want_train_types));
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rtn;
			}
		}

		Object cbxx = info.get("cbxx");
		if (!FrameTool.isEmpty(cbxx)) {
			rtn = saveCbxx(hr_id, FrameTool.getStringArray(cbxx));
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rtn;
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
			// saveNeedService(hr_id, FrameTool.getStringArray(needServices));
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

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult comHirePerson(String opr_id, String c_id, String hr_id, String hire_time, String sy_month,
			Map info) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map params = new HashMap();
			params.put("c_id", c_id);
			params.put("hr_id", hr_id);
			params.put("hire_time", hire_time);
			params.put("sy_month", sy_month);
			params.put("in_c_name", info.get("JOB_DW"));
			frameDAO.anyInsert("bs_c_hire", params);
			rtn = saveJobInfo(opr_id, FrameConstant.busi_user_kind_com, info);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		} catch (Exception e) {
			log.error("", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
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
	public ExecuteResult saveWant_train_type(String hid, String[] train_types) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("hr_id", hid);
		frameDAO.anyDelete("bs_h_want_train_type", params);
		for (String train_type : train_types) {
			params.put("want_train_type_id", FrameTool.getUUID());
			params.put("train_type", train_type);
			frameDAO.anyInsert("bs_h_want_train_type", params);
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveCbxx(String hid, String[] bx_types) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("hr_id", hid);
		frameDAO.anyDelete("hr_cbxx", params);
		for (String bx_type : bx_types) {
			params.put("hr_cbxx_id", FrameTool.getUUID());
			params.put("bx_type", bx_type);
			frameDAO.anyInsert("hr_cbxx", params);
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
			frameDAO.anyDelete("HR_CBXX", params);
			frameDAO.anyDelete("BS_H_WANT_TRAIN_TYPE", params);
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

	public ExecuteResult searchHrListCom(Map<String, Object> params, String c_id) {
		// 企业查询人员信息只能查询有就业意愿的劳动力
		params.put("LD_TYPE", FrameConstant.busi_com_boolean_true);
		params.put("IS_WANT_JOB", FrameConstant.busi_com_boolean_true);
		params.put("lable_cid", c_id);
		return loadHrList(params);
	}

	public ExecuteResult loadHrList(Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			String jntc = (String) params.get("JNTC");
			if (!FrameTool.isEmpty(jntc)) {
				params.put("jntcSet", FrameTool.getSetFromString(jntc));
			}
			String want_job_name = (String) params.get("WANT_JOB_NAME");
			if (!FrameTool.isEmpty(want_job_name)) {
				params.put("wantJobNameSet", FrameTool.getSetFromString(want_job_name));
			}
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
				List<Map> wantTrainTypes = humanResourceDAO.selectBs_h_want_train_type(hr_id);
				info.put("wantTrainTypes", wantTrainTypes);

				List<Map> cbxxs = humanResourceDAO.selectHr_cbxx(hr_id);
				info.put("cbxxs", cbxxs);

				String is_job = (String) info.get("IS_JOB");
				if (FrameConstant.busi_com_boolean_true.equals(is_job)) {
					Map job = humanResourceDAO.selectV_last_h_job(hr_id);
					info.put("job", job);
				}

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

	/*
	 * 企业查看人员信息详情，需要记录查看情况
	 */
	public Map getHrInfoCom(String c_id, String hr_id) throws SQLException {
		Map params = new HashMap();
		params.put("c_id", c_id);
		params.put("hr_id", hr_id);
		frameDAO.anyInsert("BS_C_HR_VIEW_LOG", params);
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
				// String hr_id = FrameTool.getUUID();
				// Map params = new HashMap();
				// params.put("HR_ID", hr_id);
				// params.put("HR_NAME", hr_name);
				// params.put("IDCARD", idcard);
				// params.put("IS_WANT_JOB",
				// FrameConstant.busi_com_boolean_true);
				// params.put("OPR_ID", hr_id);
				// params.put("OPR_TYPE", FrameConstant.busi_user_kind_hr);
				// frameDAO.anyInsert("busi_hr", params);
				// rtn.addInfo("hr_id", hr_id);
				rtn.setDefaultValue("没有此身份证号码的信息");
				return rtn;
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
						// 先清除错误提示列的内容，以防有些用户直接用上次的错误文件直接改后导入
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

	public ExecuteResult selectV_hr_position(Map params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectV_hr_position", params);
			info.put("total", total);
			List<Map> rows = humanResourceDAO.selectV_hr_position(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadHrList", e);
		}
		return rtn;
	}

	public ExecuteResult attentionHr(String c_id, String hr_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map params = new HashMap();
			params.put("c_id", c_id);
			params.put("hr_id", hr_id);
			if (!frameDAO.isRecordExists("bs_hr_label", params)) {
				frameDAO.anyInsert("bs_hr_label", params);
			}
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("attentionHr", e);
		}
		return rtn;
	}

	public ExecuteResult unattentionHr(String attention_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			frameDAO.anyDeleteByPk("bs_hr_label", attention_id);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("unattentionHr", e);
		}
		return rtn;
	}

	public ExecuteResult unattentionHr(String c_id, String hr_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map params = new HashMap();
			params.put("c_id", c_id);
			params.put("hr_id", hr_id);
			frameDAO.anyDelete("bs_hr_label", params);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("attentionHr", e);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult attentionHrs(String c_id, String[] hr_ids) {
		ExecuteResult rtn = new ExecuteResult();

		for (String hr_id : hr_ids) {
			rtn = attentionHr(c_id, hr_id);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rtn;
			}
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult unattentionHrs(String[] attention_ids) {
		ExecuteResult rtn = new ExecuteResult();

		for (String attention_id : attention_ids) {
			rtn = unattentionHr(attention_id);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rtn;
			}
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult turnRegularEmployees(String[] hire_ids) {
		ExecuteResult rtn = new ExecuteResult();

		for (String hire_id : hire_ids) {
			rtn = turnRegularEmployee(hire_id);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rtn;
			}
		}
		rtn.setSucc(true);
		return rtn;
	}

	public ExecuteResult turnRegularEmployee(String hire_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = frameDAO.anySelectOneTableByPk("bs_c_hire", hire_id);
			if (FrameTool.isEmpty(info)) {
				rtn.setDefaultValue("没有相应的记录");
				return rtn;
			}
			if (!FrameTool.isEmpty(info.get("QUIT_TIME"))) {
				rtn.setDefaultValue("已经离职人员不能做转正操作");
				return rtn;
			}
			Map params = new HashMap();
			params.put("is_wd", FrameConstant.busi_com_boolean_true);
			frameDAO.anyUpdateByPk("bs_c_hire", params, hire_id);
			rtn.setSucc(true);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("", e);
		}
		return rtn;
	}

	public Map getHireInfo(String hire_id) {
		Map info = null;
		try {
			info = frameDAO.anySelectOneTableByPk("bs_c_hire", hire_id);
		} catch (SQLException e) {
			log.error("", e);
		}
		return info;
	}

	public Map getHireHrInfo(String hire_id) {
		Map hireInfo = getHireInfo(hire_id);
		if (!FrameTool.isEmpty(hireInfo)) {
			String hr_id = (String) hireInfo.get("HR_ID");
			if (!FrameTool.isEmpty(hireInfo)) {
				return getHrInfo(hr_id);
			}
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult quitHire(String opr_id, String hire_id, String quit_time, String quit_reason) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = frameDAO.anySelectOneTableByPk("bs_c_hire", hire_id);
			if (FrameTool.isEmpty(info)) {
				rtn.setDefaultValue("没有相应的记录");
				return rtn;
			}
			if (!FrameTool.isEmpty(info.get("QUIT_TIME"))) {
				rtn.setDefaultValue("不能重复做离职操作");
				return rtn;
			}
			Map params = new HashMap();
			params.put("quit_time", quit_time);
			params.put("quit_reason", quit_reason);
			frameDAO.anyUpdateByPk("bs_c_hire", params, hire_id);

			Map<String, Object> nojob = new HashMap<String, Object>();
			nojob.put("hr_id", info.get("HR_ID"));
			nojob.put("nojob_time", quit_time);
			nojob.put("nojob_reason", quit_reason);
			nojob.put("nojob_dw", info.get("JOB_DW"));
			rtn = saveNOjobInfo(opr_id, FrameConstant.busi_user_kind_com, nojob);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			} else {
				rtn.setSucc(true);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("", e);
		}
		return rtn;
	}

}
