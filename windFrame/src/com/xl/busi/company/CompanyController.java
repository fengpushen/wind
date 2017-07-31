package com.xl.busi.company;

import java.util.HashMap;
import java.util.List;
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
import com.xl.frame.FrameService;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;

@Controller
@RequestMapping("/busi/company")
public class CompanyController {

	private static Log log = LogFactory.getLog(CompanyController.class);

	@Autowired
	private CompanyService companyService;

	@Autowired
	private FrameService frameService;

	@RequestMapping("/showComMgeUI")
	public ModelAndView qryComList() {

		Map trans = new HashMap();
		trans.put("C_TYPE", FrameConstant.busi_company_type_normal);
		return new ModelAndView("/busi/company/company_list", trans);
	}

	@RequestMapping("/showComMgeLwzjUI")
	public ModelAndView showComMgeLwzjUI() {

		Map trans = new HashMap();
		trans.put("C_TYPE", FrameConstant.busi_company_type_lwzj);
		return new ModelAndView("/busi/company/company_list", trans);
	}

	@RequestMapping("/showComMgeZyjsUI")
	public ModelAndView showComMgeZyjsUI() {

		Map trans = new HashMap();
		trans.put("C_TYPE", FrameConstant.busi_company_type_zyjs);
		return new ModelAndView("/busi/company/company_list", trans);
	}

