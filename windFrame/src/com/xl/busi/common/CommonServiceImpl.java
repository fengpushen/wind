package com.xl.busi.common;

import java.util.ArrayList;
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
import com.xl.frame.util.tree.TreeEasyUIJsonMaker;
import com.xl.frame.util.tree.TreeNode;

@Service
public class CommonServiceImpl implements CommonService {

	private static Log log = LogFactory.getLog(CommonServiceImpl.class);

	@Autowired
	private CommonDAO commonDAO;

	@PostConstruct
	public void initBusi() {
		// 初始化地区树
		initAreaTree();
	}

	private void initAreaTree() {
		BaseTree tree = new BaseTree(false, TreeEasyUIJsonMaker.getMaker());
		BaseTree treeBj = new BaseTree(false, TreeEasyUIJsonMaker.getMaker());
		List<Map> areas = commonDAO.selectCom_area();
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		List<TreeNode> bjnodes = new ArrayList<TreeNode>();
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
			node.setOrderNo(node.getId());
			nodeBj.setOrderNo(nodeBj.getId());
			nodes.add(node);
			bjnodes.add(nodeBj);
			if (!"4".equals(areaLevel)) {
				nodeBj = new TreeNode(areaCode + FrameConstant.busi_com_area_bj_add, areaName + "本级", areaCode);
				nodeBj.setOrderNo("0");
				bjnodes.add(nodeBj);
			}
		}
		tree.initTree(nodes);
		treeBj.initTree(bjnodes);
		FrameCache.addTree(FrameConstant.busi_com_area_tree, tree);
		FrameCache.addTree(FrameConstant.busi_com_area_tree_bj, treeBj);
	}

}
