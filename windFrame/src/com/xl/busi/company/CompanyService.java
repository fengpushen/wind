package com.xl.busi.company;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xl.frame.util.ExecuteResult;

public interface CompanyService {

	ExecuteResult loadComList(Map<String, Object> params);

	ExecuteResult saveComInfo(String orp_id, String opr_type, Map<String, Object> info) throws SQLException;

	ExecuteResult delComInfo(String[] cids, String oprId, String oprArea) throws SQLException;

	ExecuteResult loadComInfo(String c_id);

	ExecuteResult selectComAccountList(String cid);

	ExecuteResult addComAccount(String oprId, String cid, String account, String password);

	List<Map> selectComanyMge(String c_id_mge);

	ExecuteResult loadComRegJobList(Map params);

	ExecuteResult loadComRegJobCountList(Map params);

	ExecuteResult loadAreaList(Map<String, Object> params);
}