	@RequestMapping("/showComMgeWtzpUI")
	public ModelAndView showComMgeWtzpUI(HttpSession session) {

		Map trans = new HashMap();
		trans.put("C_TYPE", FrameConstant.busi_company_type_wtzp);
		trans.put("C_ID_WT", BusiCommon.getLoginAccountBusiId(session));
		return new ModelAndView("/busi/company/company_list", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/loadComList.do")
	public String loadComList(HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		ExecuteResult rst = companyService.loadComList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showComInfoSaveUI")
	public ModelAndView showComInfoSaveUI(String cid, String c_type, String c_id_wt) {
		Map trans = new HashMap();
		if (!FrameTool.isEmpty(c_type)) {
			trans.put("C_TYPE", c_type);
		}
		if (!FrameTool.isEmpty(c_id_wt)) {
			trans.put("C_ID_WT", c_id_wt);
		}
		if (!FrameTool.isEmpty(cid)) {
			ExecuteResult rst = companyService.loadComInfo(cid);
			if (rst.isSucc()) {
				trans = (Map) rst.getInfoOne("comInfo");
			}
		}
		return new ModelAndView("/busi/company/company_info", trans);
	}

	@RequestMapping("/showComInfoSaveSelfUI")
	public ModelAndView showComInfoSaveSelfUI(HttpSession session) {
		Map comInfo = null;
		String cid = BusiCommon.getLoginAccountBusiId(session);
		if (!FrameTool.isEmpty(cid)) {
			ExecuteResult rst = companyService.loadComInfo(cid);
			if (rst.isSucc()) {
				comInfo = (Map) rst.getInfoOne("comInfo");
			}
		}
		return new ModelAndView("/busi/company/company_info_self", comInfo);
	}

	@RequestMapping("/showComAccountMgeUI")
	public ModelAndView showComAccountMgeUI(@RequestParam(required = true) String cid) {
		Map comInfo = null;

		ExecuteResult rst = companyService.loadComInfo(cid);
		if (rst.isSucc()) {
			comInfo = (Map) rst.getInfoOne("comInfo");
		}
		return new ModelAndView("/busi/company/company_account_mge", comInfo);
	}

	@ResponseBody
	@RequestMapping(value = "/loadComInfo.do")
	public String loadComInfo(@RequestParam(required = true) String cid) {

		ExecuteResult rst = new ExecuteResult();
		try {
			rst = companyService.loadComInfo(cid);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/saveComInfo.do")
	public String saveComInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = companyService.saveComInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), info);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/delComInfo.do")
	public String delComInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object delIds = info.get("ids[]");
			if (!FrameTool.isEmpty(delIds)) {
				rst = companyService.delComInfo(FrameTool.getStringArray(delIds), BusiCommon.getLoginAccountId(session),
						BusiCommon.getLoginAccountKind(session));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/loadComAccountList.do")
	public String loadComAccountList(@RequestParam(required = true) String cid) {

		ExecuteResult rst = companyService.selectComAccountList(cid);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showAddAccountUI")
	public ModelAndView showAddAccountUI(@RequestParam(required = true) String cid) {
		Map info = new HashMap();
		info.put("busi_id", cid);
		info.put("password", FrameCache.getFrameConfig("default_psd_company"));
		Map comInfo = null;

		ExecuteResult rst = companyService.loadComInfo(cid);
		if (rst.isSucc()) {
			comInfo = (Map) rst.getInfoOne("comInfo");
		}
		info.put("busi_name", comInfo.get("C_NAME"));
		return new ModelAndView("/busi/company/company_account_add", info);
	}

	@ResponseBody
	@RequestMapping(value = "/addComAccount.do")
	public String addComAccount(HttpSession session, @RequestParam(required = true) String cid,
			@RequestParam(required = true) String account, String password) {

		ExecuteResult rst = new ExecuteResult();
		try {
			rst = companyService.addComAccount(BusiCommon.getLoginAccountId(session), cid, account, password);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/resetComAccountPassword.do")
	public String resetComAccountPassword(@RequestParam(required = true) String account_id) {

		ExecuteResult rst = new ExecuteResult();
		try {
			String defaultPassword = FrameCache.getFrameConfig("default_psd_company");
			rst = frameService.setAccountPassword(account_id, defaultPassword);
			rst.setSucc(true);
			rst.setDefaultValue("密码已经重置为：" + defaultPassword);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/delComAccount.do")
	public String delComAccount(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object delIds = info.get("ids[]");
			if (!FrameTool.isEmpty(delIds)) {
				rst = frameService.delAccounts(FrameTool.getStringArray(delIds));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/loadCompanyMgd.do")
	public String loadCompanyMgd(HttpSession session) {

		String c_id = BusiCommon.getLoginAccountBusiId(session);
		List<Map> mgds = companyService.selectComanyMge(c_id);
		String json = FrameTool.toJson(mgds);
		if (json == null) {
			json = "";
		}
		return json;
	}

	@RequestMapping("/showComRegJobList")
	public ModelAndView showComRegJobList() {
		return new ModelAndView("/busi/company/company_reg_job_list");
	}

	@ResponseBody
	@RequestMapping(value = "/loadComRegJobList.do")
	public String loadComRegJobList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		info.put("opr_id", BusiCommon.getLoginAccountId(session));
		BusiCommon.dealAreaBj(info, "HJ_AREA");
		ExecuteResult rst = companyService.loadComRegJobList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showComRegJobListAll")
	public ModelAndView showComRegJobListAll() {
		return new ModelAndView("/busi/company/company_reg_job_list_all");
	}

	@ResponseBody
	@RequestMapping(value = "/loadComRegJobListAll.do")
	public String loadComRegJobListAll(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		info.put("opr_type", FrameConstant.busi_user_kind_com);
		BusiCommon.dealAreaBj(info, "HJ_AREA");
		ExecuteResult rst = companyService.loadComRegJobList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showComRegJobCountList")
	public ModelAndView showComRegJobCountList() {
		return new ModelAndView("/busi/company/company_reg_job_count_list");
	}

	@ResponseBody
	@RequestMapping(value = "/loadComRegJobCountList.do")
	public String loadComRegJobCountList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		info.put("opr_id", BusiCommon.getLoginAccountId(session));
		BusiCommon.dealAreaBj(info, "HJ_AREA");
		ExecuteResult rst = companyService.loadComRegJobCountList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showComRegJobCountListAll")
	public ModelAndView showComRegJobCountListAll() {
		return new ModelAndView("/busi/company/company_reg_job_count_list_all");
	}

	@ResponseBody
	@RequestMapping(value = "/loadComRegJobCountListAll.do")
	public String loadComRegJobCountListAll(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		info.put("opr_type", FrameConstant.busi_user_kind_com);
		BusiCommon.dealAreaBj(info, "HJ_AREA");
		ExecuteResult rst = companyService.loadComRegJobCountList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showComAreaList")
	public ModelAndView showComAreaList() {
		Map trans = new HashMap();
		trans.put("area_code_high", "430900000000");
		trans.put("area_name_high", "益阳市");
		return new ModelAndView("/busi/company/company_area_list", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/loadAreaList.do")
	public String loadAreaList(HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		String area_code_high = (String) info.get("area_code_high");
		if (!FrameTool.isEmpty(area_code_high)) {
			info.put("area_code_high_level", BusiCommon.getAreaLevel(area_code_high));
		}
		ExecuteResult rst = companyService.loadAreaList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

}
