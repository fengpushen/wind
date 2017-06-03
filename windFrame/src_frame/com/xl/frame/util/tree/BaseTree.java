package com.xl.frame.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xl.frame.util.FrameTool;

public class BaseTree implements TreeView {

	private List<TreeNode> roots = new ArrayList<TreeNode>();

	private Map<String, TreeNode> nodes = new HashMap<String, TreeNode>();

	public void addNode(TreeNode node) {

		// 如果node有子节点，则需要把子节点也加入到树中来
		if (node.isParent()) {
			for (TreeNode son : node.getSonNodes()) {
				addNode(son);
			}
		}
		if (isTreeNodeExists(node)) {
			TreeNode existsNode = getNode(node.getId());
			// 相同id的节点已经存在，则如果node有pid的话，需要确定pid对应的那个节点包含了这个子节点
			String pid = node.getPid();
			if (!FrameTool.isEmpty(pid)) {
				TreeNode pNode = getNode(pid);
				if (!FrameTool.isEmpty(pNode)) {
					pNode.addSonNode(existsNode);
				}
			}
		} else {
			String id = node.getId();
			if (id == null) {
				throw new NullPointerException();
			}
			nodes.put(id, node);
			String pid = node.getPid();
			if (pid == null) {
				node.setLevel(0);
				roots.add(node);
			} else {
				TreeNode pNode = getNode(pid);
				if (pNode != null) {
					node.setLevel(pNode.getLevel() + 1);
					pNode.addSonNode(node);
				}
			}
		}
	}

	@Override
	public TreeNode getNode(String id) {
		return nodes.get(id);
	}

	@Override
	public List<TreeNode> getRoots() {
		return roots;
	}

	@Override
	public List<TreeNode> getSons(TreeNode treeNode) {
		if (isTreeNodeExists(treeNode)) {
			return treeNode.getSonNodes();
		}
		return null;
	}

	@Override
	public boolean isTreeNodeExists(TreeNode treeNode) {
		return getNode(treeNode.getId()) != null;
	}

	public TreeView getLimitTree(List<TreeNode> visibleNodes) {
		if (visibleNodes == null || visibleNodes.size() == 0) {
			return null;
		}
		BaseTree tree = new BaseTree();
		for (TreeNode node : visibleNodes) {
			if (isTreeNodeExists(node)) {
				tree.addNode(node);
				TreeNode oneNode = node;
				while (oneNode.getPid() != null) {
					TreeNode pNode = getNode(node.getPid());
					TreeNode copyNode = new TreeNode(pNode.getId(), pNode.getName(), pNode.getPid());
					copyNode.addSonNode(oneNode);
					tree.addNode(copyNode);
					oneNode = pNode;
				}
			}
		}
		return tree;
	}

	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (TreeNode node : roots) {
			sb.append(node.toJson());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public List<TreeNode> getSons(String id) {
		TreeNode node = getNode(id);
		if (node != null) {
			return getSons(node);
		}
		return null;
	}

	@Override
	public String toString() {
		return toJson();
	}

}
