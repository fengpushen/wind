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

import com.xl.busi.BusiCommon;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCode;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;

@Controller
@RequestMapping("/frame")
public class FrameController {

	private static Log log = LogFactory.getLog(FrameController.class);

	@Autowired
	private FrameService frameService;

	@ResponseBody
	@RequestMapping(value = "/loadCode.do")
	public String loadUserMenu(@RequestParam(required = true) String codeName) {

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

	
}
