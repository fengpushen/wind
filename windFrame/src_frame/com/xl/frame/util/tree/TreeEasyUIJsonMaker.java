package com.xl.frame.util.tree;

import java.lang.reflect.MalformedParametersException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xl.frame.util.FrameTool;

/**
 * 符合easyUI中树模型的json生成器
 *
 */
public class TreeEasyUIJsonMaker implements TreeJsonMaker {

	private static TreeEasyUIJsonMaker maker;

	public static synchronized TreeEasyUIJsonMaker getMaker() {
		if (maker == null) {
			maker = new TreeEasyUIJsonMaker();
		}
		return maker;
	}

	@Override
	public String getJson(TreeView tree, TreeNode node, int limitLevel) {
		if (!tree.isInited()) {
			throw new RuntimeException(BaseTree.NOT_INITED_TIP);
		}

		if (!tree.isTreeNodeExists(node)) {
			throw new RuntimeException(BaseTree.TREE_NODE_NOT_EXISTS_TIP);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(getTreeNodeBaseJson(node));
		if (node.isParent() && node.getLevel() < limitLevel) {
			List<TreeNode> sons = tree.getSons(node.getId());
			if (!FrameTool.isEmpty(sons)) {
				sb.append(",");
				sb.append("\"children\":");
				sb.append("[");
				for (TreeNode son : sons) {
					sb.append(getJson(tree, son, limitLevel));
					sb.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("]");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String getJson(TreeView tree, int limitLevel) {
		if (!tree.isInited()) {
			throw new RuntimeException(BaseTree.NOT_INITED_TIP);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		List<TreeNode> roots = tree.getRoots();
		if (!FrameTool.isEmpty(roots)) {
			for (TreeNode node : roots) {
				sb.append(getJson(tree, node, limitLevel));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String getJson(TreeView tree, List<String> ids) {
		if (!tree.isInited()) {
			throw new RuntimeException(BaseTree.NOT_INITED_TIP);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		List<TreeNode> roots = tree.getRoots(ids);
		Set<TreeNode> relatedNodes = tree.getRelatedHigherTreeNode(ids);
		if (!FrameTool.isEmpty(roots)) {
			for (TreeNode node : roots) {
				sb.append(getJson(tree, node, relatedNodes));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String getJson(TreeView tree, TreeNode node, TreeNodeFilter filter) {
		if (!tree.isInited()) {
			throw new RuntimeException(BaseTree.NOT_INITED_TIP);
		}

		if (!tree.isTreeNodeExists(node)) {
			throw new RuntimeException(BaseTree.TREE_NODE_NOT_EXISTS_TIP);
		}
		StringBuilder sb = new StringBuilder();
		if (filter.isPass(node)) {
			sb.append(getTreeNodeBaseJson(node));
			if (node.isParent() && filter.isPass(node)) {
				List<TreeNode> sons = tree.getSons(node.getId());
				if (!FrameTool.isEmpty(sons)) {
					boolean hasOne = false;

					for (TreeNode son : sons) {
						if (!hasOne && filter.isPass(son)) {
							sb.append(",");
							sb.append("\"children\":");
							sb.append("[");
							hasOne = true;
							sb.append(getJson(tree, son, filter));
							sb.append(",");
						}
					}
					if (hasOne) {
						sb.deleteCharAt(sb.length() - 1);
						sb.append("]");
					}
				}
			}
			sb.append("}");
		}
		return sb.toString();
	}

	@Override
	public String getJson(TreeView tree, TreeNodeFilter filter) {
		if (!tree.isInited()) {
			throw new RuntimeException(BaseTree.NOT_INITED_TIP);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		List<TreeNode> roots = tree.getRoots();
		if (!FrameTool.isEmpty(roots)) {
			for (TreeNode node : roots) {
				sb.append(getJson(tree, node, filter));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}

	private String getJson(TreeView tree, TreeNode node, Set<TreeNode> limitNodes) {
		if (!tree.isInited()) {
			throw new RuntimeException(BaseTree.NOT_INITED_TIP);
		}
		if (!tree.isTreeNodeExists(node)) {
			throw new RuntimeException(BaseTree.TREE_NODE_NOT_EXISTS_TIP);
		}
		if (!limitNodes.contains(node)) {
			throw new MalformedParametersException(node.getId());
		}
		StringBuilder sb = new StringBuilder();
		sb.append(getTreeNodeBaseJson(node));
		if (node.isParent()) {
			List<TreeNode> sons = tree.getSons(node.getId());
			if (!FrameTool.isEmpty(sons)) {
				boolean hasOne = false;
				for (TreeNode son : sons) {
					if (limitNodes.contains(son)) {
						if (!hasOne) {
							sb.append(",");
							sb.append("\"children\":");
							sb.append("[");
							hasOne = true;
						}
						sb.append(getJson(tree, son, limitNodes));
						sb.append(",");
					}
				}
				if (hasOne) {
					sb.deleteCharAt(sb.length() - 1);
					sb.append("]");
				}
			}
		}
		sb.append("}");
		return sb.toString();
	}

	private String getTreeNodeBaseJson(TreeNode node) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":").append("\"").append(node.getId()).append("\",");
		sb.append("\"text\":").append("\"").append(node.getName()).append("\"");
		if (node.hasNodeInfo()) {
			sb.append(",");
			sb.append("\"attributes\":");
			sb.append("{");
			Map<String, Object> nodeInfo = node.getNodeInfo();
			for (Map.Entry<String, Object> entry : nodeInfo.entrySet()) {
				sb.append("\"").append(entry.getKey()).append("\":");
				sb.append("\"").append(entry.getValue()).append("\"");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
		}
		return sb.toString();
	}

}
