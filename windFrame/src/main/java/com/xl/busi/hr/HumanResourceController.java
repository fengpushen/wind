package com.xl.busi.hr;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
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
		Map trans = new HashMap();
		trans.put("LD_TYPE", "1");
		return new ModelAndView("/busi/hr/hr_list", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/loadHrList.do")
	public Object loadHrList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		String hj_area = (String) info.get("HJ_AREA");
		if (FrameTool.isEmpty(hj_area)) {
			info.put("HJ_AREA", BusiCommon.getLoginAccountStaffArea(session));

		}
		BusiCommon.dealAreaBj(info, "HJ_AREA");
		ExecuteResult rst = humanResourceService.loadHrList(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return map;
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/searchHrListCom.do")
	public String searchHrListCom(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		String hj_area = (String) info.get("HJ_AREA");
		if (!FrameTool.isEmpty(hj_area)) {
			BusiCommon.dealAreaBj(info, "HJ_AREA");
		}

		ExecuteResult rst = humanResourceService.searchHrListCom(info, BusiCommon.getLoginAccountBusiId(session));
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
	@RequestMapping(value = "/saveHrInfo.do", produces = "text/plain;charset=UTF-8")
	public String saveHrInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = humanResourceService.saveHrInfo(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), BusiCommon.getLoginAccountStaffArea(session), info);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/loadHrInfo.do", produces = "text/plain;charset=UTF-8")
	public String loadHrInfo(@RequestParam(required = true) String hr_id) {

		ExecuteResult rst = humanResourceService.loadHrInfo(hr_id);
		return FrameTool.toJson(rst);
	}

	@RequestMapping(value = "/showHrInfoMdy.do")
	public ModelAndView showHrInfoMdy(@RequestParam(required = true) String hr_id) {

		Map trans = null;
		ExecuteResult rst = humanResourceService.loadHrInfo(hr_id);
		if (rst.isSucc()) {
			trans = (Map) rst.getInfoOne("hrInfo");
			List<Map> wantTrainTypes = (List<Map>) trans.get("wantTrainTypes");
			trans.put("wantTrainTypesJson", FrameTool.toJson(wantTrainTypes));

			List<Map> cbxxs = (List<Map>) trans.get("cbxxs");
			trans.put("cbxxsJson", FrameTool.toJson(cbxxs));
		}
		return new ModelAndView("/busi/hr/hr_info_mdy", trans);
	}

	@RequestMapping(value = "/showHrSearch.do")
	public ModelAndView showHrSearch() {
		return new ModelAndView("/busi/hr/hr_search");
	}

	@ResponseBody
	@RequestMapping(value = "/delHrInfo.do", produces = "text/plain;charset=UTF-8")
	public String delHrInfo(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object delIds = info.get("ids[]");
			if (!FrameTool.isEmpty(delIds)) {
				rst = humanResourceService.delHrInfo(FrameTool.getStringArray(delIds),
						BusiCommon.getLoginAccountId(session), BusiCommon.getLoginAccountStaffArea(session));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/delJobNojobInfo.do", produces = "text/plain;charset=UTF-8")
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

	@RequestMapping("/showHrInfoDetailCom")
	public ModelAndView showHrInfoDetailCom(HttpSession session, @RequestParam(required = true) String hr_id) {
		Map hrInfo = null;
		try {
			hrInfo = humanResourceService.getHrInfoCom(BusiCommon.getLoginAccountBusiId(session), hr_id);
		} catch (SQLException e) {
			log.error("showHrInfoDetailCom", e);
		}
		return new ModelAndView("/busi/hr/hr_info_detail", hrInfo);
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
	@RequestMapping(value = "/saveJobInfo.do", produces = "text/plain;charset=UTF-8")
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
	@RequestMapping(value = "/comHirePerson.do", produces = "text/plain;charset=UTF-8")
	public String comHirePerson(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Map<String, Object> job = (Map<String, Object>) info.get("job");
			rst = humanResourceService.comHirePerson(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountBusiId(session), (String) job.get("HR_ID"), (String) job.get("JOB_TIME"),
					(String) job.get("sy_month"), info);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/saveNOjobInfo.do", produces = "text/plain;charset=UTF-8")
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
	@RequestMapping(value = "/bindHr.do", produces = "text/plain;charset=UTF-8")
	public String bindHr(@RequestParam(required = true) String idcard, @RequestParam(required = true) String hr_name) {

		ExecuteResult rst = humanResourceService.bindHr(idcard, hr_name);
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/saveHrInfoSelfMo.do", produces = "text/plain;charset=UTF-8")
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

	@ResponseBody
	@RequestMapping(value = "/updateHrInfoStaff.do", produces = "text/plain;charset=UTF-8")
	public String updateHrInfoStaff(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			rst = humanResourceService.updateHrInfoStaff(BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), BusiCommon.getLoginAccountStaffArea(session), info);
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

		Map trans = null;
		ExecuteResult rst = humanResourceService.loadHrInfo(hr_id);
		if (rst.isSucc()) {
			trans = (Map) rst.getInfoOne("hrInfo");
			List<Map> needServices = (List<Map>) trans.get("needServices");
			trans.put("needServicesJson", FrameTool.toJson(needServices));
			return new ModelAndView("/busi/mobile/hr/mo_hr_info", trans);
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

	@RequestMapping("/dwnBatchTemplate.do")
	public ResponseEntity<byte[]> dwnBatchTemplate() {
		ResponseEntity<byte[]> rst = null;
		try {
			rst = BusiCommon.getFileDwnResponseEntity(BusiCommon.getFullPathOfHrImpTemplate(), "劳动力资源信息导入模板.xls");
		} catch (IOException e) {
			log.error("download", e);
		}
		return rst;
	}

	@RequestMapping("/showHrImpUI.do")
	public ModelAndView showHrImpUI() {

		return new ModelAndView("/busi/hr/hr_imp");
	}

	@ResponseBody
	@RequestMapping(value = "/batchImpHrInfo.do", produces = "text/plain;charset=UTF-8")
	public String batchImpHrInfo(@RequestParam(value = "hrImpFile", required = true) CommonsMultipartFile file,
			HttpSession session) {

		ExecuteResult rst = new ExecuteResult();
		try {
			// 批量文件上传后先生成处理批次号，然后把文件转存到临时文件夹中，再调用业务处理函数进行处理
			String batchId = FrameTool.getUUID();
			String path = BusiCommon.getFullPathOfTempDir() + batchId + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			File newFile = new File(path);
			file.transferTo(newFile);
			rst = humanResourceService.batchImpHrInfo(batchId, newFile, BusiCommon.getLoginAccountId(session),
					BusiCommon.getLoginAccountKind(session), BusiCommon.getLoginAccountStaffArea(session));
		} catch (IOException e) {
			log.error("batchImpHrInfo", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/dwnBatchImpError.do")
	public ResponseEntity<byte[]> dwnBatchImpError(@RequestParam(required = true) String batch_id) {
		ResponseEntity<byte[]> rst = null;
		try {
			rst = BusiCommon.getFileDwnResponseEntity(humanResourceService.getBatchImpHrErrorFilePath(batch_id),
					"劳动力资源信息导入模板.xls");
		} catch (IOException e) {
			log.error("download", e);
		}
		return rst;
	}

	@RequestMapping("/showHrPositionMatchUI")
	public ModelAndView showHrPositionMatchUI() {
		return new ModelAndView("/busi/hr/hr_position_match_list");
	}

	@ResponseBody
	@RequestMapping(value = "/loadHrPositionMatchList.do")
	public String loadHrPositionMatchList(HttpSession session, HttpServletRequest request) {

		Map<String, Object> info = FrameTool.getRequestParameterMap(request);
		String hj_area = (String) info.get("HJ_AREA");
		if (FrameTool.isEmpty(hj_area)) {
			info.put("HJ_AREA", BusiCommon.getLoginAccountStaffArea(session));
		}
		BusiCommon.dealAreaBj(info, "HJ_AREA");
		ExecuteResult rst = humanResourceService.selectV_hr_position(info);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

	@RequestMapping("/showHrListUI")
	public ModelAndView showHrListUI() {
		Map trans = new HashMap();
		trans.put("LD_TYPE", "1");
		return new ModelAndView("/busi/hr/hr_list_qry", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/attentionHrs.do", produces = "text/plain;charset=UTF-8")
	public String attentionHrs(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object hrIds = info.get("ids[]");
			if (!FrameTool.isEmpty(hrIds)) {
				rst = humanResourceService.attentionHrs(BusiCommon.getLoginAccountBusiId(session),
						FrameTool.getStringArray(hrIds));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/unattentionHrs.do", produces = "text/plain;charset=UTF-8")
	public String unattentionHrs(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object hrIds = info.get("ids[]");
			if (!FrameTool.isEmpty(hrIds)) {
				rst = humanResourceService.unattentionHrs(FrameTool.getStringArray(hrIds));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@ResponseBody
	@RequestMapping(value = "/turnRegularEmployees.do", produces = "text/plain;charset=UTF-8")
	public String turnRegularEmployees(HttpSession session, HttpServletRequest request) {

		ExecuteResult rst = new ExecuteResult();
		try {
			Map<String, Object> info = FrameTool.getRequestParameterMap(request);
			Object hrIds = info.get("ids[]");
			if (!FrameTool.isEmpty(hrIds)) {
				rst = humanResourceService.turnRegularEmployees(FrameTool.getStringArray(hrIds));
			}
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showHrQuitUI")
	public ModelAndView showHrQuitUI(@RequestParam(required = true) String hire_id) {
		Map trans = humanResourceService.getHireHrInfo(hire_id);
		trans.put("hire_id", hire_id);
		return new ModelAndView("/busi/hr/hr_quit_reg", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/quitHire.do", produces = "text/plain;charset=UTF-8")
	public String quitHire(HttpSession session, HttpServletRequest request,
			@RequestParam(required = true) String hire_id, @RequestParam(required = true) String quit_time,
			@RequestParam(required = true) String quit_reason) {

		ExecuteResult rst = new ExecuteResult();
		try {
			rst = humanResourceService.quitHire(BusiCommon.getLoginAccountId(session), hire_id, quit_time, quit_reason);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("error", e);
		}
		return FrameTool.toJson(rst);
	}

	@RequestMapping("/showHrJobTjUI.do")
	public ModelAndView showHrJobTjUI(HttpSession session) {
		String area = BusiCommon.getLoginAccountStaffArea(session);
		String area_level = BusiCommon.getAreaDownLevel(area);
		Map trans = new HashMap();
		trans.put("area_scope", area);
		trans.put("area_level", area_level);
		trans.put("area_scope_name", BusiCommon.getAreaName(area));
		return new ModelAndView("/busi/center/center_hr_job_tj", trans);
	}
	
	@RequestMapping("/showHrChangeTjUI.do")
	public ModelAndView showHrChangeTjUI(HttpSession session) {
		String area = BusiCommon.getLoginAccountStaffArea(session);
		String area_level = BusiCommon.getAreaDownLevel(area);
		Map trans = new HashMap();
		trans.put("area_scope", area);
		trans.put("area_level", area_level);
		trans.put("area_scope_name", BusiCommon.getAreaName(area));
		return new ModelAndView("/busi/center/center_hr_change_tj", trans);
	}

	@ResponseBody
	@RequestMapping(value = "/loadHrJobTjList.do")
	public String loadHrJobTjList(@RequestParam(required = true) String area_scope,
			@RequestParam(required = true) String area_level, String area_type, String sort, String order) {

		ExecuteResult rst = humanResourceService.loadHrJobTjList(area_scope, area_level, area_type, sort, order);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/loadHrChangeTjList.do")
	public String loadHrChangeTjList(@RequestParam(required = true) String area_scope,
			@RequestParam(required = true) String area_level, String area_type, String sort, String order) {

		ExecuteResult rst = humanResourceService.loadHrChangeTjList(area_scope, area_level, area_type, sort, order);
		if (rst.isSucc()) {
			Map map = (Map) rst.getInfoOne("info");
			return FrameTool.toJson(map);
		}
		return null;
	}

}
