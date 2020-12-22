package com.xl.restful.device;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xl.frame.util.ExecuteResult;

@RestController
@RequestMapping("/device")
public class DeviceController {

	private static Log log = LogFactory.getLog(DeviceController.class);

	@Autowired
	private DeviceService deviceService;

	@ResponseBody
	@RequestMapping(value = "/deviceIn.do")
	public ExecuteResult deviceIn(@RequestBody Map<String, Object> map) {

		ExecuteResult rst = null;
		return rst;
	}

}
