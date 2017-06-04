package com.xl.busi;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.ToolForIdcard;
import com.xl.frame.util.tree.TreeNode;
import com.xl.frame.util.tree.TreeView;

public class BusiCommon {

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
	 * �������֤�ϵĳ�����ݺ��Ա��жϵ�ǰ�Ƿ񳬹�����������
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isOverRetirementAgeByIdcardYear(String idcard) {
		return isOverRetirementAge(ToolForIdcard.getAgeFromIdcardYear(idcard), BusiCommon.getSexCodeFromIdcard(idcard));
	}

}
