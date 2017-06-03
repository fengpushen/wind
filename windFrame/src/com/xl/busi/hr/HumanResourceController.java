package com.xl.busi.hr;

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
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;

@Controller
@RequestMapping("/busi/hr")
public class HumanResourceController {

	private static Log log = LogFactory.getLog(HumanResourceController.class);

	@Autowired
	private HumanResourceService humanResourceService;

	@RequestMapping("/qryHrList")
	public ModelAndView qryHrList() {

		return new ModelAndView("/busi/hr/hr_list");
	}

	@ResponseBody
	@RequestMapping(value = "/loadHrList.do")
	public String loadHrList(HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		ExecuteResult rst = humanResourceService.loadHrList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showHrInfo")
	public ModelAndView showHrInfo() {

		return new ModelAndView("/busi/hr/hr_info");
	}

	@ResponseBody
	@RequestMapping(value = "/saveHrInfo.do")
	public String saveHrInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = humanResourceService.saveHrInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), info);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/loadHrInfo.do")
	public String loadHrInfo(@RequestParam(required = true) String hr_id) {

		ExecuteResult rst = humanResourceService.loadHrInfo(hr_id);
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/delHrInfo.do")
	public String delHrInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object delIds = info.get("ids[]");
			if (!FrameTool.isEmpty(delIds)) {
				rst = humanResourceService.delHrInfo(FrameTool.getStringArray(delIds),
						BusiCommon.getLoginAccountId(session), BusiCommon.getLoginAccountKind(session));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/delJobNojobInfo.do")
	public String delJobNojobInfo(HttpSession session, HttpServletRequest request,
			@RequestParam(required = true) String hr_id) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object ids = info.get("ids[]");
			Object is_jobs = info.get("is_jobs[]");
			if (!FrameTool.isEmpty(ids) && !FrameTool.isEmpty(is_jobs)) {
				rst = humanResourceService.delHrJobNojobInfo(hr_id, FrameTool.getStringArray(ids),
						FrameTool.getStringArray(is_jobs), BusiCommon.getLoginAccountId(session),
						BusiCommon.getLoginAccountKind(session));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showHrJobnojobMge")
	public ModelAndView showHrJobnojobMge(@RequestParam(required = true) String hr_id) {
		Map hrInfo = humanResourceService.getHrInfo(hr_id);
		return new ModelAndView("/busi/hr/hr_jobnojob_mge", hrInfo);
	}

	@ResponseBody
	@RequestMapping(value = "/loadHrJobList.do")
	public String loadHrJobList(@RequestParam(required = true) String hr_id) {

		ExecuteResult rst = humanResourceService.loadHrJobList(hr_id);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showHrJobReg")
	public ModelAndView showHrJobReg(@RequestParam(required = true) String hr_id) {
		Map hrInfo = humanResourceService.getHrInfo(hr_id);
		return new ModelAndView("/busi/hr/hr_job_reg", hrInfo);
	}

	@RequestMapping("/showHrNOjobReg")
	public ModelAndView showHrNOjobReg(@RequestParam(required = true) String hr_id) {
		Map hrInfo = humanResourceService.getHrInfo(hr_id);
		return new ModelAndView("/busi/hr/hr_nojob_reg", hrInfo);
	}

	@ResponseBody
	@RequestMapping(value = "/saveJobInfo.do")
	public String saveJobInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = humanResourceService.saveJobInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), info);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/saveNOjobInfo.do")
	public String saveNOjobInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = humanResourceService.saveNOjobInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), info);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showMobileHrBindUI.do")
	public ModelAndView showMobileHrBindUI() {

		return new ModelAndView("/busi/mobile/hr/mo_hr_bind");
	}

	@ResponseBody
	@RequestMapping(value = "/bindHr.do")
	public String bindHr(@RequestParam(required = true) String idcard, @RequestParam(required = true) String hr_name) {

		ExecuteResult rst = humanResourceService.bindHr(idcard, hr_name);
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/saveHrInfoSelfMo.do")
	public String saveHrInfoSelfMo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = humanResourceService.updateHrInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), BusiCommon.getLoginAccountBusiId(session), info);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/unbindHr.do")
	public ModelAndView unbindHr() {
		Map trans = new HashMap();
		trans.put("rebind", FrameConstant.busi_com_boolean_true);
		return new ModelAndView("/busi/mobile/hr/mo_hr_bind", trans);
	}

	@RequestMapping("/showMobileHrCenterUI.do")
	public ModelAndView showMobileHrCenterUI(HttpSession session) {

		String hr_id = BusiCommon.getLoginAccountBusiId(session);
		if (!FrameTool.isEmpty(hr_id)) {
			Map info = humanResourceService.getHrInfo(hr_id);
			if (!FrameTool.isEmpty(info)) {
				return new ModelAndView("/busi/mobile/hr/mo_hr_main", info);
			}
		}
		return unbindHr();
	}

	@RequestMapping("/showMobileHrInfoUI.do")
	public ModelAndView showMobileHrInfoUI(HttpSession session) {

		String hr_id = BusiCommon.getLoginAccountBusiId(session);
		if (!FrameTool.isEmpty(hr_id)) {
			Map info = humanResourceService.getHrInfo(hr_id);
			if (!FrameTool.isEmpty(info)) {
				return new ModelAndView("/busi/mobile/hr/mo_hr_info", info);
			}
		}
		return unbindHr();
	}

	@RequestMapping("/showMobileHrJobNOjobMgeUI.do")
	public ModelAndView showMobileHrJobNOjobMgeUI() {

		return new ModelAndView("/busi/mobile/hr/mo_hr_jobnojob_mge");
	}
	
	@RequestMapping("/showMobileHrJobRegUI.do")
	public ModelAndView showMobileHrJobRegUI() {

		return new ModelAndView("/busi/mobile/hr/mo_hr_job_reg");
	}
	
	@RequestMapping("/showMobileHrNojobRegUI.do")
	public ModelAndView showMobileHrNojobRegUI() {

		return new ModelAndView("/busi/mobile/hr/mo_hr_nojob_reg");
	}

}
