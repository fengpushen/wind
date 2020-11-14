package com.xl.busi.px;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xl.busi.BusiCommon;
import com.xl.frame.FrameDAO;
import com.xl.frame.util.ExecuteResult;
import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.ToolForFile;

/**
 * @author Administrator
 *
 */
@Service
public class PxServiceImpl implements PxService {

	private static Log log = LogFactory.getLog(PxServiceImpl.class);

	@Autowired
	private FrameDAO frameDAO;

	@Autowired
	private PxDAO pxDAO;

	public ExecuteResult loadJcptpxCpList(Map<String, Object> params) {

		return frameDAO.qryPaginationInfo("selectBs_jcptpx_cp", params);
	}

	/**
	 * 根据评选id和参评地区两个属性加载某个地区的所有参评信息
	 * 
	 * @param px_id
	 * @param cp_area
	 * @return
	 */
	public ExecuteResult loadJcptpxCpInfo(String px_id, String cp_area) {

		ExecuteResult rst = new ExecuteResult();
		if (!FrameTool.isEmpty(px_id) && !FrameTool.isEmpty(cp_area)) {
			try {

				// 查询出参评信息，包括评选基本信息
				Map cpInfo = getCpInfo(px_id, cp_area);
				if (!FrameTool.isEmpty(cpInfo)) {
					Map params = new HashMap();
					params.put("px_id", px_id);
					List pxItemList = pxDAO.selectBs_jcptpx_xm(params);

					if (!FrameTool.isEmpty(pxItemList)) {
						List<Map> pxItemTree = new ArrayList<Map>();
						Map<String, Map> pxBigItems = new HashMap<String, Map>();
						for (Object o : pxItemList) {
							Map pxItem = (Map) o;
							String p_item_id = (String) pxItem.get("P_ITEM_ID");
							if ("0".equals(p_item_id)) {
								pxItem.put("subItems", new ArrayList());
								pxItemTree.add(pxItem);
								String item_id = (String) pxItem.get("ITEM_ID");
								pxBigItems.put(item_id, pxItem);
							} else {
								Map pxParentItem = pxBigItems.get(p_item_id);
								List<Map> subItems = (List) pxParentItem.get("subItems");
								subItems.add(pxItem);
							}
						}
						cpInfo.put("pxItems", pxItemTree);
					}

					String cp_id = (String) cpInfo.get("CP_ID");
					if (!FrameTool.isEmpty(cp_id)) {
						params.clear();
						params.put("cp_id", cp_id);
						List cpItemList = pxDAO.selectBs_jcptpxcp_xm_att(params);
						// 参评项目如果不为空，则要将数据库查询记录转换为java存放方式，数据库查询是将附件和参评信息同时查出来的，这里需要将附件作为
						// 参评项目的子项存放
						if (!FrameTool.isEmpty(cpItemList)) {
							Map<String, Map> cpItemMap = new HashMap<String, Map>();
							// 每一条查询的记录里面都有参评信息和附件信息，这里分两个map存放，信息项目照理说是要分开的，但是为了方便就偷懒不分开了
							for (Object o : cpItemList) {
								Map record = (Map) o;
								Map att = new HashMap();
								// 当本条记录是有附件的时候，需要将附件加入到附件列表中
								if (!FrameTool.isEmpty(record.get("CP_XM_ATTA_ID"))) {
									att.putAll(record);
								}
								String cp_item_id = (String) record.get("CP_ITEM_ID");
								Map cpItem = null;

								// 如果参评项目已经存在，则直接取出参评项目而不用本次查询出来的参评项目，如果不存在，则在参评项目中增加一个附件列表的属性，并存放到参评项目映射表中
								if (cpItemMap.containsKey(cp_item_id)) {
									cpItem = cpItemMap.get(cp_item_id);
								} else {
									List atts = new ArrayList();
									record.put("atts", atts);
									cpItemMap.put(cp_item_id, record);
									cpItem = record;
								}
								// 将这一条查询出来的附件信息插入到参评项目的附件列表中
								List atts = (List) cpItem.get("atts");

								// 当本条记录是有附件的时候，需要将附件加入到附件列表中
								if (!FrameTool.isEmpty(record.get("CP_XM_ATTA_ID"))) {
									atts.add(att);
								}

							}
							// 将参评项目加入到对应的评选项目属性中
							for (Map cpItem : cpItemMap.values()) {
								String px_item_id = (String) cpItem.get("PX_ITEM_ID");
								for (Object o : pxItemList) {
									Map pxItem = (Map) o;
									if (pxItem.get("PX_ITEM_ID").equals(px_item_id)) {
										pxItem.put("cpItem", cpItem);
									}
								}
							}
						}
					}
					rst.addInfo("cpInfo", cpInfo);
					rst.setSucc(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rst;
	}

	public ExecuteResult loadJcptpxCpInfo(String cpId) {
		ExecuteResult rst = new ExecuteResult();
		Map cpInfo = getCpInfo(cpId);

		if (FrameTool.isEmpty(cpInfo)) {
			rst.setDefaultValue("参评信息错误");
			return rst;
		}
		String pxId = (String) cpInfo.get("PX_ID");
		String areaCode = (String) cpInfo.get("AREA_CODE");
		rst = loadJcptpxCpInfo(pxId, areaCode);
		return rst;
	}

	/**
	 * 上传参评照片
	 * 
	 * @param pxId
	 * @param cpArea
	 * @param itemId
	 * @param file
	 * @return
	 */
	public ExecuteResult uploadCpImg(String pxId, String cpArea, String itemId, MultipartFile file) {
		ExecuteResult rst = new ExecuteResult();

		// 一个地区在做上传照片的时候有可能还未参评，所以这里无论是否参评，都先做参评操作，保证一个地区一定是参评了的才上传照片
		ExecuteResult cpRst = cp(pxId, cpArea);
		if (!cpRst.isSucc()) {
			return cpRst;
		}

		try {
			Map cpInfo = (Map) cpRst.getInfoOne("cpInfo");
			String cpId = (String) cpInfo.get("CP_ID");
			String pxItemId = getPxItemId(pxId, itemId);
			String cpItemId = getCpItemId(cpId, pxItemId);

			// 1、上传附件
			String orgFileName = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(orgFileName);
			String newFileName = FrameTool.generatUniqueString();
			String newFileFullName = newFileName + "." + extension;

			String year = (String) cpInfo.get("PX_YEAR");

			String imgDir = getImgDir((String) cpInfo.get("PX_YEAR"), (String) cpInfo.get("AREA_CODE"), pxItemId);

			String path = FrameCache.getFrameConfig("file_root") + imgDir + "/" + newFileFullName;
			File newFile = new File(path);
			ToolForFile.makeSureFileExists(newFile);
			file.transferTo(newFile);
			String fileType = ToolForFile.getFileTypeByFile(newFile);
			if(!"jpg".equals(fileType) && !"png".equals(fileType)) {
				rst.setDefaultValue("只能上传jpg或png格式文件");
				ToolForFile.delete(newFile);
				return rst;
			}

			// 2、插入附件表
			Map params = new HashMap();
			params.put("atta_id", newFileName);
			params.put("atta_show_name", orgFileName);
			params.put("atta_suff", extension);
			params.put("atta_dire", imgDir);
			params.put("atta_name", newFileFullName);
			frameDAO.anyInsert("com_atta", params);

			// 3、插入参评附件表
			params.clear();
			params.put("cp_xm_atta_id", FrameTool.generatUniqueString());
			params.put("cp_item_id", cpItemId);
			params.put("atta_id", newFileName);
			frameDAO.anyInsert("bx_jcptcp_xm_atta", params);
			rst.setSucc(true);
		} catch (Exception e) {
			log.error("", e);
		}

		return rst;
	}

	/*
	 * 根据参评id更新年度总结
	 */
	public ExecuteResult saveNdzj(String ndzj, String cpId) {
		ExecuteResult rst = new ExecuteResult();

		try {
			Map cpInfo = getCpInfo(cpId);

			if (FrameTool.isEmpty(cpInfo)) {
				rst.setDefaultValue("参评信息错误");
				return rst;
			}
			if (!canMdyCpInfo(cpInfo)) {
				rst.setDefaultValue("提交后不能再修改参评信息");
				return rst;
			}
			if (ndzj != null && ndzj.length() > 2000) {
				rst.setDefaultValue("年度总结不能超过2000个汉字");
				return rst;
			}

			Map params = new HashMap();
			params.put("cp_ndzj", ndzj);
			frameDAO.anyUpdateByPk("bx_jcptcp", params, cpId);

			rst.setSucc(true);
		} catch (Exception e) {
			log.error("", e);
		}

		return rst;
	}

	/*
	 * 根据评选id，参评地区编码更新年度总结
	 */
	public ExecuteResult saveNdzj(String ndzj, String pxId, String cpArea) {
		ExecuteResult cpRst = cp(pxId, cpArea);
		if (!cpRst.isSucc()) {
			return cpRst;
		}
		Map cpInfo = (Map) cpRst.getInfoOne("cpInfo");
		String cpId = (String) cpInfo.get("CP_ID");
		return saveNdzj(ndzj, cpId);
	}

	/*
	 * 根据参评项目附件表的主键，删除上传的附件信息
	 */
	public ExecuteResult delImg(String cpxmAttaId) {
		ExecuteResult rst = new ExecuteResult();

		try {
			Map cpItemAtta = getCpItemAtta(cpxmAttaId);
			if (!FrameTool.isEmpty(cpItemAtta)) {
				frameDAO.anyDeleteByPk("bx_jcptcp_xm_atta", cpxmAttaId);
				frameDAO.anyDeleteByPk("com_atta", (String) cpItemAtta.get("ATTA_ID"));
				String path = (String) cpItemAtta.get("ATTA_PATH");
				String fullPath = FrameCache.getFrameConfig("file_root") + path;
				ToolForFile.delete(fullPath);
				rst.setSucc(true);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return rst;
	}

	/*
	 * 参评信息提交评审
	 */
	public ExecuteResult submitCp(String cpId) {
		ExecuteResult rst = new ExecuteResult();

		ExecuteResult cpInfoLoadRst = loadJcptpxCpInfo(cpId);
		if (!cpInfoLoadRst.isSucc()) {
			rst.setDefaultValue("参评信息错误");
			return rst;
		}

		try {
			Map cpInfo = (Map) cpInfoLoadRst.getInfoOne("cpInfo");
			if (!canMdyCpInfo(cpInfo)) {
				rst.setDefaultValue("参评状态错误，当前状态是[" + cpInfo.get("CP_STATUS_NAME") + "]不能提交");
				return rst;
			}
			if (FrameTool.isEmpty(cpInfo.get("CP_NDZJ"))) {
				rst.setDefaultValue("年度总结必须填写");
				return rst;
			}
			
			//循环判断该传的图片是否都按照要求传上
			List pxItems = (List) cpInfo.get("pxItems");
			for (Object o : pxItems) {
				Map pxItem = (Map) o;
				List subItems = (List) pxItem.get("subItems");

				for (Object oo : subItems) {
					Map subItem = (Map) oo;
					Map cpItem = (Map) subItem.get("cpItem");
					List atts = (List) cpItem.get("atts");
					int max = ((BigDecimal) subItem.get("PHOTO_MAX")).intValue();
					int min = ((BigDecimal) subItem.get("PHOTO_MIN")).intValue();
					if (atts.size() > max || atts.size() < min) {
						String parentItemName = (String) pxItem.get("ITEM_NAME");
						String subItemName = (String) subItem.get("ITEM_NAME");
						String itemTxsm = (String) subItem.get("ITEM_TXSM");
						rst.setDefaultValue(parentItemName + "->" + subItemName + ":" + itemTxsm);
						return rst;
					}
				}
			}
			Map params = new HashMap();
			params.put("CP_STATUS", "01");
			frameDAO.anyUpdateByPk("bx_jcptcp", params, cpId);
			rst.setSucc(true);
		} catch (Exception e) {
			log.error("", e);
		}

		return rst;
	}

	private Map getCpItemAtta(String cpxmAttaId) {
		Map cpItemAtt = null;
		Map params = new HashMap();
		params.put("cp_xm_atta_id", cpxmAttaId);
		List cpItemAtts = pxDAO.selectBs_jcptpxcp_xm_att(params);
		if (!FrameTool.isEmpty(cpItemAtts)) {
			cpItemAtt = (Map) cpItemAtts.get(0);
		}
		return cpItemAtt;
	}

	private String getImgDir(String year, String area, String pxItemId) {
		return BusiCommon.getJcptpxFileDir() + "/" + year + "/" + area + "/" + pxItemId;
	}

	private String getCpItemId(String cpId, String pxItemId) {
		String cpItemId = null;

		Map params = new HashMap();
		params.put("cp_id", cpId);
		params.put("px_item_id", pxItemId);

		List cpItemAtts = pxDAO.selectBs_jcptpxcp_xm_att(params);
		if (!FrameTool.isEmpty(cpItemAtts)) {
			Map cpItemAtt = (Map) cpItemAtts.get(0);
			cpItemId = (String) cpItemAtt.get("CP_ITEM_ID");
		}
		return cpItemId;
	}

	private String getPxItemId(String pxId, String itemId) {
		String pxItemId = null;
		Map params = new HashMap();
		params.put("px_id", pxId);
		params.put("item_id", itemId);

		List pxItems = pxDAO.selectBs_jcptpx_xm(params);
		if (!FrameTool.isEmpty(pxItems)) {
			Map pxItem = (Map) pxItems.get(0);
			pxItemId = (String) pxItem.get("PX_ITEM_ID");
		}
		return pxItemId;
	}

	/**
	 * 根据评选id和参评地区编码获取参评信息
	 * 
	 * @param pxId
	 * @param cpArea
	 * @return
	 */
	private Map getCpInfo(String pxId, String cpArea) {
		Map params = new HashMap();
		params.put("cp_area", cpArea);
		params.put("px_id", pxId);

		Map cpInfo = pxDAO.selectBs_jcptpx_cp(params);
		return cpInfo;
	}

	/**
	 * 根据参评id获取参评信息
	 * 
	 * @param cpId
	 * @return
	 */
	private Map getCpInfo(String cpId) {
		Map params = new HashMap();
		params.put("cp_id", cpId);

		Map cpInfo = pxDAO.selectBs_jcptpx_cp(params);
		return cpInfo;
	}

	/**
	 * 根据传入的参评信息，判断是否已经参评
	 * 
	 * @param cpInfo
	 * @return
	 */
	private boolean isCped(Map cpInfo) {
		String cpId = (String) cpInfo.get("CP_ID");
		return !FrameTool.isEmpty(cpId);
	}

	/**
	 * 根据传入的参评信息，判断是否可以参评
	 * 
	 * @param cpInfo
	 * @return
	 */
	private boolean canCp(Map cpInfo) {
		String isAdd = (String) cpInfo.get("IS_ADD");
		return FrameConstant.busi_com_boolean_true.equals(isAdd);
	}

	/**
	 * 判断是否可以修改参评信息
	 * 
	 * @param cpInfo
	 * @return
	 */
	private boolean canMdyCpInfo(Map cpInfo) {
		return "00".equals(cpInfo.get("CP_STATUS"));
	}

	/**
	 * 参评
	 * 
	 * @param pxId
	 * @param cpArea
	 * @return
	 */
	private ExecuteResult cp(String pxId, String cpArea) {
		ExecuteResult rst = new ExecuteResult();

		if (FrameTool.isEmpty(pxId) || FrameTool.isEmpty(cpArea)) {
			rst.setDefaultValue("参数错误");
			return rst;
		}

		if (!BusiCommon.isStreetCode(cpArea) && !BusiCommon.isVillageCode(cpArea)) {
			rst.setDefaultValue("社区或街道才能参评");
			return rst;
		}

		// 获取当前地区的参评信息，如果已经参评，直接返回成功，否则看本次评选状态是否可以参评，如果不能参评则返回失败
		Map cpInfo = getCpInfo(pxId, cpArea);
		if (isCped(cpInfo)) {
			rst.addInfo("cpInfo", cpInfo);
			rst.setSucc(true);
			return rst;
		}
		if (!canCp(cpInfo)) {
			rst.setDefaultValue("目前已经不能参评");
			return rst;
		}

		// 流程到这里就是没有参评，则增加参评信息，并填写参评项目信息
		Map params = new HashMap();
		String cpId = FrameTool.generatUniqueString();
		params.put("cp_id", cpId);
		params.put("px_id", pxId);
		params.put("area_code", cpArea);

		try {
			frameDAO.anyInsert("BX_JCPTCP", params);
			pxDAO.insertBx_jcptcp_xm(params);
			cpInfo = getCpInfo(pxId, cpArea);
			rst.addInfo("cpInfo", cpInfo);
			rst.setSucc(true);
		} catch (Exception e) {
			rst.setDefaultValue("程序内部错误");
			log.error("", e);
		}
		return rst;
	}

}
