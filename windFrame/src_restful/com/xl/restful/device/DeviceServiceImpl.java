package com.xl.restful.device;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xl.frame.FrameDAO;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.ToolForMap;

@Service
public class DeviceServiceImpl implements DeviceService {

	private static Log log = LogFactory.getLog(DeviceServiceImpl.class);

	@Autowired
	private FrameDAO frameDAO;

	/**
	 * 设备进入
	 * 
	 * @param params
	 * @return
	 */
	public ExecuteResult deviceIn(Map<String, Object> params) {
		ExecuteResult rst = new ExecuteResult();
		try {
			boolean isNewDv = true;
			String dvId = (String) params.get("id");
			if (!FrameTool.isEmpty(dvId)) {
				Map dv = frameDAO.anySelectOneTableByPk("BS_DEVICE", dvId);
				// 传入的设备信息中有id，并且根据id从设备表里面可以查询出相应的记录来，说明不是新入网的设备
				if (!FrameTool.isEmpty(dv)) {
					isNewDv = false;
					ToolForMap.lowerKey(dv);
					// 老设备就用本次进入的参数将存档的设备参数更新，但是不要更新关联的账户信息
					params.remove("related_account");
					frameDAO.anyUpdateByPk("BS_DEVICE", params, dvId);
				}
			}
			if (isNewDv) {
				dvId = FrameTool.getUUID();
				params.put("id", dvId);
				frameDAO.anyInsert("BS_DEVICE", params);
			}

			rst.setSucc(true);
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

}
