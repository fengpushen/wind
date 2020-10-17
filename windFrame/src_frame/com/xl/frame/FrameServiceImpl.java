package com.xl.frame;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.bidimap.DualTreeBidiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameCode;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.tree.BaseTree;
import com.xl.frame.util.tree.TreeEasyUIJsonMaker;
import com.xl.frame.util.tree.TreeNode;
import com.xl.frame.util.tree.TreeNodeFilterHasSon;
import com.xl.frame.util.tree.TreeView;
import com.xl.util.Tree;

@Service
public class FrameServiceImpl implements FrameService {

	private static Log log = LogFactory.getLog(FrameServiceImpl.class);

	@Autowired
	private FrameDAO frameDAO;

	@Autowired
	private FrameSelfDAO frameSelfDAO;

	@PostConstruct
	public void initFrame() {
		// 初始化系统配置
		refreshConfig();
		// 初始化菜单树
		initMenuBaseTree();
		// 初始化系统码表
		initFrameCode();
	}

	public ExecuteResult selectFrame_account(Map params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectFrame_account", params);
			info.put("total", total);
			List<Map> rows = frameDAO.selectFrame_account(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadComList", e);
		}
		return rtn;
	}

	public ExecuteResult loadRoleList(Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {

			Map info = new HashMap();
			List<Map> rows = frameSelfDAO.selectFrame_role(null);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadRoleList", e);
		}
		return rtn;
	}

	@Override
	public ExecuteResult checkAccountLogin(String account_kind, String account, String password) {
		ExecuteResult rst = new ExecuteResult();
		try {
			Map accountInfo = frameDAO.selectFrame_accountByAccount(account_kind, account);
			if (accountInfo == null || accountInfo.size() == 0) {
				rst.setDefaultValue("账户或密码错误");
			} else if (!accountInfo.get("PASSWORD").equals(FrameTool.getMd5(password))) {
				rst.setDefaultValue("账户或密码错误");
			} else {
				rst.addInfo("accountInfo", accountInfo);
				rst.setSucc(true);
			}
		} catch (Exception e) {
			rst.setDefaultValue("未知错误");
			log.error("", e);
		}
		return rst;
	}

