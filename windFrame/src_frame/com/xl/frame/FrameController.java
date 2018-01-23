package com.xl.frame;

import java.util.List;
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
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCode;
import com.xl.frame.util.FrameTool;

@Controller
@RequestMapping("/frame")
public class FrameController {

	private static Log log = LogFactory.getLog(FrameController.class);

	@Autowired
	private FrameService frameService;

	@ResponseBody
	@RequestMapping(value = "/loadCode.do")
	public String loadCode(@RequestParam(required = true) String codeName) {

		String codeJson = FrameCode.getCodeJson(codeName);
		if (codeJson == null) {
			codeJson = "";
		}
		return codeJson;
	}

	@ResponseBody
	@RequestMapping(value = "/loadAccountRolesMgd.do")
	public String loadAccountRolesMgd(HttpSession session) {

		String account_id = BusiCommon.getLoginAccountId(session);
		List<Map> roles = frameService.getAccountsRoleMgd(account_id);
		String json = FrameTool.toJson(roles);
		if (json == null) {
			json = "";
		}
		return json;
	}

	@RequestMapping("/showMenuMgeUI.do")
	public ModelAndView showMenuMgeUI() {

		return new ModelAndView("/frame/menu_list");
	}

	@ResponseBody
	@RequestMapping(value = "/loadAllMenu.do")
	public String loadUserMenu() {

		ExecuteResult rst = frameService.getMenuTreeJson();
		String menuJson = "";
		if (rst.isSucc()) {
			menuJson = rst.getDefaultValue();
		}
		return menuJson;
	}

	@ResponseBody
	@RequestMapping(value = "/loadOneMenu.do")
	public String loadOneMenu(@RequestParam(required = true) String menu_id) {

		ExecuteResult rst = frameService.getMenuJson(menu_id);
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/loadRoleList.do")
	public String loadHrList() {

		ExecuteResult rst = frameService.loadRoleList(null);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

}
