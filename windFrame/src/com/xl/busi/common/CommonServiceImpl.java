package com.xl.busi.common;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameConstant;
import com.xl.frame.util.tree.BaseTree;
import com.xl.frame.util.tree.TreeNode;

@Service
public class CommonServiceImpl implements CommonService {

	private static Log log = LogFactory.getLog(CommonServiceImpl.class);

	@Autowired
	private CommonDAO commonDAO;

	@PostConstruct
	public void initBusi() {
		// ��ʼ��������
		initAreaTree();
	}

	private void initAreaTree() {
		BaseTree tree = new BaseTree();
		BaseTree treeBj = new BaseTree();
		List<Map> areas = commonDAO.selectCom_area();
		for (Map area : areas) {
			String pid = (String) area.get("PCODE");
			String areaCode = (String) area.get("AREA_CODE");
			String areaName = (String) area.get("AREA_NAME");
			String areaLevel = (String) area.get("AREA_LEVEL");
			TreeNode node = null;
			TreeNode nodeBj = null;
			if ("0".equals(pid)) {
				node = new TreeNode(areaCode, areaName);
				nodeBj = new TreeNode(areaCode, areaName);
			} else {
				node = new TreeNode(areaCode, areaName, pid);
				nodeBj = new TreeNode(areaCode, areaName, pid);
			}
			tree.addNode(node);
			treeBj.addNode(nodeBj);
			if (!"4".equals(areaLevel)) {
				nodeBj = new TreeNode(areaCode + FrameConstant.busi_com_area_bj_add, areaName + "����", areaCode);
				treeBj.addNode(nodeBj);
			}
		}
		FrameCache.addTree(FrameConstant.busi_com_area_tree, tree);
		FrameCache.addTree(FrameConstant.busi_com_area_tree_bj, treeBj);
	}

}
