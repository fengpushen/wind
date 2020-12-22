package com.xl.frame.util.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xl.frame.util.FrameTool;

public class BaseTree implements TreeView {

	public static final String NOT_INITED_TIP = "the baseTree is not inited.";

	public static final String TREE_NODE_NOT_EXISTS_TIP = "the treeNode is not exists in this tree.";

	public static final String PARENT_TREE_NODE_NOT_EXISTS_TIP = "the parent treeNode is not exists in this tree ";

	private List<TreeNode> roots;

	private Map<String, TreeNode> nodes;

	private boolean inited;

	private int maxLevel;

	private TreeJsonMaker treeJsonMaker;

	public BaseTree(boolean inited, TreeJsonMaker treeJsonMaker) {
		this.inited = inited;
		this.treeJsonMaker = treeJsonMaker;
		roots = new ArrayList<TreeNode>();
		nodes = new HashMap<String, TreeNode>();
	}

	public BaseTree(TreeJsonMaker treeJsonMaker) {
		this(true, treeJsonMaker);
	}

	/**
	 * 初始化树时候的节点信息里面不需要包括子节点信息，只需要有父节点信息就可以了，子节点信息在初始化的过程中生成
	 * 
	 * @param initNodes
	 */
	public synchronized void initTree(List<TreeNode> initNodes) {
		inited = false;
		roots.clear();
		nodes.clear();
		for (TreeNode node : initNodes) {
			nodes.put(node.getId(), node);
			if (FrameTool.isEmpty(node.getPid())) {
				roots.add(node);
			}
		}
		for (TreeNode node : initNodes) {
			String pid = node.getPid();
			if (!FrameTool.isEmpty(pid)) {
				TreeNode pNode = nodes.get(pid);
				if (!FrameTool.isEmpty(pNode)) {
					pNode.addSonId(node.getId());
				}
			}
		}
		for (TreeNode node : roots) {
			setTreeNodeLevelCycl(node, 0);
		}
		Collections.sort(roots);
		inited = true;
	}

	public void setTreeJsonMaker(TreeJsonMaker treeJsonMaker) {
		this.treeJsonMaker = treeJsonMaker;
	}

