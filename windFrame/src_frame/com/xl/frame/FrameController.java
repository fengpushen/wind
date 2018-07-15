package com.xl.frame;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
		String jsonStr = FrameTool.toJson(rst);
		return jsonStr;
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

	@ResponseBody
	@RequestMapping(value = "/loadAllMenuGroup.do")
	public String loadAllMenuGroup() {

		ExecuteResult rst = frameService.getMenuGroupTreeJson();
		String menuJson = "";
		if (rst.isSucc()) {
			menuJson = rst.getDefaultValue();
		}
		return menuJson;
	}

	@RequestMapping("/showAddMenuUI.do")
	public ModelAndView showAddMenuUI(@RequestParam String menu_p_id) {
		Map trans = new HashMap();
		trans.put("pmenu", frameService.getMenu(menu_p_id));
		return new ModelAndView("/frame/menu_add", trans);
	}

	@RequestMapping("/dwnTempFile.do")
	public ResponseEntity<byte[]> dwnTempFile(@RequestParam(required = true) String shortName, String showName) {
		ResponseEntity<byte[]> rst = null;
		try {
			rst = BusiCommon.getFileDwnResponseEntity(BusiCommon.getTempFileFullName(shortName), showName);
		} catch (IOException e) {
			log.error("download", e);
		}
		return rst;
	}

}
