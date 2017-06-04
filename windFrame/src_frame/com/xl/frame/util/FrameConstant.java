package com.xl.frame.util;

public final class FrameConstant {

	public static final String frame_login_session_key = "accountInfo";

	public static final String frame_menu_base_tree = "frame_menu_base_tree";

	/**
	 * 超级管理员的账号id
	 */
	public static final String busi_com_super_admin_account_id = "10001";

	/**
	 * 超级管理员的后台用户id
	 */
	public static final String busi_com_super_admin_staff_id = "10001";

	// 全国地区树
	public static final String busi_com_area_tree = "busi_com_area_tree";

	// 带本级的全国地区树
	public static final String busi_com_area_tree_bj = "busi_com_area_tree_bj";

	// 本级行政区划后附加的字符
	public static final String busi_com_area_bj_add = "BJ";

	// 行政区划级别后附加的字符
	public static final String busi_com_area_level_add = "LEVEL";

	// 登录用户类型：人社用户
	public static final String busi_user_kind_rs = "0";

	// 登录用户类型：招聘单位用户
	public static final String busi_user_kind_com = "1";

	// 登录用户类型：个人用户
	public static final String busi_user_kind_hr = "2";

	// 通用布朗值：真
	public static final String busi_com_boolean_true = "1";

	// 通用布朗值：假
	public static final String busi_com_boolean_false = "0";

	// 企业类型用户的权限
	public static final String[] busi_user_kind_roles_com = { "20000" };

	// 受委托招聘企业类型用户的权限
	public static final String[] busi_user_kind_roles_com_lwzj = { "20000", "role001" };

	/**
	 * 普通招聘企业的type值
	 */
	public static final String busi_company_type_normal = "1";

	/**
	 * 劳务中介企业的type值
	 */
	public static final String busi_company_type_lwzj = "2";

	/**
	 * 职业介绍企业的type值
	 */
	public static final String busi_company_type_zyjs = "3";

	/**
	 * 委托招聘企业的type值
	 */
	public static final String busi_company_type_wtzp = "4";

	public static final String busi_default_date_style = "yyyy-MM-dd";

	/**
	 * 男性码值
	 */
	public static final String busi_sex_code_male = "1";

	/**
	 * 女性码值
	 */
	public static final String busi_sex_code_female = "2";
}
