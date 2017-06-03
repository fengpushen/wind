package com.xl.busi.login;

import java.util.HashMap;
import java.util.Map;

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
import com.xl.busi.staff.StaffService;
import com.xl.frame.FrameService;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;

@Controller
@RequestMapping("/loginaccount")
public class LoginController {

	private static Log log = LogFactory.getLog(LoginController.class);

	@Autowired
	private FrameService frameService;

	@Autowired
	private StaffService staffService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private HumanResourceService humanResourceService;

	@RequestMapping("/showLoginUI.do")
	public ModelAndView showLoginUI() {
		return new ModelAndView("/login", getAppNameAndRightInfo());
	}

	@RequestMapping("/showComLoginUI.do")
	public ModelAndView showComLoginUI() {
		return new ModelAndView("/login_com", getAppNameAndRightInfo());
	}

	@ResponseBody
	@RequestMapping(value = "/login.do")
	public String login(@RequestParam(required = true) String account, @RequestParam(required = true) String password,
			HttpSession session) {

		Map<String, Object> map = new HashMap<String, Object>();
		ExecuteResult rst = frameService.checkAccountLogin(FrameConstant.busi_user_kind_rs, account, password);
		if (rst.isSucc()) {
			Map info = (Map) rst.getInfoOne("accountInfo");
			String busi_id = (String) info.get("BUSI_ID");
			ExecuteResult rtn = staffService.selectStaffInfo(busi_id);
			if (rtn.isSucc()) {
				info.put("staffInfo", rtn.getInfoOne("staffInfo"));
				session.setAttribute("accountInfo", info);
			}
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/loginCom.do")
	public String loginCom(@RequestParam(required = true) String account,
			@RequestParam(required = true) String password, HttpSession session) {

		ExecuteResult rst = new ExecuteResult();
		rst = frameService.checkAccountLogin(FrameConstant.busi_user_kind_com, account, password);
		if (rst.isSucc()) {
			Map info = (Map) rst.getInfoOne("accountInfo");
			String busi_id = (String) info.get("BUSI_ID");
			rst = companyService.loadComInfo(busi_id);
			if (rst.isSucc()) {
				info.put("comInfo", rst.getInfoOne("comInfo"));
				session.setAttribute("accountInfo", info);
			}
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showMainUI.do")
	public ModelAndView showMainUI(HttpSession session) {
		Map trans = getAppNameAndRightInfo();
		trans.put("accountShowName", BusiCommon.getLoginAccountShowName(session));
		if (BusiCommon.isLoginAccountKindRs(session)) {
			String areaCode = BusiCommon.getLoginAccountStaffArea(session);
			String areaName = BusiCommon.getAreaName(areaCode);
			trans.put("accountAreaName", areaName);
		}
		return new ModelAndView("/main", trans);
	}

	@RequestMapping("/showWelcomeUI.do")
	public ModelAndView showWelcomeUI() {
		return new ModelAndView("/welcome", getAppNameAndRightInfo());
	}

	@ResponseBody
	@RequestMapping(value = "/loadUserMenu.do")
	public String loadUserMenu(HttpSession session) {

		Map<String, Object> map = new HashMap<String, Object>();
		Map accountInfo = (Map) session.getAttribute("accountInfo");
		String account_id = (String) accountInfo.get("ACCOUNT_ID");
		ExecuteResult rst = frameService.getAccountMenuTreeJson(account_id);
		String menuJson = "";
		if (rst.isSucc()) {
			menuJson = rst.getDefaultValue();
		}
		return menuJson;
	}

	@RequestMapping(value = "/loginHr.do")
	public ModelAndView loginHr(@RequestParam(required = true) String hr_id, HttpSession session) {

		Map info = humanResourceService.getHrInfo(hr_id);
		if (FrameTool.isEmpty(info)) {
			Map trans = new HashMap();
			trans.put("rebind", FrameConstant.busi_com_boolean_true);
			return new ModelAndView("/busi/mobile/hr/mo_hr_bind", trans);
		} else {
			String is_job = (String) info.get("IS_JOB");
			Map accountInfo = new HashMap();
			accountInfo.put("ACCOUNT_ID", info.get("HR_ID"));
			accountInfo.put("BUSI_ID", info.get("HR_ID"));
			accountInfo.put("ACCOUNT_KIND", FrameConstant.busi_user_kind_hr);
			accountInfo.put("hrInfo", info);
			session.setAttribute("accountInfo", accountInfo);
			if (FrameTool.isEmpty(is_job)) {
				return new ModelAndView("/busi/mobile/hr/mo_hr_info", info);
			} else {
				return new ModelAndView("/busi/mobile/hr/mo_hr_main", info);
			}
		}

	}

	@ResponseBody
	@RequestMapping(value = "/logout.do")
	public String logout(HttpSession session) {

		ExecuteResult rst = new ExecuteResult();
		try {
			if (BusiCommon.isLogined(session)) {
				if (BusiCommon.isLoginAccountKindRs(session)) {
					rst.setDefaultValue("loginaccount/showLoginUI.do");
				}
				if (BusiCommon.isLoginAccountKindCom(session)) {
					rst.setDefaultValue("loginaccount/showComLoginUI.do");
				}
			} else {
				rst.setDefaultValue("loginaccount/showLoginUI.do");
			}
			session.removeAttribute(FrameConstant.frame_login_session_key);
			rst.setSucc(true);
		} catch (Exception e) {
			log.error("", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showChangePwdUI.do")
	public ModelAndView showChangePwdUI() {
		return new ModelAndView("/changePwd");
	}

	@ResponseBody
	@RequestMapping(value = "/changeAccountSelfPwd.do")
	public String changeAccountSelfPwd(@RequestParam(required = true) String password,
			@RequestParam(required = true) String password_old, HttpSession session) {

		ExecuteResult rst = frameService.changeAccountSelfPwd(BusiCommon.getLoginAccountId(session), password_old,
				password);
		return FrameTool.toJson(rst);
	}

	private Map getAppNameAndRightInfo() {
		Map trans = new HashMap();
		trans.put("app_name", FrameCache.getFrameConfig("app_name"));
		trans.put("ownership_of_copyright", FrameCache.getFrameConfig("ownership_of_copyright"));
		return trans;
	}
}
