package com.xl.busi.staff;

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
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameTool;

@Controller
@RequestMapping("/busi/staff")
public class StaffController {

	private static Log log = LogFactory.getLog(StaffController.class);

	@Autowired
	private StaffService staffService;

	@RequestMapping("/showStaffMgeUI.do")
	public ModelAndView showStaffMgeUI() {

		return new ModelAndView("/busi/staff/staff_list");
	}

	@ResponseBody
	@RequestMapping(value = "/loadStaffList.do")
	public String loadStaffList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		String area_code = (String) info.get("area_code");
		if (FrameTool.isEmpty(area_code)) {
			info.put("area_code", BusiCommon.getLoginAccountStaffArea(session));
		}
		BusiCommon.dealAreaBj(info, "area_code");
		ExecuteResult rst = staffService.loadStaffList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showStaffInfo.do")
	public ModelAndView showStaffInfo() {

		Map trans = new HashMap();
		String default_psd_center = FrameCache.getFrameConfig("default_psd_center");
		trans.put("pswPrompt", "不填写密码则系统自动设置为：" + default_psd_center);
		return new ModelAndView("/busi/staff/staff_info", trans);
	}

	@RequestMapping("/showStaffMdy.do")
	public ModelAndView showStaffMdy(HttpSession session, @RequestParam(required = true) String staff_id) {

		Map trans = new HashMap();
		ExecuteResult rst = staffService.checkMgeAccount(BusiCommon.getLoginAccountBusiId(session), staff_id);
		if (rst.isSucc()) {
			rst = staffService.selectStaffInfo(staff_id);
			if (rst.isSucc()) {
				trans.put("staffInfo", rst.getInfoOne("staffInfo"));
				trans.put("accountInfo", rst.getInfoOne("accountInfo"));
				trans.put("roleMgds", FrameTool.toJson(rst.getInfoOne("roleMgds")));
				trans.put("pswPrompt", "不修改密码则无需填写");
				return new ModelAndView("/busi/staff/staff_info", trans);
			}
		}
		trans.put("tip", rst.getDefaultValue());
		return new ModelAndView("/busi/comm/showTip", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/saveStaffInfo.do")
	public String saveStaffInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = staffService.saveStaffInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), (Map) info.get("staffInfo"),
					(Map) info.get("accountInfo"));
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/delStaff.do")
	public String delStaff(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		Object delIds = info.get("ids[]");
		if (!FrameTool.isEmpty(delIds)) {
			rst = staffService.delStaffs(BusiCommon.getLoginAccountBusiId(session), FrameTool.getStringArray(delIds));
		}

		return FrameTool.toJson(rst);
	}
}
