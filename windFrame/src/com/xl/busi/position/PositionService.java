package com.xl.busi.position;

import java.sql.SQLException;
import java.util.Map;

import com.xl.frame.util.ExecuteResult;

public interface PositionService {

	ExecuteResult loadPositionList(Map<String, Object> params);

	ExecuteResult loadPostionInfo(String c_id);

	ExecuteResult saveComPositionInfo(String orp_id, String opr_type, Map<String, Object> info) throws SQLException;

	ExecuteResult pubPosition(String[] pids) throws SQLException;

	ExecuteResult unpubPosition(String[] pids) throws SQLException;

	ExecuteResult delComPosition(String[] pids) throws SQLException;

	ExecuteResult loadPositionValidList(Map<String, Object> params);

	ExecuteResult sendPositionReq(String pid, String hr_id) throws SQLException;

	ExecuteResult loadHrReqList(String hr_id);

	ExecuteResult loadReqList(Map params);

	ExecuteResult loadPostionReqInfo(String req_id);

	ExecuteResult bgnPostionReqInterview(String req_id, String host);

	ExecuteResult loadComPostionReqDetail(String req_id, String c_id, String host);

	ExecuteResult changeReqStatus(String req_id, String req_status);

	ExecuteResult changeReqsStatus(String[] req_ids, String req_status);
}