	@Override
	public ExecuteResult getAccountMenuTreeJson(String account_id) {
		ExecuteResult rst = new ExecuteResult();
		try {
			List<Map> menus = frameDAO.selectMenusOfAccount(account_id);
			List<String> ids = new ArrayList<String>();
			for (Map menu : menus) {
				ids.add((String) menu.get("MENU_ID"));
			}
			TreeView menuTree = ((BaseTree) FrameCache.getTree(FrameConstant.frame_menu_base_tree));
			rst.addInfo("menuTree", menuTree);
			rst.setDefaultValue(menuTree.getJson(ids));
			rst.setSucc(true);
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

	@Override
	public ExecuteResult getMenuTreeJson() {
		ExecuteResult rst = new ExecuteResult();
		try {
			TreeView menuTree = FrameCache.getTree(FrameConstant.frame_menu_base_tree);
			rst.setDefaultValue(menuTree.getJson());
			rst.setSucc(true);
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

	public ExecuteResult getMenuGroupTreeJson() {
		ExecuteResult rst = new ExecuteResult();
		try {
			TreeView menuTree = FrameCache.getTree(FrameConstant.frame_menu_base_tree);
			// TODO:直接new了一个对象，看是否可以改造为某种获取方式
			rst.setDefaultValue(menuTree.getJson(new TreeNodeFilterHasSon()));
			rst.setSucc(true);
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

	public ExecuteResult getMenuJson(String menu_id) {
		ExecuteResult rst = new ExecuteResult();
		try {
			TreeView menuTree = FrameCache.getTree(FrameConstant.frame_menu_base_tree);
			TreeNode node = menuTree.getNode(menu_id);
			if (!FrameTool.isEmpty(node)) {
				rst.addInfo("menu", node);
				String pid = node.getPid();
				if (!FrameTool.isEmpty(pid)) {
					TreeNode pnode = menuTree.getNode(pid);
					if (!FrameTool.isEmpty(pnode)) {
						rst.addInfo("pmenu", pnode);
					}
				}
				Map params = new HashMap();
				params.put("menu_id", menu_id);
				List<Map> ownTheMenuRoles = frameSelfDAO.selectFrame_role(params);
				rst.addInfo("ownTheMenuRoles", ownTheMenuRoles);
				rst.setSucc(true);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

	public TreeNode getMenu(String menu_id) {
		TreeNode rst = null;
		try {
			TreeView menuTree = FrameCache.getTree(FrameConstant.frame_menu_base_tree);
			rst = menuTree.getNode(menu_id);
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

	private void initMenuBaseTree() {
		BaseTree tree = new BaseTree(false, TreeEasyUIJsonMaker.getMaker());
		Tree t = new Tree();
		List<Map> menus = frameDAO.selectFrame_menu();
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for (Map menu : menus) {
			TreeNode node = new TreeNode((String) menu.get("MENU_ID"), (String) menu.get("MENU_NAME"),
					(String) menu.get("MENU_P_ID"));
			node.addNodeInfo("menu_url", menu.get("MENU_URL"));
			node.addNodeInfo("is_leaf", menu.get("IS_LEAF"));
			node.addNodeInfo("menu_memo", menu.get("MENU_MEMO"));
			node.setOrderNo((String) menu.get("MENU_ORDER"));
			nodes.add(node);

			com.xl.util.TreeNode treeNode = new com.xl.util.TreeNode((String) menu.get("MENU_ID"),
					(String) menu.get("MENU_NAME"), (String) menu.get("MENU_P_ID"));
			treeNode.addNodeInfo("menu_url", menu.get("MENU_URL"));
			treeNode.addNodeInfo("is_leaf", menu.get("IS_LEAF"));
			treeNode.addNodeInfo("menu_memo", menu.get("MENU_MEMO"));
			t.addTreeNode(treeNode);

		}
		com.xl.util.TreeNode treeNode = new com.xl.util.TreeNode("menu0016", "系统用户管理", "10001");
		treeNode.addNodeInfo("menu_url", "sys/users");
		t.addTreeNode(treeNode);
		tree.initTree(nodes);
		FrameCache.addTree(FrameConstant.frame_menu_base_tree, tree);
		FrameCache.setMenuTree(t);
	}

	private void initFrameCode() {
		List<Map> codes = frameDAO.selectFrame_code();
		if (codes != null) {
			for (Map map : codes) {
				String codeName = (String) map.get("CODE_NAME");
				String codeSql = (String) map.get("CODE_SQL");
				if (codeName != null && codeName.length() > 0) {
					refreshCode(codeName, codeSql);
				}
			}
		}
	}

	public void refreshConfig() {
		List<Map> configs = frameDAO.selectFrame_config(null);
		if (!FrameTool.isEmpty(configs)) {
			for (Map config : configs) {
				FrameCache.addConfig((String) config.get("CONFIG_NAME"), (String) config.get("CONFIG_VALUE"));
			}
		}
	}

	public void refreshCode(String codeName, String codeSql) {
		List<Map> codeDetail = null;
		if (codeSql != null && codeSql.length() > 0) {
			codeDetail = frameDAO.anySelect(codeSql);
		} else {
			codeDetail = frameDAO.selectFrame_code_map(codeName);
		}
		if (codeDetail != null && codeDetail.size() > 0) {
			DualTreeBidiMap<String, String> code = new DualTreeBidiMap<String, String>();
			for (Map map : codeDetail) {
				String key = (String) map.get("CODE_KEY");
				String value = (String) map.get("CODE_VALUE");
				if (key != null && key.length() > 0 && value != null && value.length() > 0) {
					code.put(key, value);
				}
			}
			if (code.size() > 0) {
				FrameCode.addCode(codeName, code);
			}
		}
	}

	public Map selectFrame_accountByAccountId(String account_id) {
		return frameDAO.selectFrame_accountByAccountId(account_id);
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult addAccount(String opr_id, String busi_id, String account_kind, String account, String password,
			String defaultPassword, String[] roles) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("account_kind", account_kind);
		params.put("account", account);
		Map accountInfo = frameDAO.selectFrame_accountByAccount(account_kind, account);
		if (!FrameTool.isEmpty(accountInfo)) {
			rtn.setDefaultValue("相同的账号名称已经存在");
		} else {

			params.put("busi_id", busi_id);
			String account_id = FrameTool.getUUID();
			params.put("account_id", account_id);
			if (FrameTool.isEmpty(password)) {
				params.put("password", FrameTool.getMd5(defaultPassword));
			} else {
				params.put("password", FrameTool.getMd5(password));
			}
			params.put("opr_id", opr_id);
			frameDAO.anyInsert("frame_account", params);
			addAccountRoles(account_id, roles);
			rtn.setSucc(true);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult updateAccount(String opr_id, String account_id, String account, String[] roles)
			throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map accountInfo = frameDAO.selectFrame_accountByAccountId(account_id);
		if (FrameTool.isEmpty(accountInfo)) {
			rtn.setDefaultValue("要修改的账号不存在");
		} else {
			Map account2 = frameDAO.selectFrame_accountByAccount((String) accountInfo.get("ACCOUNT_KIND"), account);
			if (!FrameTool.isEmpty(account2) && !account_id.equals(account2.get("ACCOUNT_ID"))) {
				rtn.setDefaultValue("相同的账号名称已经存在");
			} else {

				Map params = new HashMap();
				String oldAccount = (String) accountInfo.get("ACCOUNT");
				// 需要修改账号
				if (!oldAccount.equals(account)) {
					params.put("account", account);
					frameDAO.anyUpdateByPk("frame_account", params, account_id);
				}

				// 重置账号角色信息
				params.clear();
				params.put("account_id", account_id);
				frameDAO.anyDelete("frame_account_role", params);
				addAccountRoles(account_id, roles);
				rtn.setSucc(true);
			}
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	private void addAccountRoles(String account_id, String[] roles) throws SQLException {
		if (!FrameTool.isEmpty(roles)) {
			Map roleParams = new HashMap();
			roleParams.put("account_id", account_id);
			for (String role : roles) {
				roleParams.put("role_id", role);
				roleParams.put("account_role_id", FrameTool.getUUID());
				frameDAO.anyInsert("frame_account_role", roleParams);
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult setAccountPassword(String account_id, String password) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		if (FrameTool.isEmpty(account_id) || FrameTool.isEmpty(password)) {
			rtn.setDefaultValue("错误的参数");
		} else {
			Map params = new HashMap();
			params.put("password", FrameTool.getMd5(password));
			frameDAO.anyUpdateByPk("frame_account", params, account_id);
			rtn.setSucc(true);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delAccounts(String[] account_ids) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		for (String id : account_ids) {
			rtn = delAccount(id);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delAccount(String account_id) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map params = new HashMap();
			params.put("account_id", account_id);
			frameDAO.anyDelete("frame_account_role", params);
			frameDAO.anyDeleteByPk("frame_account", account_id);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("error", e);
			rtn.setDefaultValue("程序内部错误");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rtn;
	}

	public List<Map> getAccountsRoleMgd(String account_id) {
		Map params = new HashMap();
		// 超级管理员可以管理所有可分配权限，普通用户只能管理自己拥有的可分配权限
		if (!FrameConstant.busi_com_super_admin_account_id.equals(account_id)) {
			params.put("account_id", account_id);
		}
		return frameDAO.selectView_frame_role_mgd(params);
	}

	public boolean accountHasRoles(String account_id, String[] roles) {

		if (FrameTool.isEmpty(account_id)) {
			return false;
		}
		if (FrameTool.isEmpty(roles)) {
			return true;
		}
		boolean hasRoles = true;
		List<Map> roleMgd = getAccountsRoleMgd(account_id);
		if (FrameTool.isEmpty(roleMgd)) {
			hasRoles = false;
		} else {
			List roleIdMgd = FrameTool.getColumnList(roleMgd, "ROLE_ID");
			for (String role : roles) {
				if (!roleIdMgd.contains(role)) {
					hasRoles = false;
					break;
				}
			}
		}
		return hasRoles;
	}

	/**
	 * 判断一个账号是否有另一个账号的所有角色
	 * 
	 * @param account_id
	 * @param other_account_id
	 * @return
	 */
	public boolean accountHasOtherAllRole(String account_id, String other_account_id) {
		if (FrameTool.isEmpty(account_id) || FrameTool.isEmpty(other_account_id)) {
			return false;
		}
		List<Map> roleMgd = getAccountsRoleMgd(account_id);
		if (FrameTool.isEmpty(roleMgd)) {
			return false;
		}
		List<Map> roleMgdOther = getAccountsRoleMgd(other_account_id);
		if (FrameTool.isEmpty(roleMgdOther)) {
			return true;
		}
		List roleIdMgd = FrameTool.getColumnList(roleMgd, "ROLE_ID");
		List roleIdMgdOther = FrameTool.getColumnList(roleMgdOther, "ROLE_ID");
		boolean hasAll = true;
		for (Object o : roleIdMgdOther) {
			if (!roleIdMgd.contains(o)) {
				hasAll = false;
				break;
			}
		}
		return hasAll;
	}

	@Override
	public ExecuteResult changeAccountSelfPwd(String account_id, String passwordOld, String password) {
		ExecuteResult rst = new ExecuteResult();
		try {
			Map accountInfo = frameDAO.selectFrame_accountByAccountId(account_id);
			if (FrameTool.isEmpty(accountInfo)) {
				rst.setDefaultValue("错误的账号");
				return rst;
			}
			rst = checkAccountLogin((String) accountInfo.get("ACCOUNT_KIND"), (String) accountInfo.get("ACCOUNT"),
					passwordOld);
			if (!rst.isSucc()) {
				return rst;
			}
			rst.setSucc(false);

			rst = setAccountPassword(account_id, password);
		} catch (Exception e) {
			rst.setDefaultValue("未知错误");
			log.error("", e);
		}
		return rst;
	}

}
