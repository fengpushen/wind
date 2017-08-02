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
import com.xl.busi.common.CommonDAO;
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

	@Autowired
	private CommonDAO commonDAO;

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

	public ExecuteResult loadComRegJobCountList(Map params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectBs_job_nojob_tj", params);
			info.put("total", total);
			List<Map> rows = companyDAO.selectBs_job_nojob_tj(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadHrList", e);
		}
		return rtn;
	}

	public ExecuteResult loadAreaList(Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectV_com_area", params);
			info.put("total", total);
			List<Map> rows = commonDAO.selectV_com_area(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadAreaList", e);
		}
		return rtn;
	}

	public ExecuteResult bgnAreaVideoChat(String c_id, String area_code, String host) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map params = new HashMap();
			String room = FrameTool.getUUID();
			params.put("C_AREA_VIDEO_ID", room);
			params.put("c_id", c_id);
			params.put("area_code", area_code);
			frameDAO.anyInsert("bs_c_area_video", params);

			rtn.addInfo("rtmp_url", BusiCommon.getRtmpUrl(host));
			rtn.addInfo("room", room);
			rtn.setSucc(true);

		} catch (Exception e) {
			log.error("bgnAreaVideoChat", e);
		}
		return rtn;
	}

	public ExecuteResult bgnAreaVideoChatCenter(String c_area_video_id, String area_code, String host) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map params = new HashMap();
			Map videoInfo = companyDAO.selectV_c_area_video_last(c_area_video_id);
			if (!FrameTool.isEmpty(videoInfo)) {
				String c_id = (String) videoInfo.get("C_ID");
				Map comInfo = companyDAO.selectBs_companyById(c_id);

				params.put("VIDEO_STATUS", "1");
				frameDAO.anyUpdateByPk("bs_c_area_video", params, c_area_video_id);
				rtn.addInfo("rtmp_url", BusiCommon.getRtmpUrl(host));
				rtn.addInfo("room", c_area_video_id);
				rtn.setSucc(true);
			}
		} catch (Exception e) {
			log.error("bgnAreaVideoChatCenter", e);
		}
		return rtn;
	}

	public ExecuteResult loadAreaVideoList(String area_code) {
		ExecuteResult rtn = new ExecuteResult();
		Map params = new HashMap();
		params.put("area_code", area_code);
		try {
			Map info = new HashMap();
			int total = frameDAO.selectRecord_count("selectV_c_area_video_last", params);
			info.put("total", total);
			List<Map> rows = companyDAO.selectV_c_area_video_last(params);
			info.put("rows", rows);
			rtn.addInfo("info", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadAreaList", e);
		}
		return rtn;
	}

	public ExecuteResult loadCenterPhone(String area_code) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map info = companyDAO.selectBs_s_area_phone(area_code);
			rtn.addInfo("phoneInfo", info);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("loadCenterPhone", e);
		}
		return rtn;
	}

	@Transactional(rollbackFor = Exception.class)
	public ExecuteResult setCenterPhone(String area_code, Map<String, Object> params) {
		ExecuteResult rtn = new ExecuteResult();
		try {
			Map delMap = new HashMap();
			delMap.put("area_code", area_code);
			frameDAO.anyDelete("bs_s_area_phone", delMap);
			params.put("area_code", area_code);
			frameDAO.anyInsert("bs_s_area_phone", params);
			rtn.setSucc(true);
		} catch (Exception e) {
			log.error("setCenterPhone", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
