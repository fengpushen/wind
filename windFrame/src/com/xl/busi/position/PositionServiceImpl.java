package com.xl.busi.position;

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
import com.xl.busi.company.CompanyDAO;
import com.xl.busi.hr.HumanResourceDAO;
import com.xl.frame.FrameDAO;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;

@Service
public class PositionServiceImpl implements PositionService {

	private static Log log = LogFactory.getLog(PositionServiceImpl.class);

	@Autowired
	private FrameDAO frameDAO;

	@Autowired
	private PositionDAO positionDAO;

	@Autowired
	private HumanResourceDAO humanResourceDAO;

	@Autowired
	private CompanyDAO companyDAO;

	public ExecuteResult loadPositionList(Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectBs_position", params);
			info.put("total", total);
			List<Map> rows = positionDAO.selectBs_position(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadPositionList", e);
		}
		return rtn;
	}

	public ExecuteResult loadPositionValidList(Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			params.put("VALID", "VALID");
			String p_names = (String) params.get("p_names");
			if (!FrameTool.isEmpty(p_names)) {
				params.put("pNameSet", FrameTool.getSetFromString(p_names));
			}
			String p_work_area = (String) params.get("P_WORK_AREA");
			if (!FrameTool.isEmpty(p_work_area)) {
				params.put("P_WORK_AREA_LEVEL", BusiCommon.getAreaLevel(p_work_area));
			}
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectBs_position", params);
			info.put("total", total);
			List<Map> rows = positionDAO.selectBs_position(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadPositionValidList", e);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult saveComPositionInfo(String orp_id, String opr_type, Map<String, Object> info)
			throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		String p_name = (String) info.get("P_NAME");
		String p_id = (String) info.get("P_ID");
		String c_id = (String) info.get("C_ID");
		boolean isAdd = false;
		if (FrameTool.isEmpty(p_id)) {
			isAdd = true;
			p_id = FrameTool.getUUID();
		}
		if (positionNameExits(p_id, c_id, p_name)) {
			rtn.setDefaultValue("相同的职位名称已经存在");
		} else {
			String P_DW_ZPR = (String) info.get("P_DW_ZPR");
			if (FrameConstant.busi_com_boolean_true.equals(P_DW_ZPR)) {
				info.remove("P_ZPR");
				info.remove("P_ZPDH");
			}
			if (isAdd) {
				info.put("P_ID", p_id);
				info.put("C_ID", c_id);
				info.put("OPR_ID", orp_id);
				info.put("OPR_TYPE", opr_type);
				frameDAO.anyInsert("bs_position", info);
			} else {
				frameDAO.anyUpdateByPk("bs_position", info, p_id);
			}
			rtn.setSucc(true);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult delComPosition(String[] pids) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		for (String pid : pids) {
			frameDAO.anyDeleteByPk("bs_position", pid);
		}
		rtn.setSucc(true);
		return rtn;
	}

	public ExecuteResult loadPostionInfo(String c_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = positionDAO.selectBs_positionById(c_id);
			if (!FrameTool.isEmpty(info)) {
				rtn.addInfo("positionInfo", info);
				rtn.setSucc(true);
			}
		} catch (Exception e) {
			log.error("loadPostionInfo", e);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult pubPosition(String[] pids) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("P_STATUS", FrameConstant.busi_com_boolean_true);
		for (String pid : pids) {
			frameDAO.anyUpdateByPk("bs_position", params, pid);
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult unpubPosition(String[] pids) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("P_STATUS", FrameConstant.busi_com_boolean_false);
		for (String pid : pids) {
			frameDAO.anyUpdateByPk("bs_position", params, pid);
		}
		rtn.setSucc(true);
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult sendPositionReq(String pid, String hr_id) throws SQLException {
		ExecuteResult rtn = new ExecuteResult();
		if (positionReqExists(pid, hr_id)) {
			rtn.setDefaultValue("您已经申请过此岗位，请勿重复申请");
		} else {
			Map params = new HashMap();
			params.put("REQ_ID", FrameTool.getUUID());
			params.put("P_ID", pid);
			params.put("HR_ID", hr_id);
			frameDAO.anyInsert("bs_position_req", params);
			rtn.setSucc(true);
		}
		return rtn;
	}

	public ExecuteResult loadHrReqList(String hr_id) {

		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		ExecuteResult rtn = loadReqList(params);
		return rtn;
	}

	public ExecuteResult loadReqList(Map params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectBs_position_req", params);
			info.put("total", total);
			List<Map> rows = positionDAO.selectBs_position_req(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadHrReqList", e);
		}
		return rtn;
	}

	public ExecuteResult loadPostionReqInfo(String req_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = positionDAO.selectBs_position_reqById(req_id);
			if (!FrameTool.isEmpty(info)) {
				rtn.addInfo("positionReqInfo", info);
				rtn.setSucc(true);
			}
		} catch (Exception e) {
			log.error("loadPostionReqInfo", e);
		}
		return rtn;
	}

	public ExecuteResult bgnPostionReqInterview(String req_id) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map reqInfo = positionDAO.selectBs_position_reqById(req_id);
			if (!FrameTool.isEmpty(reqInfo)) {
				rtn.addInfo("req_id", req_id);
				String pid = (String) reqInfo.get("P_ID");
				String hr_id = (String) reqInfo.get("HR_ID");
				Map positionInfo = positionDAO.selectBs_positionById(pid);
				if (!FrameTool.isEmpty(positionInfo)) {
					rtn.addInfo("positionInfo", positionInfo);
					Map hrInfo = humanResourceDAO.selectBusi_hr(hr_id);
					if (!FrameTool.isEmpty(hrInfo)) {
						rtn.addInfo("hrInfo", hrInfo);
						rtn.addInfo("rtmp_url", FrameCache.getFrameConfig("rtmp_url"));
						rtn.addInfo("room", req_id);
						rtn.setSucc(true);
					}
				}
			}
		} catch (Exception e) {
			log.error("bgnPostionReqInterview", e);
		}
		return rtn;
	}

	public ExecuteResult loadComPostionReqDetail(String req_id, String c_id) {
		ExecuteResult rtn = bgnPostionReqInterview(req_id);
		if (rtn.isSucc()) {
			Map hrInfo = (Map) rtn.getInfoOne("hrInfo");
			String hr_id = (String) hrInfo.get("HR_ID");
			Map params = new HashMap();
			params.put("c_id", c_id);
			params.put("hr_id", hr_id);
			try {
				frameDAO.anyInsert("BS_C_HR_VIEW_LOG", params);
			} catch (SQLException e) {
				log.error("", e);
			}
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult changeReqsStatus(String[] req_ids, String req_status) {
		ExecuteResult rtn = new ExecuteResult();
		for (String req_id : req_ids) {
			rtn = changeReqStatus(req_id, req_status);
			if (!rtn.isSucc()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rtn;
	}

	public ExecuteResult changeReqStatus(String req_id, String req_status) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map params = new HashMap();
			params.put("req_status", req_status);
			frameDAO.anyUpdateByPk("bs_position_req", params, req_id);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("changeReqStatus", e);
		}
		return rtn;
	}

	private boolean positionReqExists(String pid, String hr_id) {
		Map params = new HashMap();
		params.put("HR_ID", hr_id);
		params.put("P_ID", pid);
		List list = positionDAO.selectBs_position_req(params);
		return !FrameTool.isEmpty(list);
	}

	private boolean positionNameExits(String p_id, String c_id, String p_name) {
		Map position = positionDAO.selectBs_positionByName(c_id, p_name);
		if (FrameTool.isEmpty(position)) {
			return false;
		} else {
			String cur_p_id = (String) position.get("P_ID");
			if (cur_p_id.equals(p_id)) {
				return false;
			}
			return true;
		}
	}

}
