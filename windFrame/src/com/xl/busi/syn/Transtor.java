package com.xl.busi.syn;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.xl.busi.company.CompanyDAO;
import com.xl.busi.position.PositionDAO;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.URLTool;

@Service
public class Transtor {

	private static Log log = LogFactory.getLog(Transtor.class);

	private String sendUrl = "http://222.240.173.92:7001/hnpes/annualScore/remoteAction!synReceiveData.action";

	@Autowired
	private PositionDAO positionDAO;

	@Autowired
	private CompanyDAO companyDAO;

	/**
	 * 标识是否正在传输
	 */
	private boolean isTransing;

	@Scheduled(cron = "0 0 0 * * ? ")
	public void doTrans() {
		log.info("doTrans bgn:");
		if (!isTransing) {
			synchronized (this) {
				if (!isTransing) {
					isTransing = true;
					try {
						log.info("trans corp:");
						List<Map> datas = companyDAO.selectV_syn_corp();
						while (!FrameTool.isEmpty(datas)) {
							System.out.println("---------"+datas.size());
							List<Map> trans = datas.subList(0, datas.size() > 100 ? 100 : datas.size());
							SynPackage pkg = SynPackage.getCorpInfoPkg();
							pkg.setDatas(trans);
							sendPkg(pkg);
							datas.removeAll(trans);
							System.out.println("---------"+datas.size());
						}
						log.info("trans hire:");
						datas = positionDAO.selectV_syn_hire();
						while (!FrameTool.isEmpty(datas)) {
							System.out.println("---------"+datas.size());
							List<Map> trans = datas.subList(0, datas.size() > 100 ? 100 : datas.size());
							SynPackage pkg = SynPackage.getHireInfoPkg();
							pkg.setDatas(datas);
							sendPkg(pkg);
							datas.removeAll(trans);
							System.out.println("---------"+datas.size());
						}
					} catch (Exception e) {
						log.error("", e);
					} finally {
						isTransing = false;
					}
				}
			}
		}
	}

	private void sendPkg(SynPackage pkg) throws IOException {
		if (pkg != null) {
			String sendContent = FrameTool.toJson(pkg);
			if (!FrameTool.isEmpty(sendContent)) {
				log.info("-------------sendContent:\n");
				log.info(sendContent);
				URL url = new URL(sendUrl);
				String rtn = URLTool.getInstance().postStringAndGetReturnValue(url, sendContent);
				log.info("-------------rtn:\n");
				log.info(rtn);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://222.240.173.92:7001/hnpes/annualScore/remoteAction!queryReceiveDatas.action");
		SynPackage pkg = SynPackage.getQryPkg("busiCorpInfo", "866bdc0f83c645f7b7a8578932a31c92");
		String sendContent = FrameTool.toJson(pkg);
		System.out.println(sendContent);
		String rtn = URLTool.getInstance().postStringAndGetReturnValue(url, sendContent);
		System.out.println(rtn);
	}
}
