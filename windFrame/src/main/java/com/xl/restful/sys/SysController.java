package com.xl.restful.sys;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xl.frame.util.CodeInfo;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCode;

@RestController
@RequestMapping("/sys")
public class SysController {

	private static Log log = LogFactory.getLog(SysController.class);

	@Autowired
	private SysService sysService;

	@ResponseBody
	@RequestMapping(value = "/loadStaffList.do")
	public ExecuteResult loadStaffList(@RequestBody Map<String, Object> map) {

		ExecuteResult rst = sysService.loadStaffList(map);
		return rst;
	}

	@ResponseBody
	@RequestMapping(value = "/loadCodeInit.do")
	public Collection<CodeInfo> loadCodeInit(@RequestBody CodeInfo info) {

		return FrameCode.loadCodes(info.getItem(), info.getCode());
	}

	@ResponseBody
	@RequestMapping(value = "/loadCode.do")
	public CodeInfo loadCode(@RequestBody CodeInfo info) {

		return FrameCode.loadCode(info.getItem(), info.getCode());
	}

}
