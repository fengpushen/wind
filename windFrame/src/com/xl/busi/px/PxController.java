package com.xl.busi.px;

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
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.xl.busi.BusiCommon;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameTool;

@Controller
@RequestMapping("/busi/px")
public class PxController {

	private static Log log = LogFactory.getLog(PxController.class);

	@Autowired
	private PxService pxService;

	@RequestMapping("/showJcptpxCpMgeUI")
	public ModelAndView showComPositionMgeUI() {

		return new ModelAndView("/busi/px/jcptpx_cp_list");
	}

	@ResponseBody
	@RequestMapping(value = "/loadJcptpxCpList.do")
	public String loadJcptpxCpList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		info.put("cp_area", BusiCommon.getLoginAccountStaffArea(session));
		ExecuteResult rst = pxService.loadJcptpxCpList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showJcptCpSaveUI.do")
	public ModelAndView showJcptCpSaveUI(HttpSession session, @RequestParam(required = true) String px_id) {
		Map info = new HashMap();
		info.put("px_id", px_id);
		return new ModelAndView("/busi/px/jcptpx_cp_edit", info);
	}

	@ResponseBody
	@RequestMapping(value = "/loadJcptpxCpInfo.do")
	public ExecuteResult loadJcptpxCpInfo(HttpSession session, @RequestParam(required = true) String px_id) {

		ExecuteResult rst = pxService.loadJcptpxCpInfo(px_id, BusiCommon.getLoginAccountStaffArea(session));
		return rst;
	}

	@ResponseBody
	@RequestMapping(value = "/uploadCpImg.do")
	public ExecuteResult uploadCpImg(@RequestParam(value = "cpImg", required = true) CommonsMultipartFile file,
			@RequestParam(required = true) String itemId, @RequestParam(required = true) String px_id,
			HttpSession session) {

		ExecuteResult rst = new ExecuteResult();
		try {
			String areaCode = BusiCommon.getLoginAccountStaffArea(session);
			rst = pxService.uploadCpImg(px_id, areaCode, itemId, file);
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

	@ResponseBody
	@RequestMapping(value = "/saveNdzj.do")
	public ExecuteResult saveNdzj(@RequestParam(required = true) String cpNdzj, String px_id, String cp_id,
			HttpSession session) {

		ExecuteResult rst = new ExecuteResult();
		try {
			String areaCode = BusiCommon.getLoginAccountStaffArea(session);
			if (!FrameTool.isEmpty(cp_id)) {
				rst = pxService.saveNdzj(cpNdzj, cp_id);
			} else if (!FrameTool.isEmpty(px_id) && !FrameTool.isEmpty(areaCode)) {
				rst = pxService.saveNdzj(cpNdzj, px_id, areaCode);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

	@ResponseBody
	@RequestMapping(value = "/delImg.do")
	public ExecuteResult delImg(HttpSession session, @RequestParam(required = true) String cpxmAttaId) {

		ExecuteResult rst = pxService.delImg(cpxmAttaId);
		return rst;
	}

	@ResponseBody
	@RequestMapping(value = "/submitCp.do")
	public ExecuteResult submitCp(HttpSession session, @RequestParam(required = true) String cpId) {

		ExecuteResult rst = pxService.submitCp(cpId);
		return rst;
	}

	@RequestMapping("/showXjCpList")
	public ModelAndView showXjCpList() {

		return new ModelAndView("/busi/px/xj_cp_list");
	}

	@ResponseBody
	@RequestMapping(value = "/loadXjCpList")
	public String loadXjCpList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		String areaCode = BusiCommon.getLoginAccountStaffArea(session);
		if (BusiCommon.isCityCode(areaCode)) {
			info.put("city_code", areaCode);
		} else if (BusiCommon.isCountryCode(areaCode)) {
			info.put("country_code", areaCode);
		} else {
			return null;
		}
		ExecuteResult rst = pxService.selectV_bx_jcptcp(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/loadJcptpxCpInfoByCpId.do")
	public ExecuteResult loadJcptpxCpInfoByCpId(HttpSession session, @RequestParam(required = true) String cp_id) {

		ExecuteResult rst = pxService.loadJcptpxCpInfo(cp_id);
		return rst;
	}

}
