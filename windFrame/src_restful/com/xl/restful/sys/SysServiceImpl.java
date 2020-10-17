package com.xl.restful.sys;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xl.frame.FrameDAO;
import com.xl.frame.util.ExecuteResult;

@Service
public class SysServiceImpl implements SysService {

	private static Log log = LogFactory.getLog(SysServiceImpl.class);

	@Autowired
	private FrameDAO frameDAO;

	public ExecuteResult loadStaffList(Map<String, Object> params) {

		return frameDAO.qryPaginationInfo("selectV_busi_staff_account", params);
	}

}
