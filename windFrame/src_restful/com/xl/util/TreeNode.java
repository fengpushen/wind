package com.xl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode {

	private String id;

	private String name;

	private String pid;

	private Map<String, Object> nodeInfo;

	private List<TreeNode> children;

	private boolean hasChildren;

	private boolean root;

	public TreeNode(String id, String name) {
		this(id, name, null);
	}

	public TreeNode(String id, String name, String pid) {
		this.id = id;
		this.name = name;
		this.pid = pid;
		if (pid == null) {
			root = true;
		}
	}

	public void setNodeInfo(Map<String, Object> nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public void addNodeInfo(String key, Object value) {
		if (nodeInfo == null) {
			nodeInfo = new HashMap<String, Object>();
		}
		nodeInfo.put(key, value);
	}

	public void addChild(TreeNode node) {
		if (children == null) {
			children = new ArrayList<TreeNode>();
		}
		children.add(node);
		if (!hasChildren) {
			hasChildren = true;
		}
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPid() {
		return pid;
	}

	public boolean isRoot() {
		return root;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!TreeNode.class.equals(obj.getClass())) {
			return false;
		}
		TreeNode otherNode = (TreeNode) obj;
		return id.equals(otherNode.getId());
	}

}
