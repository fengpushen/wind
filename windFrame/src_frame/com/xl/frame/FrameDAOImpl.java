package com.xl.frame;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameDbInfo;
import com.xl.frame.util.FrameDbTable;
import com.xl.frame.util.FrameTool;

@Repository
public class FrameDAOImpl implements FrameDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<Map> anySelect(String sql) {
		return sqlSession.selectList("anySelect", sql);
	}

	public int selectRecord_count(String sqlId, Map params) {
		if (params == null) {
			params = new HashMap();
		}
		params.put("record_count", "record_count");
		HashMap rst = sqlSession.selectOne(sqlId, params);
		if (FrameTool.isEmpty(rst)) {
			return 0;
		}
		BigDecimal count = (BigDecimal) rst.get("RECORD_COUNT");
		params.remove("record_count");
		return count.intValue();
	}

	public int anyInsert(String tableName, Map<String, Object> params) throws SQLException {
		Map<String, Object> paramsCopy = getFilteColumnsParams(tableName, params);
		Map anyInsertParams = new HashMap<String, Object>();
		anyInsertParams.put("tableName", tableName);
		anyInsertParams.put("params", paramsCopy);
		return sqlSession.insert("anyInsert", anyInsertParams);
	}

	public int anyDelete(String tableName, Map<String, Object> params) throws SQLException {
		if (FrameTool.isEmpty(params)) {
			throw new SQLException("未提供参数");
		}
		Map<String, Object> paramsCopy = getFilteColumnsParams(tableName, params);
		if (FrameTool.isEmpty(paramsCopy)) {
			throw new SQLException("未提供参数");
		}
		Map anyDelParams = new HashMap<String, Object>();
		anyDelParams.put("tableName", tableName);
		anyDelParams.put("params", paramsCopy);
		return sqlSession.delete("anyDelete", anyDelParams);
	}

	public int anyDeleteByPk(String tableName, String pkValue) throws SQLException {
		if (FrameTool.isEmpty(pkValue)) {
			throw new SQLException("未提供参数");
		}
		return anyDelete(tableName, getPkParams(tableName, pkValue));
	}

	public int anyUpdate(String tableName, Map<String, Object> params, Map<String, Object> qryParams)
			throws SQLException {
		if (FrameTool.isEmpty(qryParams) || FrameTool.isEmpty(params)) {
			throw new SQLException("未提供参数");
		}
		Map<String, Object> paramsCopy = getFilteColumnsParams(tableName, params);
		Map<String, Object> qryParamsCopy = getFilteColumnsParams(tableName, qryParams);

		if (FrameTool.isEmpty(paramsCopy) || FrameTool.isEmpty(qryParamsCopy)) {
			throw new SQLException("未提供参数");
		}
		Map anyUpdateParams = new HashMap<String, Object>();
		anyUpdateParams.put("tableName", tableName);
		anyUpdateParams.put("params", paramsCopy);
		anyUpdateParams.put("qryParams", qryParamsCopy);
		return sqlSession.delete("anyUpdate", anyUpdateParams);
	}

	public int anyUpdateByPk(String tableName, Map<String, Object> params, String pkValue) throws SQLException {
		if (FrameTool.isEmpty(pkValue)) {
			throw new SQLException("未提供参数");
		}
		return anyUpdate(tableName, getColumnsParamsWithoutPk(tableName, params), getPkParams(tableName, pkValue));
	}

	private FrameDbTable getTableMetaData(String tableName) throws SQLException {
		// TODO:sqlSession.getConfiguration().getDatabaseId()这个为空，应该有别的方法区分不同的数据源
		FrameDbInfo dbInfo = FrameCache.getDbInfo(sqlSession.getConfiguration().getDatabaseId());
		FrameDbTable table = dbInfo.getTable(tableName);
		if (table == null) {
			Connection conn = null;
			ResultSet results = null;
			try {
				StringBuilder sb = new StringBuilder("select * from ").append(tableName).append(" where 1 = 2");
				conn = SqlSessionUtils.getSqlSession(sqlSession.getSqlSessionFactory(), sqlSession.getExecutorType(),
						sqlSession.getPersistenceExceptionTranslator()).getConnection();

				Statement stmt = conn.createStatement();
				results = stmt.executeQuery(sb.toString());
				ResultSetMetaData resultMetaData = results.getMetaData();
				table = new FrameDbTable(tableName);
				int max = resultMetaData.getColumnCount();
				for (int i = 1; i <= max; i++) {
					table.addColName(resultMetaData.getColumnName(i));
				}
				DatabaseMetaData metadata = conn.getMetaData();
				FrameTool.closeDbResource(results);
				// TODO:这里写死了，应该需要直接从sqlsession的配置中读取出来
				results = metadata.getPrimaryKeys(null, "YYJOB", tableName.toUpperCase());
				while (results.next()) {
					table.addPkColName(results.getString("COLUMN_NAME"));
				}
				dbInfo.putTable(tableName, table);
			} finally {
				FrameTool.closeDbResource(results);
			}
		}
		return table;
	}

	private Map<String, Object> getFilteColumnsParams(String tableName, Map<String, Object> params)
			throws SQLException {
		Map<String, Object> paramsCopy = new HashMap<String, Object>();
		paramsCopy.putAll(params);
		removeNotExistsColumns(paramsCopy, getTableMetaData(tableName));
		return paramsCopy;
	}

	private Map<String, Object> getColumnsParamsWithoutPk(String tableName, Map<String, Object> params)
			throws SQLException {
		Map<String, Object> paramsCopy = new HashMap<String, Object>();
		paramsCopy.putAll(params);
		FrameDbTable table = getTableMetaData(tableName);
		for (String name : table.getPkColNames()) {
			paramsCopy.remove(name);
		}
		return paramsCopy;
	}

	private Map<String, Object> getPkParams(String tableName, String pkValue) throws SQLException {
		Map<String, Object> params = new HashMap<String, Object>();
		FrameDbTable table = getTableMetaData(tableName);
		params.put(table.getPkColNames()[0], pkValue);
		return params;
	}

	private void removeNotExistsColumns(Map<String, Object> params, FrameDbTable table) {
		if (!FrameTool.isEmpty(params) && !FrameTool.isEmpty(table)) {
			Set<String> keys = new HashSet<String>();
			for (String key : params.keySet()) {
				if (!table.containsColName(key.toLowerCase())
						&& !table.containsColName(key.toUpperCase())) {
					keys.add(key);
				}
			}
			for (String key : keys) {
				params.remove(key);
			}
			keys.clear();
		}
	}

	@Override
	public List<Map> selectFrame_account(Map params) {
		return sqlSession.selectList("selectFrame_account", params);
	}

	@Override
	public Map selectFrame_accountByAccount(String account_kind, String account) {
		if (account != null && account.length() > 0 && account_kind != null && account_kind.length() > 0) {
			Map params = new HashMap();
			params.put("account", account);
			params.put("account_kind", account_kind);
			return sqlSession.selectOne("selectFrame_account", params);
		}
		return null;
	}

	@Override
	public Map selectFrame_accountByAccountId(String account_id) {
		if (!FrameTool.isEmpty(account_id)) {
			Map params = new HashMap();
			params.put("account_id", account_id);
			return sqlSession.selectOne("selectFrame_account", params);
		}
		return null;
	}

	@Override
	public List<Map> selectFrame_config(Map params) {
		return sqlSession.selectList("selectFrame_config", params);
	}

	public Map selectFream_configByName(String config_name) {
		if (!FrameTool.isEmpty(config_name)) {
			Map params = new HashMap();
			params.put("config_name", config_name);
			return sqlSession.selectOne("selectFrame_config", params);
		}
		return null;
	}

	@Override
	public List<Map> selectMenusOfAccount(String account_id) {
		return sqlSession.selectList("selectMenusOfAccount", account_id);
	}

	@Override
	public List<Map> selectFrame_menu_group() {
		return sqlSession.selectList("selectFrame_menu_group");
	}

	@Override
	public List<Map> selectFrame_menu() {
		return sqlSession.selectList("selectFrame_menu");
	}

	public List<Map> selectFrame_code_map(String codeName) {
		return sqlSession.selectList("selectFrame_code_map", codeName);
	}

	public List<Map> selectFrame_code() {
		return sqlSession.selectList("selectFrame_code");
	}

	public List<Map> selectView_frame_role_mgd(Map params) {
		return sqlSession.selectList("selectView_frame_role_mgd", params);
	}

}
