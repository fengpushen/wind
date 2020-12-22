package com.xl.busi.syn;

import java.util.List;
import java.util.Map;

import com.xl.frame.util.FrameTool;

public class SynPackage {

	private static final String busi_busiCorpInfo = "busiCorpInfo";

	private String center_system_name = "益阳劳动力信息平台";

	private String login_user = "yyldl";

	private String login_pwd = "9019DC0617314251D8EF97F63BAAAED3";

	private String busi;

	private String serial_num;

	private String aae027 = "430900";

	private List<Map> datas;

	private SynPackage() {
	}

	public String getSerial_num() {
		return serial_num;
	}

	public static SynPackage getCorpInfoPkg() {
		return getPkg("busiCorpInfo");
	}

	public static SynPackage getHireInfoPkg() {
		return getPkg("busiHireInfo");
	}

	public static SynPackage getFairInfoPkg() {
		return getPkg("busiFairInfo");
	}

	public static SynPackage getFairHireInfoPkg() {
		return getPkg("busiFairHireInfo");
	}

	public static SynPackage getQryPkg(String qryBusi, String serialNum) {
		return getPkg(qryBusi, serialNum);
	}

	public void setDatas(List<Map> datas) {
		this.datas = datas;
	}

	private static SynPackage getPkg(String cutBusi) {
		return getPkg(cutBusi, FrameTool.getUUID());
	}

	private static SynPackage getPkg(String cutBusi, String serialNum) {
		SynPackage pkg = new SynPackage();
		pkg.busi = cutBusi;
		pkg.serial_num = serialNum;
		return pkg;
	}

}