	public void addNode(TreeNode node) {
		if (!inited) {
			throw new RuntimeException(NOT_INITED_TIP);
		}
		if (!isTreeNodeExists(node)) {
			String pid = node.getPid();
			if (pid == null) {
				roots.add(node);
			} else {
				TreeNode pNode = getNode(pid);
				if (pNode != null) {
					pNode.addSonId(node.getId());
					setTreeNodeLevel(node, pNode.getLevel() + 1);
				} else {
					throw new RuntimeException(PARENT_TREE_NODE_NOT_EXISTS_TIP + ":pid");
				}
			}
			nodes.put(node.getId(), node);
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

	public TreeNode getRoot(TreeNode node) {
		if (!inited) {
			throw new RuntimeException(NOT_INITED_TIP);
		}
		if (!isTreeNodeExists(node)) {
			throw new RuntimeException(TREE_NODE_NOT_EXISTS_TIP);
		}
		if (isRoot(node)) {
			return node;
		} else {
			return getRoot(getNode(node.getPid()));
		}
	}

	public TreeNode getRoot(String id) {
		return getRoot(getNode(id));
	}

	public List<TreeNode> getRoots(List<String> ids) {
		if (!inited) {
			throw new RuntimeException(NOT_INITED_TIP);
		}
		if (FrameTool.isEmpty(ids)) {
			return null;
		}
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for (String id : ids) {
			TreeNode root = getRoot(id);
			if (!nodes.contains(root)) {
				nodes.add(root);
			}
		}
		Collections.sort(nodes);
		return nodes;
	}

	public Set<TreeNode> getRelatedHigherTreeNode(List<String> ids) {
		if (!inited) {
			throw new RuntimeException(NOT_INITED_TIP);
		}
		Set<TreeNode> nodes = new HashSet<TreeNode>();
		for (String id : ids) {
			Set<TreeNode> higherNodes = getRelatedHigherTreeNode(id);
			nodes.addAll(higherNodes);
		}
		return nodes;
	}

	public Set<TreeNode> getRelatedHigherTreeNode(String id) {

		return getRelatedHigherTreeNode(getNode(id));
	}

	public Set<TreeNode> getRelatedHigherTreeNode(TreeNode node) {
		if (!inited) {
			throw new RuntimeException(NOT_INITED_TIP);
		}
		if (!isTreeNodeExists(node)) {
			throw new RuntimeException(TREE_NODE_NOT_EXISTS_TIP);
		}
		Set<TreeNode> nodes = new HashSet<TreeNode>();
		nodes.add(node);
		if (!isRoot(node)) {
			Set<TreeNode> higherNodes = getRelatedHigherTreeNode(getNode(node.getPid()));
			nodes.addAll(higherNodes);
		}
		return nodes;
	}

	public boolean isTheUnbornNode(String nodeId, String unbornNodeId) {
		return isTheUnbornNode(getNode(nodeId), getNode(unbornNodeId));
	}

	public boolean isTheUnbornNode(TreeNode node, TreeNode unbornNode) {
		if (!inited) {
			throw new RuntimeException(NOT_INITED_TIP);
		}
		if (!isTreeNodeExists(node) || !isTreeNodeExists(unbornNode)) {
			throw new RuntimeException(TREE_NODE_NOT_EXISTS_TIP);
		}
		if (node.equals(unbornNode)) {
			return false;
		}
		Set<TreeNode> higherNodes = getRelatedHigherTreeNode(unbornNode);
		if (!FrameTool.isEmpty(higherNodes) && higherNodes.contains(node)) {
			return true;
		}
		return false;
	}

	public int getMaxLevel() {
		if (inited) {
			return maxLevel;
		} else {
			throw new RuntimeException(NOT_INITED_TIP);
		}
	}

	public boolean isInited() {
		return inited;
	}

	public boolean isRoot(TreeNode node) {
		if (!inited) {
			throw new RuntimeException(NOT_INITED_TIP);
		}
		if (!isTreeNodeExists(node)) {
			throw new RuntimeException(TREE_NODE_NOT_EXISTS_TIP);
		}
		return roots.contains(node);
	}

	public boolean isRoot(String id) {
		return isRoot(getNode(id));
	}

	@Override
	public List<TreeNode> getSons(TreeNode treeNode) {
		if (isTreeNodeExists(treeNode)) {
			Set<String> sonIds = treeNode.getSonIds();
			if (!FrameTool.isEmpty(sonIds)) {
				List<TreeNode> sons = new ArrayList<TreeNode>();
				for (String sonId : sonIds) {
					if (isTreeNodeExists(sonId)) {
						sons.add(getNode(sonId));
					}
				}
				if (!FrameTool.isEmpty(sons)) {
					Collections.sort(sons);
					return sons;
				}
			}
		}
		return null;
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

	@Override
	public List<TreeNode> getSons(String id) {
		if (isTreeNodeExists(id)) {
			return getSons(getNode(id));
		}
		return null;
	}

	public String getJson(String id) {
		return getJson(getNode(id), maxLevel);
	}

	public String getJson(TreeNode node) {
		return getJson(node, maxLevel);
	}

	public String getJson(String id, int limitLevel) {
		return getJson(getNode(id), limitLevel);
	}

	public String getJson(TreeNode node, int limitLevel) {
		return treeJsonMaker.getJson(this, node, limitLevel);
	}

	public String getJson(String id, TreeNodeFilter filter) {
		return getJson(getNode(id), filter);
	}

	public String getJson(TreeNode node, TreeNodeFilter filter) {
		return treeJsonMaker.getJson(this, node, filter);
	}

	public String getJson(TreeNodeFilter filter) {
		return treeJsonMaker.getJson(this, filter);
	}

	public String getJson() {
		return getJson(maxLevel);
	}

	public String getJson(int limitLevel) {
		return treeJsonMaker.getJson(this, limitLevel);
	}

	public String getJson(List<String> ids) {
		return treeJsonMaker.getJson(this, ids);
	}

	private void setTreeNodeLevel(TreeNode node, int level) {
		if (maxLevel < level) {
			maxLevel = level;
		}
		node.setLevel(level);
	}

	private void setTreeNodeLevelCycl(TreeNode node, int level) {
		setTreeNodeLevel(node, level);
		if (node.isParent()) {
			List<TreeNode> sons = getSons(node.getId());
			if (!FrameTool.isEmpty(sons)) {
				for (TreeNode son : sons) {
					setTreeNodeLevelCycl(son, level + 1);
				}
			}
		}
	}

}
