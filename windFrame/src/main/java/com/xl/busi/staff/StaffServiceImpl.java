package com.xl.busi.staff;

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
import com.xl.frame.util.tree.TreeNode;

@Service
public class StaffServiceImpl implements StaffService {

	private static Log log = LogFactory.getLog(StaffServiceImpl.class);

	@Autowired
	private FrameDAO frameDAO;

	@Autowired
	private FrameService frameService;

	@Autowired
	private StaffDAO staffDAO;

	public ExecuteResult loadStaffList(Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectV_busi_staff_account", params);
			info.put("total", total);
			List<Map> rows = staffDAO.selectV_busi_staff_account(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadStaffList", e);
		}
		return rtn;
	}

	public ExecuteResult selectStaffInfo(String staff_id) {
		ExecuteResult rtn = new ExecuteResult();
		Map staffInfo = staffDAO.selectV_busi_staff_accountById(staff_id);
		if (!FrameTool.isEmpty(staffInfo)) {
			String account_id = (String) staffInfo.get("ACCOUNT_ID");
			Map accountInfo = frameService.selectFrame_accountByAccountId(account_id);
			if (!FrameTool.isEmpty(accountInfo)) {
				List<Map> roleMgds = frameService.getAccountsRoleMgd(account_id);
				rtn.addInfo("staffInfo", staffInfo);
				rtn.addInfo("accountInfo", accountInfo);
				rtn.addInfo("roleMgds", roleMgds);
				rtn.setSucc(true);
			}
		}

		return rtn;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveStaffInfo(String orp_id, String opr_type, Map<String, Object> staffInfo,
			Map<String, Object> accountInfo) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		String account = (String) accountInfo.get("ACCOUNT");
		String staff_id = (String) staffInfo.get("STAFF_ID");
		String[] roles = FrameTool.getStringArray(accountInfo, "ROLES");
		if (FrameTool.isEmpty(roles)) {
			rtn.setDefaultValue("权限不能为空");
			return rtn;
		}
		Map orpStaff = staffDAO.selectV_busi_staff_accountByAccountId(orp_id);
		if (FrameTool.isEmpty(orpStaff)) {
			rtn.setDefaultValue("操作账号不存在");
			return rtn;
		}
		String orpAreaCode = (String) orpStaff.get("AREA_CODE");
		String staffAreaCode = (String) staffInfo.get("AREA_CODE");
		if (!BusiCommon.isInScope(orpAreaCode, staffAreaCode)) {
			rtn.setDefaultValue("不能操作非本地区账号");
			return rtn;
		}
		if (!frameService.accountHasRoles(orp_id, roles)) {
			rtn.setDefaultValue("权限超过赋值范围");
			return rtn;
		}

		boolean isAdd = false;
		if (FrameTool.isEmpty(staff_id)) {
			isAdd = true;
			staff_id = FrameTool.getUUID();
		}
		if (isAdd) {
			staffInfo.put("STAFF_ID", staff_id);
			staffInfo.put("OPR_ID", orp_id);
			staffInfo.put("OPR_TYPE", opr_type);
			frameDAO.anyInsert("busi_staff", staffInfo);

			rtn = frameService.addAccount(orp_id, staff_id, FrameConstant.busi_user_kind_rs, account,
					(String) accountInfo.get("PASSWORD"), FrameCache.getFrameConfig("default_psd_center"), roles);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		} else {
			frameDAO.anyUpdateByPk("busi_staff", staffInfo, staff_id);
			String account_id = (String) accountInfo.get("ACCOUNT_ID");
			String password = (String) accountInfo.get("PASSWORD");
			if (!FrameTool.isEmpty(password)) {
				rtn = frameService.setAccountPassword(account_id, password);
				if (!rtn.isSucc()) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return rtn;
				}
			}
			rtn = frameService.updateAccount(orp_id, account_id, account, roles);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
		return rtn;
	}

