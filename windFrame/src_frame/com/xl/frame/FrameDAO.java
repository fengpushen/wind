package com.xl.frame;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xl.frame.util.ExecuteResult;

public interface FrameDAO {

	List<Map> anySelect(String sql);

	List<Map> anySelectOneTable(String tableName, Map<String, Object> params);

	Map anySelectOneTableOneRow(String tableName, Map<String, Object> params);

	Map anySelectOneTableByPk(String tableName, String pkValue) throws SQLException;

	boolean isRecordExists(String tableName, Map<String, Object> params);

	int selectRecord_count(String sqlId, Map params);

	int anyInsert(String tableName, Map<String, Object> params) throws SQLException;

	int anyDelete(String tableName, Map<String, Object> params) throws SQLException;

	int anyDeleteByPk(String tableName, String pkValue) throws SQLException;

	int anyUpdate(String tableName, Map<String, Object> params, Map<String, Object> qryParams) throws SQLException;

	int anyUpdateByPk(String tableName, Map<String, Object> params, String pkValue) throws SQLException;

	ExecuteResult qryPaginationInfo(String sqlId, Map<String, Object> params);

	List<Map> selectList(String sqlId, Map params);

	Map selectOne(String sqlId, Map params);

	List<Map> selectFrame_account(Map params);

	Map selectFrame_accountByAccount(String account_kind, String account);

	Map selectFrame_accountByAccountId(String account_id);

	List<Map> selectFrame_config(Map params);

	Map selectFream_configByName(String config_name);

	List<Map> selectMenusOfAccount(String account_id);

	List<Map> selectFrame_menu();

	List<Map> selectFrame_code_map(String codeName);

	List<Map> selectFrame_code();

	List<Map> selectView_frame_role_mgd(Map params);
}
