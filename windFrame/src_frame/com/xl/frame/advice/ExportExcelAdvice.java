package com.xl.frame.advice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.xl.busi.BusiCommon;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.ToolForExcel;
import com.xl.frame.util.ToolForFile;

@ControllerAdvice
//@EnableWebMvc
// TODO:这个advice一直没有配置生效，直到加了一个@EnableWebMvc的注释才生效，不知道为什么
public class ExportExcelAdvice implements ResponseBodyAdvice {

	@Override
	// TODO:作为框架里的类，直接引用了业务里的类来获取输出文件的地址，这个需要改过来
	public Object beforeBodyWrite(Object arg0, MethodParameter arg1, MediaType arg2, Class arg3, ServerHttpRequest arg4,
			ServerHttpResponse arg5) {

		String rst = arg0.toString();
		if (arg0 instanceof Map) {
			Map map = (Map) arg0;
			if (!FrameTool.isEmpty(map) && map.containsKey("rows")) {
				List<Map> rows = (List<Map>) map.get("rows");
				if (!FrameTool.isEmpty(rows)) {
					List<String> keys = new ArrayList<String>();
					keys.add("P_WORK_AREA_NAME");
					keys.add("END_TIME");
					try {
						ToolForExcel.makeExlFile(getExpFile(), rows, keys);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("-----------------------");
		System.out.println("arg0:" + arg0);
		System.out.println("rst:" + rst);
		System.out.println("-----------------------");
		return arg0;
	}

	@Override
	public boolean supports(MethodParameter methodparameter, Class class1) {

		return true;
	}

	private File getExpFile() throws IOException {
		return getExpFile(null);
	}

	private File getExpFile(String shortName) throws IOException {
		String fullName = getExpFileFullName(shortName);
		File f = new File(fullName);
		ToolForFile.makeSureFileExists(f);
		return f;
	}

	private String getExpFileFullName(String shortName) {
		if (FrameTool.isEmpty(shortName)) {
			shortName = "export.xls";
		}
		return BusiCommon.getTempFileFullName(shortName);
	}

}
