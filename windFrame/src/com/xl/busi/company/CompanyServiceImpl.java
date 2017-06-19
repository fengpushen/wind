package com.xl.busi.company;

import java.sql.SQLException;
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
import com.xl.frame.FrameService;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;

@Service
public class CompanyServiceImpl implements CompanyService {

	private static Log log = LogFactory.getLog(CompanyServiceImpl.class);

	@Autowired
	private FrameDAO frameDAO;

	@Autowired
	private FrameService frameService;

	@Autowired
	private CompanyDAO companyDAO;

	public ExecuteResult loadComList(Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectBs_company", params);
			info.put("total", total);
			List<Map> rows = companyDAO.selectBs_company(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadComList", e);
		}
		return rtn;
	}

	public List<Map> selectComanyMge(String c_id_mge) {
		Map params = new HashMap();
		params.put("C_ID_MGE", c_id_mge);
		return companyDAO.selectBs_company(params);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveComInfo(String orp_id, String opr_type, Map<String, Object> info) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		String c_name = (String) info.get("C_NAME");
		String c_id = (String) info.get("C_ID");
		String c_type = (String) info.get("C_TYPE");
		boolean isAdd = false;
		if (FrameTool.isEmpty(c_id)) {
			isAdd = true;
			c_id = FrameTool.getUUID();
		}
		if ((isAdd || (!isAdd && !FrameTool.isEmpty(c_name))) && comNameExits(c_id, c_name, c_type)) {
			rtn.setDefaultValue("相同的单位名称已经存在");
		} else {
			if (isAdd) {
				info.put("C_ID", c_id);
				info.put("OPR_ID", orp_id);
				info.put("OPR_TYPE", opr_type);
				frameDAO.anyInsert("bs_company", info);
			} else {
				frameDAO.anyUpdateByPk("bs_company", info, c_id);
			}
			rtn.setSucc(true);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delComInfo(String[] cids, String oprId, String oprArea) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		for (String cid : cids) {
			frameDAO.anyDeleteByPk("bs_company", cid);
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delComInfo(String cid, String oprId, String oprArea) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();

		ExecuteResult rst = selectComAccountList(cid);
		if (rst.isSucc()) {
			List<Map> accounts = (List<Map>) ((Map) rst.getInfoOne("info")).get("rows");
			for (Map account : accounts) {
				rst = frameService.delAccount((String) account.get("ACCOUNT_ID"));
				if (!rst.isSucc()) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return rst;
				}
			}
		}
		frameDAO.anyDeleteByPk("bs_company", cid);

		rtn.setSucc(true);
		return rtn;
	}

	public ExecuteResult loadComInfo(String c_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = companyDAO.selectBs_companyById(c_id);
			if (!FrameTool.isEmpty(info)) {
				rtn.addInfo("comInfo", info);
				rtn.setSucc(true);
			}
		} catch (Exception e) {
			log.error("loadComInfo", e);
		}
		return rtn;
	}

	public ExecuteResult addComAccount(String oprId, String cid, String account, String password) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = companyDAO.selectBs_companyById(cid);
			if (FrameTool.isEmpty(info)) {
				rtn.setDefaultValue("此单位不存在");
				return rtn;
			}
			String ctype = (String) info.get("C_TYPE");
			String[] roles = FrameConstant.busi_user_kind_roles_com_lwzj;
			if (FrameConstant.busi_company_type_normal.equals(ctype)) {
				roles = FrameConstant.busi_user_kind_roles_com;
			}
			rtn = frameService.addAccount(oprId, cid, FrameConstant.busi_user_kind_com, account, password,
					FrameCache.getFrameConfig("default_psd_company"), roles);
			rtn.setSucc(true);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			rtn.setDefaultValue("程序内部错误");
			log.error("addComAccount", e);
		}
		return rtn;
	}

	public ExecuteResult selectComAccountList(String cid) {
		Map params = new HashMap();
		params.put("busi_id", cid);
		return frameService.selectFrame_account(params);
	}

	public ExecuteResult loadComRegJobList(Map params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectBs_job_nojob", params);
			info.put("total", total);
			List<Map> rows = companyDAO.selectBs_h_job(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadHrList", e);
		}
		return rtn;
	}

	private boolean comNameExits(String c_id, String c_name, String c_type) {
		Map com = companyDAO.selectBs_companyByName(c_name, c_type);
		if (FrameTool.isEmpty(com)) {
			return false;
		} else {
			String cur_c_id = (String) com.get("C_ID");
			if (cur_c_id.equals(c_id)) {
				return false;
			}
			return true;
		}
	}

}
