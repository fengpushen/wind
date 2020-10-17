package com.xl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xl.frame.util.FrameTool;

public class Tree {

	private List<TreeNode> roots = new ArrayList<TreeNode>();

	private Map<String, TreeNode> nodes = new HashMap<String, TreeNode>();

	public void addTreeNode(TreeNode node) {
		if (isTreeNodeExists(node)) {
			return;
		}
		if (node.isRoot()) {
			roots.add(node);
		} else {
			TreeNode fnode = nodes.get(node.getPid());
			if (fnode == null) {
				return;
			}
			fnode.addChild(node);
		}
		nodes.put(node.getId(), node);
	}

	public boolean isTreeNodeExists(TreeNode treeNode) {
		if (FrameTool.isEmpty(treeNode)) {
			return false;
		}
		String id = treeNode.getId();
		if (FrameTool.isEmpty(id)) {
			return false;
		}
		return isTreeNodeExists(id);

	}

	public boolean isTreeNodeExists(String id) {
		return nodes.containsKey(id);
	}
}
