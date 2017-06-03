package com.xl.busi.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xl.frame.util.FrameCache;
import com.xl.frame.util.FrameTool;
import com.xl.frame.util.tree.TreeNode;
import com.xl.frame.util.tree.TreeView;

@Controller
@RequestMapping("/busi/common")
public class CommonController {

	private static Log log = LogFactory.getLog(CommonController.class);

	/**
	 * �첽��������json
	 * 
	 * @param treeName
	 *            ����
	 * @param rootId
	 *            ��������һ���ڵ㿪ʼ��ʾ
	 * @param limitLevel
	 *            ��ʾ��������һ��
	 * @param id
	 *            Ҫ��ʾ���ӽڵ�ĸ�id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loadTree.do")
	public String loadTree(@RequestParam(required = true) String treeName,
			@RequestParam(required = false) String rootId,
			@RequestParam(required = false, defaultValue = "1000") int limitLevel,
			@RequestParam(required = false) String id) {

		TreeView tree = FrameCache.getTree(treeName);
		String treeJson = "";
		if (tree != null) {
			List<TreeNode> nodes = null;
			// ������rootId, δ����id��˵���ǵ�һ�μ���������ָ����Ҫ���صĸ��ڵ㣬�򽫴˸��ڵ㼰���ӽڵ㷵��
			if (!FrameTool.isEmpty(rootId) && FrameTool.isEmpty(id)) {
				TreeNode node = tree.getNode(rootId);
				if (!FrameTool.isEmpty(node)) {
					List<Map> jsNodes = new ArrayList<Map>();
					Map root = new HashMap();
					root.put("id", node.getId());
					root.put("text", node.getName());
					root.put("state", "open");

					List<Map> jsSonNodes = getTreeNodeMaps(node.getSonNodes(), limitLevel);
					if (!FrameTool.isEmpty(jsSonNodes)) {
						root.put("children", jsSonNodes);
					}
					jsNodes.add(root);
					treeJson = FrameTool.toJson(jsNodes);
				}
			} else {
				if (FrameTool.isEmpty(id)) {
					nodes = tree.getRoots();
				} else {
					nodes = tree.getSons(id);
				}

				if (!FrameTool.isEmpty(nodes)) {
					List<Map> jsNodes = getTreeNodeMaps(nodes, limitLevel);
					if (!FrameTool.isEmpty(jsNodes)) {
						treeJson = FrameTool.toJson(jsNodes);
					}
				}
			}
		}
		return treeJson;
	}

	private List<Map> getTreeNodeMaps(List<TreeNode> nodes, int limitLevel) {

		List<Map> jsNodes = null;
		if (!FrameTool.isEmpty(nodes)) {
			jsNodes = new ArrayList<Map>();
			for (TreeNode node : nodes) {
				Map jsNode = new HashMap();
				jsNode.put("id", node.getId());
				jsNode.put("text", node.getName());
				if (node.isParent() && node.getLevel() < limitLevel) {
					jsNode.put("state", "closed");
				} else {
					jsNode.put("state", "open");
				}
				jsNodes.add(jsNode);
			}
		}
		return jsNodes;
	}
}
