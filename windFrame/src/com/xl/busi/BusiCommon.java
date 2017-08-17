package com.xl.busi;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.ToolForIdcard;
import com.xl.frame.util.tree.TreeNode;
import com.xl.frame.util.tree.TreeView;

public class BusiCommon {

	/**
	 * ϵͳԤ���幩�����ļ��Ĵ��Ŀ¼
	 */
	public static final String download_file_dir = "WEB-INF/views/busi/forDwn/";

	/**
	 * ϵͳ������ʱ�ļ��Ĵ��Ŀ¼
	 */
	public static final String temp_file_dir = "temp/";

	/**
	 * ��ȡ��Ա��������ģ���ȫ·��
	 * 
	 * @return
	 */
	public static String getFullPathOfHrImpTemplate() {
		return System.getProperty("realPath.webcontent") + download_file_dir + "hrImpTemplate.xls";
	}

	/**
	 * ��ȡϵͳ����ʱ��ʱ�ļ���ŵص�ȫ·��
	 * 
	 * @return
	 */
	public static String getFullPathOfTempDir() {
		return System.getProperty("realPath.webcontent") + temp_file_dir;
	}

	public static ResponseEntity<byte[]> getFileDwnResponseEntity(String fileFullPath, String showName)
			throws IOException {

		File file = new File(fileFullPath);
		HttpHeaders headers = new HttpHeaders();
		String fileName = FrameTool.getIso8859Str(showName);

		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		byte[] bs = FileUtils.readFileToByteArray(file);

		headers.setContentLength(bs.length);
		return new ResponseEntity<byte[]>(bs, headers, HttpStatus.OK);
	}

	public static String getRtmpUrl(String host) {
		if (host != null && host.equals(FrameCache.getFrameConfig("ip_inside"))) {
			return FrameCache.getFrameConfig("rtmp_url_inside");
		}
		return FrameCache.getFrameConfig("rtmp_url");
	}

	public static boolean isLogined(HttpSession session) {
		return !FrameTool.isEmpty(getLoginAccount(session));
	}

	public static Map getLoginAccount(HttpSession session) {
		return (Map) session.getAttribute(FrameConstant.frame_login_session_key);
	}

	public static String getLoginAccountId(HttpSession session) {
		return (String) getLoginAccount(session).get("ACCOUNT_ID");
	}

	public static String getLoginAccountKind(HttpSession session) {
		return (String) getLoginAccount(session).get("ACCOUNT_KIND");
	}

	public static String getLoginAccountBusiId(HttpSession session) {
		return (String) getLoginAccount(session).get("BUSI_ID");
	}

	public static Map getLoginAccountStaff(HttpSession session) {
		return (Map) getLoginAccount(session).get("staffInfo");
	}

	public static Map getLoginAccountCom(HttpSession session) {
		return (Map) getLoginAccount(session).get("comInfo");
	}

	public static String getLoginAccountStaffArea(HttpSession session) {
		return (String) getLoginAccountStaff(session).get("AREA_CODE");
	}

	public static boolean isLoginAccountKindRs(HttpSession session) {
		return FrameConstant.busi_user_kind_rs.equals(getLoginAccountKind(session));
	}

	public static boolean isLoginAccountKindHr(HttpSession session) {
		return FrameConstant.busi_user_kind_hr.equals(getLoginAccountKind(session));
	}

	public static boolean isLoginAccountKindCom(HttpSession session) {
		return FrameConstant.busi_user_kind_com.equals(getLoginAccountKind(session));
	}

	public static String getLoginAccountShowName(HttpSession session) {
		if (isLoginAccountKindRs(session)) {
			Map staff = getLoginAccountStaff(session);
			return (String) staff.get("STAFF_NAME");
		}
		if (isLoginAccountKindCom(session)) {
			Map staff = getLoginAccountCom(session);
			return (String) staff.get("C_NAME");
		}
		return "";
	}

	public static TreeNode getAreaTreeNode(String areaCode) {
		TreeView areaTree = FrameCache.getTree(FrameConstant.busi_com_area_tree);
		if (FrameTool.isEmpty(areaTree)) {
			return null;
		}
		return areaTree.getNode(areaCode);
	}

	public static String getAreaName(String areaCode) {
		TreeNode node = getAreaTreeNode(areaCode);
		if (FrameTool.isEmpty(node)) {
			return null;
		}
		return node.getName();
	}

