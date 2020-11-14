package com.xl.busi.px;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.xl.frame.util.ExecuteResult;

public interface PxService {

	ExecuteResult loadJcptpxCpList(Map<String, Object> params);

	ExecuteResult loadJcptpxCpInfo(String px_id, String cp_area);

	ExecuteResult loadJcptpxCpInfo(String cpId);

	ExecuteResult uploadCpImg(String pxId, String cpArea, String itemId, MultipartFile file);

	ExecuteResult saveNdzj(String ndzj, String cpId);

	ExecuteResult saveNdzj(String ndzj, String pxId, String cpArea);

	ExecuteResult delImg(String cpxmAttaId);

	ExecuteResult submitCp(String cpId);

}