	/**
	 * 判断staff_id对应的用户是否可以管理staff_id_mgd对应的用户，管理的规则为 1. 超级用户只能由超级用户自己管理 2.
	 * 账号不能被比其自身行政区划还要低的账户管理
	 * 
	 * @param opr_id
	 * @param staff_id
	 * @return
	 */
	public ExecuteResult checkMgeAccount(String staff_id, String staff_id_mgd) {
		ExecuteResult rtn = new ExecuteResult();
		if (FrameConstant.busi_com_super_admin_staff_id.equals(staff_id)) {
			rtn.setSucc(true);
			return rtn;
		}
		if (staff_id_mgd.equals(staff_id)) {
			rtn.setSucc(true);
			return rtn;
		}
		if (FrameConstant.busi_com_super_admin_staff_id.equals(staff_id_mgd)) {
			rtn.setDefaultValue("不能管理系统管理员账号");
			return rtn;
		}
		Map staffInfo = staffDAO.selectV_busi_staff_accountById(staff_id);
		Map staffMgdInfo = staffDAO.selectV_busi_staff_accountById(staff_id_mgd);
		if (!FrameTool.isEmpty(staffInfo) && !FrameTool.isEmpty(staffMgdInfo)) {
			String areaStaff = (String) staffInfo.get("AREA_CODE");
			String areaStaffMgd = (String) staffMgdInfo.get("AREA_CODE");
			if (areaStaff.equals(areaStaffMgd)) {
				rtn.setSucc(true);
				return rtn;
			}
			TreeNode node = FrameCache.getTree(FrameConstant.busi_com_area_tree).getNode(areaStaff);
			if (!FrameTool.isEmpty(node)) {
				if (FrameCache.getTree(FrameConstant.busi_com_area_tree).isTheUnbornNode(node.getId(), areaStaffMgd)) {
					rtn.setSucc(true);
				} else {
					rtn.setDefaultValue(staffMgdInfo.get("STAFF_NAME") + "：不能管理非本地区账号");
				}
				return rtn;
			}
		}
		return rtn;
	}

	/**
	 * 判断staff_id对应的用户是否可以删除staff_id_mgd对应的用户，
	 * 超级用户之外的判断规则为除了遵循管理的规则外staff_id还需要有staff_id_mgd用户的所有权限
	 * 
	 * @param opr_id
	 * @param staff_id
	 * @return
	 */
	public ExecuteResult checkDelAccount(String staff_id, String staff_id_mgd) {
		ExecuteResult rtn = new ExecuteResult();
		if (FrameConstant.busi_com_super_admin_staff_id.equals(staff_id)) {
			rtn.setSucc(true);
			return rtn;
		}
		if (staff_id_mgd.equals(staff_id)) {
			rtn.setDefaultValue("不能删除自身账号");
			return rtn;
		}
		rtn = checkMgeAccount(staff_id, staff_id_mgd);
		if (!rtn.isSucc()) {
			return rtn;
		}
		rtn.setSucc(false);
		Map staffInfo = staffDAO.selectV_busi_staff_accountById(staff_id);
		Map staffMgdInfo = staffDAO.selectV_busi_staff_accountById(staff_id_mgd);
		if (!FrameTool.isEmpty(staffInfo) && !FrameTool.isEmpty(staffMgdInfo)) {
			String accountStaff = (String) staffInfo.get("ACCOUNT_ID");
			String accountStaffMgd = (String) staffMgdInfo.get("ACCOUNT_ID");

			if (frameService.accountHasOtherAllRole(accountStaff, accountStaffMgd)) {
				rtn.setSucc(true);
			} else {
				rtn.setDefaultValue(staffMgdInfo.get("STAFF_NAME") + "：权限不够不能删除此账号");
			}
			return rtn;
		}
		return rtn;
	}

	public ExecuteResult checkDelAccounts(String staff_id, String[] staff_id_mgds) {
		ExecuteResult rtn = new ExecuteResult();
		rtn.setSucc(true);
		if (FrameConstant.busi_com_super_admin_staff_id.equals(staff_id)) {
			return rtn;
		}
		for (String staff_id_mgd : staff_id_mgds) {
			ExecuteResult oneCheck = checkDelAccount(staff_id, staff_id_mgd);
			if (!oneCheck.isSucc()) {
				rtn.setSucc(false);
				if (!FrameTool.isEmpty(rtn.getDefaultValue())) {
					rtn.setDefaultValue(rtn.getDefaultValue() + "、");
				}
				rtn.setDefaultValue(rtn.getDefaultValue() + oneCheck.getDefaultValue());
			}
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delStaffs(String orp_staff_id, String[] staff_ids) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			ExecuteResult chkRtn = checkDelAccounts(orp_staff_id, staff_ids);
			if (!chkRtn.isSucc()) {
				return chkRtn;
			}
			for (String id : staff_ids) {
				rtn = delStaff(orp_staff_id, id);
				if (!rtn.isSucc()) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("", e);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delStaff(String orp_staff_id, String staff_id) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		try {
			ExecuteResult chkRtn = checkDelAccount(orp_staff_id, staff_id);
			if (!chkRtn.isSucc()) {
				return chkRtn;
			}
			Map staffInfo = staffDAO.selectV_busi_staff_accountById(staff_id);
			String account_id = (String) staffInfo.get("ACCOUNT_ID");
			rtn = frameService.delAccount(account_id);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rtn;
			}
			frameDAO.anyDeleteByPk("busi_staff", staff_id);
			rtn.setSucc(true);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("", e);
		}
		return rtn;
	}

}
