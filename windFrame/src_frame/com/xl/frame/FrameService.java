package com.xl.frame;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.tree.TreeNode;

public interface FrameService {

	ExecuteResult selectFrame_account(Map params);

	ExecuteResult checkAccountLogin(String account_kind, String account, String password);

	ExecuteResult getAccountMenuTreeJson(String account_id);

	ExecuteResult addAccount(String opr_id, String busi_id, String account_kind, String account, String password,
			String defaultPassword, String[] roles) throws SQLException;

	ExecuteResult setAccountPassword(String account_id, String password) throws SQLException;

	ExecuteResult delAccounts(String[] account_ids) throws SQLException;

	List<Map> getAccountsRoleMgd(String account_id);

	ExecuteResult updateAccount(String opr_id, String account_id, String account, String[] roles) throws SQLException;

	boolean accountHasRoles(String account_id, String[] roles);

	Map selectFrame_accountByAccountId(String account_id);

	boolean accountHasOtherAllRole(String account_id, String other_account_id);

	ExecuteResult delAccount(String account_id) throws SQLException;

	ExecuteResult changeAccountSelfPwd(String account_id, String passwordOld, String password);

	ExecuteResult getMenuTreeJson();

	ExecuteResult getMenuGroupTreeJson();

	ExecuteResult getMenuJson(String menu_id);

	ExecuteResult loadRoleList(Map<String, Object> params);

	TreeNode getMenu(String menu_id);
}
