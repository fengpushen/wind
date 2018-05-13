package com.xl.busi.position;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xl.busi.BusiCommon;
import com.xl.busi.company.CompanyService;
import com.xl.busi.hr.HumanResourceService;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;

@Controller
@RequestMapping("/busi/position")
public class PositionController {

	private static Log log = LogFactory.getLog(PositionController.class);

	@Autowired
	private PositionService positionService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private HumanResourceService humanResourceService;

	@RequestMapping("/showComPositionMgeUI")
	public ModelAndView showComPositionMgeUI() {

		return new ModelAndView("/busi/position/position_list_com");
	}
	
	@RequestMapping("/showCenterPositionUI")
	public ModelAndView showCenterPositionUI() {

		return new ModelAndView("/busi/position/position_list_center");
	}

	@ResponseBody
	@RequestMapping(value = "/loadComPositionList.do")
	public String loadComPositionList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		info.put("C_ID", BusiCommon.getLoginAccountBusiId(session));
		ExecuteResult rst = positionService.loadPositionList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/loadPositionValidList.do")
	public Object loadPositionValidList(HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		ExecuteResult rst = positionService.loadPositionValidList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return map;
		}
		return null;
	}

	@RequestMapping("/showAddComPositionInfoUI")
	public ModelAndView showAddComPositionInfoUI(HttpSession session) {
		Map positionInfo = positionInfo = new HashMap();

		String c_id = BusiCommon.getLoginAccountBusiId(session);
		ExecuteResult rst = companyService.loadComInfo(c_id);
		if (rst.isSucc()) {
			positionInfo.put("comInfo", rst.getInfoOne("comInfo"));
		}
		return new ModelAndView("/busi/position/position_info_com", positionInfo);
	}

	@RequestMapping("/showComPositionInfoSaveUI")
	public ModelAndView showComPositionInfoSaveUI(HttpSession session, @RequestParam(required = true) String pid) {
		Map positionInfo = null;
		ExecuteResult rst = positionService.loadPostionInfo(pid);
		if (rst.isSucc()) {
			positionInfo = (Map) rst.getInfoOne("positionInfo");
		}

		String c_id = BusiCommon.getLoginAccountBusiId(session);
		rst = companyService.loadComInfo(c_id);
		if (rst.isSucc()) {
			positionInfo.put("comInfo", rst.getInfoOne("comInfo"));
		}
		return new ModelAndView("/busi/position/position_info_com", positionInfo);
	}
	
	@RequestMapping("/showPositionInfoUI")
	public ModelAndView showPositionInfoUI(HttpSession session, @RequestParam(required = true) String pid) {
		Map positionInfo = null;
		ExecuteResult rst = positionService.loadPostionInfo(pid);
		if (rst.isSucc()) {
			positionInfo = (Map) rst.getInfoOne("positionInfo");
		}

		String c_id = (String)positionInfo.get("C_ID");
		rst = companyService.loadComInfo(c_id);
		if (rst.isSucc()) {
			positionInfo.put("comInfo", rst.getInfoOne("comInfo"));
		}
		return new ModelAndView("/busi/position/position_info_center", positionInfo);
	}

	@ResponseBody
	@RequestMapping(value = "/saveComPositionInfo.do")
	public String saveComPositionInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = positionService.saveComPositionInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), info);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/pubPosition.do")
	public String pubPosition(HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object delIds = info.get("ids[]");
			if (!FrameTool.isEmpty(delIds)) {
				rst = positionService.pubPosition(FrameTool.getStringArray(delIds));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/unpubPosition.do")
	public String unpubPosition(HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object delIds = info.get("ids[]");
			if (!FrameTool.isEmpty(delIds)) {
				rst = positionService.unpubPosition(FrameTool.getStringArray(delIds));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/delComPosition.do")
	public String delComPosition(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object delIds = info.get("ids[]");
			if (!FrameTool.isEmpty(delIds)) {
				rst = positionService.delComPosition(FrameTool.getStringArray(delIds));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showMobilePositionInfo.do")
	public ModelAndView showMobilePositionInfo(@RequestParam(required = true) String pid) {

		Map positionInfo = null;

		ExecuteResult rst = positionService.loadPostionInfo(pid);
		if (rst.isSucc()) {
			positionInfo = (Map) rst.getInfoOne("positionInfo");
			if (!FrameTool.isEmpty(positionInfo)) {
				String cid = (String) positionInfo.get("C_ID");
				rst = companyService.loadComInfo(cid);
				if (rst.isSucc()) {
					Map comInfo = (Map) rst.getInfoOne("comInfo");
					positionInfo.put("comInfo", comInfo);
					String cidWt = (String) comInfo.get("C_ID_WT");
					if (!FrameTool.isEmpty(cidWt)) {
						rst = companyService.loadComInfo(cidWt);
						if (rst.isSucc()) {
							positionInfo.put("comWtInfo", rst.getInfoOne("comInfo"));
						}
					}
				}
			}
		}

		return new ModelAndView("/busi/mobile/hr/mo_position_info", positionInfo);
	}

	@RequestMapping("/showMobilePositionConfirmUI.do")
	public ModelAndView showMobilePositionConfirmUI(@RequestParam(required = true) String pid,
			@RequestParam(required = true) String hr_id) {

		Map positionInfo = null;

		ExecuteResult rst = positionService.loadPostionInfo(pid);
		if (rst.isSucc()) {
			positionInfo = (Map) rst.getInfoOne("positionInfo");
			if (!FrameTool.isEmpty(positionInfo)) {
				String cid = (String) positionInfo.get("C_ID");
				rst = companyService.loadComInfo(cid);
				if (rst.isSucc()) {
					positionInfo.put("comInfo", rst.getInfoOne("comInfo"));
				}
			}
		}
		rst = humanResourceService.loadHrInfo(hr_id);
		if (rst.isSucc()) {
			positionInfo.put("hrInfo", rst.getInfoOne("hrInfo"));
		}

		return new ModelAndView("/busi/mobile/hr/mo_hr_resume", positionInfo);
	}

	@ResponseBody
	@RequestMapping(value = "/sendPositionReq.do")
	public String sendPositionReq(@RequestParam(required = true) String pid,
			@RequestParam(required = true) String hr_id) {

		ExecuteResult rst = new ExecuteResult();
		try {
			rst = positionService.sendPositionReq(pid, hr_id);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showMobilePositionHrReqsUI.do")
	public ModelAndView showMobilePositionHrReqsUI() {
		return new ModelAndView("/busi/mobile/hr/mo_hr_reqs");
	}

	@ResponseBody
	@RequestMapping(value = "/loadHrReqList.do")
	public String loadHrReqList(@RequestParam(required = true) String hr_id) {

		ExecuteResult rst = positionService.loadHrReqList(hr_id);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/loadCorpReqList.do")
	public String loadCorpReqList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		info.put("owner_id", BusiCommon.getLoginAccountBusiId(session));
		ExecuteResult rst = positionService.loadReqList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showComPositionReqMgeUI.do")
	public ModelAndView showComPositionReqMgeUI() {
		Map trans = new HashMap();
		trans.put("req_status", FrameConstant.busi_req_status_wait_deal);
		return new ModelAndView("/busi/position/position_req_list_com", trans);
	}

	@RequestMapping("/showComPositionReqInterviewUI.do")
	public ModelAndView showComPositionReqInterviewUI(HttpServletRequest request,
			@RequestParam(required = true) String req_id) {

		Map trans = new HashMap();
		String host = request.getHeader("host");
		ExecuteResult rst = positionService.bgnPostionReqInterview(req_id, host);
		if (rst.isSucc()) {
			trans = rst.getInfo();
		}
		return new ModelAndView("/busi/position/position_req_interview_com_rtc", trans);
	}
	
	@ResponseBody
	@RequestMapping(value = "/bgnPostionReqInterview.do")
	public String bgnPostionReqInterview(HttpServletRequest request,
			@RequestParam(required = true) String req_id) {

		String host = request.getHeader("host");
		ExecuteResult rst = positionService.bgnPostionReqInterview(req_id, host);
		return FrameTool.toJson(rst);
	}
	
	@ResponseBody
	@RequestMapping(value = "/personInPostionReqInterview.do")
	public String personInPostionReqInterview(HttpServletRequest request,
			@RequestParam(required = true) String req_id) {

		String host = request.getHeader("host");
		ExecuteResult rst = positionService.personInPostionReqInterview(req_id, host);
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showMobilePositionInterview.do")
	public ModelAndView showMobilePositionInterview(HttpServletRequest request,
			@RequestParam(required = true) String req_id) {

		String host = request.getHeader("host");
		ExecuteResult rst = positionService.personInPostionReqInterview(req_id, host);
		return new ModelAndView("/busi/mobile/hr/mo_hr_interview_rtc", rst.getInfo());
	}

	@RequestMapping("/showMobilePositionSearch.do")
	public ModelAndView showMobilePositionSearch() {

		Map trans = new HashMap();
		return new ModelAndView("/busi/mobile/hr/mo_position_search", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/searchMobilePositionValidList.do")
	public String searchMobilePositionValidList(HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		ExecuteResult rst = positionService.loadPositionValidList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping(value = "/searchMobilePositionValidListUI.do")
	public ModelAndView searchMobilePositionValidListUI(HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		return new ModelAndView("/busi/mobile/hr/mo_position_search_result", info);
	}

	@RequestMapping("/showComPositionReqDetail.do")
	public ModelAndView showComPositionReqDetail(HttpSession session, HttpServletRequest request,
			@RequestParam(required = true) String req_id) {

		Map trans = new HashMap();
		ExecuteResult rst = positionService.loadComPostionReqDetail(req_id, BusiCommon.getLoginAccountBusiId(session));
		if (rst.isSucc()) {
			trans = rst.getInfo();
		}
		return new ModelAndView("/busi/position/position_req_detail", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/changeReqStatus.do")
	public String changeReqStatus(@RequestParam(required = true) String req_id,
			@RequestParam(required = true) String req_status) {

		ExecuteResult rst = positionService.changeReqStatus(req_id, req_status);
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/changeReqsStatus.do")
	public String changeReqsStatus(HttpServletRequest request, @RequestParam(required = true) String req_status) {
		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		Object delIds = info.get("ids[]");
		ExecuteResult rst = positionService.changeReqsStatus(FrameTool.getStringArray(delIds), req_status);
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showPositionReqJobReg.do")
	public ModelAndView showPositionReqJobReg(@RequestParam(required = true) String req_id) {

		Map trans = new HashMap();
		ExecuteResult rst = positionService.loadPostionReqInfo(req_id);
		if (rst.isSucc()) {
			trans = rst.getInfo();
		}
		return new ModelAndView("/busi/position/position_req_job_reg", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/saveJobInfo.do")
	public String saveJobInfo(HttpSession session, HttpServletRequest request,
			@RequestParam(required = true) String req_id) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = humanResourceService.saveJobInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), info);
			if (rst.isSucc()) {
				rst = positionService.changeReqStatus(req_id, "05");
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/sendPositionReqCenter.do")
	public String sendPositionReqCenter(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object pids = info.get("pids[]");
			Object hr_ids = info.get("hr_ids[]");
			if (!FrameTool.isEmpty(pids)) {
				rst = positionService.sendPositionReqsCenter(BusiCommon.getLoginAccountId(session),
						FrameTool.getStringArray(pids), FrameTool.getStringArray(hr_ids));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

}
