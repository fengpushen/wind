package com.xl.restful.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xl.busi.staff.StaffService;
import com.xl.frame.FrameService;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;
import com.xl.util.Tree;

@RestController
@RequestMapping("/login")
public class LoginRestController {

	@Autowired
	private FrameService frameService;

	@Autowired
	private StaffService staffService;

	@ResponseBody
	@RequestMapping(value = "/login.do")
	public ExecuteResult login(@RequestBody Map<String, String> map) {

		ExecuteResult rst = frameService.checkAccountLogin(FrameConstant.busi_user_kind_rs, (String) map.get("account"),
				(String) map.get("password"));
		if (rst.isSucc()) {
			Map info = (Map) rst.getInfoOne("accountInfo");
			String busi_id = (String) info.get("BUSI_ID");
			ExecuteResult rtn = staffService.selectStaffInfo(busi_id);
			if (rtn.isSucc()) {
				Map staffInfo = (Map) rtn.getInfoOne("staffInfo");
				String token = putLoginInfo(staffInfo);
				rtn.addInfo("staffInfo", staffInfo);
				rtn.addInfo("token", token);
				return rtn;
			}
		}
		return rst;
	}

	@ResponseBody
	@RequestMapping(value = "/loadUserMenu.do")
	public Tree loadUserMenu(@RequestHeader(value = "Authorization") String token) {

		Map<String, Object> map = new HashMap<String, Object>();
		Map loginInfo = getLoginInfo(token);
		String account_id = (String) loginInfo.get("ACCOUNT_ID");
		Tree menuTree = FrameCache.getMenuTree();
		return menuTree;
	}

	@ResponseBody
	@RequestMapping(value = "/logout.do")
	public String logout(@RequestHeader(value = "Authorization") String token) {

		ExecuteResult rst = new ExecuteResult();
		try {
			removeLoginInfo(token);
			rst.setSucc(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FrameTool.toJson(rst);
	}

	private String putLoginInfo(Map info) {
		String token = FrameTool.getUUID();
		tokenLoginInfo.put(token, info);
		return token;
	}

	private Map getLoginInfo(String token) {
		return tokenLoginInfo.get(token);
	}

	private void removeLoginInfo(String token) {
		tokenLoginInfo.remove(token);
	}

	private Map<String, Map> tokenLoginInfo = new HashMap<String, Map>();

}