	public static String getAreaLevel(String areaCode) {
		TreeNode node = getAreaTreeNode(areaCode);
		if (FrameTool.isEmpty(node)) {
			return null;
		}
		return Integer.toString(node.getLevel());
	}

	/**
	 * �ж�otherAreaCode�Ƿ�ΪareaCode�ķ�Χ֮��
	 * 
	 * @param areaCode
	 * @param otherAreaCode
	 * @return
	 */
	public static boolean isInScope(String areaCode, String otherAreaCode) {

		if (FrameTool.isEmpty(areaCode) || FrameTool.isEmpty(otherAreaCode)) {
			return false;
		}
		if (otherAreaCode.equals(areaCode)) {
			return true;
		}
		TreeNode node = BusiCommon.getAreaTreeNode(areaCode);
		if (FrameTool.isEmpty(node)) {
			return false;
		}
		return node.hasTheUnbornNode(otherAreaCode);
	}

	/**
	 * �������֤�����ȡϵͳ�е��Ա����
	 * 
	 * @param idcard
	 * @return
	 */
	public static String getSexCodeFromIdcard(String idcard) {
		if (ToolForIdcard.isMale(idcard)) {
			return FrameConstant.busi_sex_code_male;
		} else {
			return FrameConstant.busi_sex_code_female;
		}
	}

	/**
	 * �����ѯ�����еı�������������
	 * 
	 * @param map
	 *            ��ѯ����map
	 * @param key
	 *            ָ����������key
	 */
	public static void dealAreaBj(Map map, String key) {
		if (FrameTool.isEmpty(map) || FrameTool.isEmpty(key)) {
			return;
		}
		String areaCode = (String) map.get(key);
		if (FrameTool.isEmpty(areaCode)) {
			return;
		}
		// ��������еĵ��������Ǳ��������ı������ڲ�ѯ�����м�һ����ʾ�����Ĳ��������ѵ�������ָ�������������
		if (areaCode.endsWith(FrameConstant.busi_com_area_bj_add)) {
			map.put(key + "_" + FrameConstant.busi_com_area_bj_add, FrameConstant.busi_com_area_bj_add);
			areaCode = areaCode.replaceAll(FrameConstant.busi_com_area_bj_add, "");
			map.put(key, areaCode);
		} else {
			// �������벻�Ǳ����ģ�����Ҫ�ڲ�ѯ������������һ����ʾ�����ȼ��Ĳ���
			map.put(key + "_" + FrameConstant.busi_com_area_level_add, BusiCommon.getAreaLevel(areaCode));
		}
	}

	/**
	 * ����������Ա��жϵ�ǰ�Ƿ񳬹�����������
	 * 
	 * @param age
	 * @param sexCode
	 * @return
	 */
	public static boolean isOverRetirementAge(int age, String sexCode) {
		if (FrameConstant.busi_sex_code_female.equals(sexCode)) {
			return age >= 55;
		}
		if (FrameConstant.busi_sex_code_male.equals(sexCode)) {
			return age >= 60;
		}
		throw new RuntimeException("������Ա����");
	}

	/**
	 * �ж��Ƿ�δ��������������
	 * 
	 * @param age
	 * @return
	 */
	public static boolean isTooYoungForWork(int age) {
		return age < 16;
	}
	
	/**
	 * �ж��Ƿ�����̫С���ʺ�¼��ϵͳ
	 * @param age
	 * @return
	 */
	public static boolean isTooYoungForInput(int age){
		return age < 10;
	}

	/**
	 * �������֤�ϵĳ�����ݺ��Ա��жϵ�ǰ�Ƿ񳬹�����������
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isOverRetirementAgeByIdcardYear(String idcard) {
		return isOverRetirementAge(ToolForIdcard.getAgeFromIdcardYear(idcard), BusiCommon.getSexCodeFromIdcard(idcard));
	}

	/**
	 * �������֤�ϵĳ�������ж��Ƿ�δ��������������
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isTooYoungForWorkByIdcardYear(String idcard) {
		return isTooYoungForWork(ToolForIdcard.getAgeFromIdcardYear(idcard));
	}
	
	/**
	 * �������֤�ϵĳ�������ж��Ƿ�δ������¼������
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isTooYoungFoInputByIdcardYear(String idcard) {
		return isTooYoungForInput(ToolForIdcard.getAgeFromIdcardYear(idcard));
	}

}
