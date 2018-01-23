package com.xl.frame.util.tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.xl.frame.util.FrameTool;

public class TreeNode implements Comparable<TreeNode> {

	private Set<String> sonIds;

	private String id;

	private String name;

	private String pid;

	private int level;

	private String orderNo;

	private Map<String, Object> nodeInfo;

	public TreeNode(String id, String name) {
		this(id, name, null);
	}

	public TreeNode(String id, String name, String pid) {
		this.id = id;
		this.name = name;
		this.pid = pid;
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

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public Map<String, Object> getNodeInfo() {
		return nodeInfo;
	}

	public boolean hasNodeInfo() {
		return nodeInfo != null && nodeInfo.size() > 0;
	}

	public int compareTo(TreeNode o) {

		if (level < o.getLevel()) {
			return -1;
		}
		if (level > o.getLevel()) {
			return 1;
		}
		if (orderNo == null || o.getOrderNo() == null) {
			return -1;
		}
		return orderNo.compareTo(o.getOrderNo());
	}

	public boolean isParent() {
		return !FrameTool.isEmpty(getSonIds());
	}

	public void addSonId(String sonId) {
		if (sonIds == null) {
			initSonIds();
		}
		sonIds.add(sonId);
	}

	public Set<String> getSonIds() {
		return sonIds;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	private synchronized void initSonIds() {
		if (sonIds == null) {
			sonIds = new HashSet<String>();
		}
	}
}
