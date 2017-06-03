package com.xl.frame.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xl.frame.util.FrameTool;

public class TreeNode implements Comparable<TreeNode> {

	private List<TreeNode> sonNodes;

	private String id;

	private String name;

	private String pid;

	private int level;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public int compareTo(TreeNode o) {

		int pidOrder = getPid().compareTo(o.getPid());
		if (pidOrder == 0) {
			return getId().compareTo(o.getId());
		} else {
			return pidOrder;
		}
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

	public boolean isParent() {
		return !FrameTool.isEmpty(getSonNodes());
	}

	/**
	 * 判断本节点是否存在一个id等于传入参数的后代节点
	 * 
	 * @param unbornId
	 * @return
	 */
	public boolean hasTheUnbornNode(String unbornId) {
		boolean rst = false;
		if (!getId().equals(unbornId)) {
			if (isParent()) {
				for (TreeNode node : getSonNodes()) {
					if (node.getId().equals(unbornId) || node.hasTheUnbornNode(unbornId)) {
						rst = true;
						break;
					}
				}
			}
		} else {
			rst = true;
		}
		return rst;
	}

	public void addSonNode(TreeNode node) {
		if (node.getPid().equals(getId())) {
			if (sonNodes == null) {
				sonNodes = new ArrayList<TreeNode>();
			}
			if (!getSonNodes().contains(node)) {
				getSonNodes().add(node);
			}
		}
	}

	public void addSonNode(String id, String name) {
		TreeNode node = new TreeNode(id, name, getPid());
		addSonNode(node);
	}

	public void addSonNode(String id, String name, Map<String, Object> nodeInfo) {
		TreeNode node = new TreeNode(id, name, getPid());
		node.setNodeInfo(nodeInfo);
		addSonNode(node);
	}

	public List<TreeNode> getSonNodes() {
		return sonNodes;
	}

	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":").append("\"").append(id).append("\",");
		sb.append("\"text\":").append("\"").append(name).append("\"");
		if (nodeInfo != null && nodeInfo.size() > 0) {
			sb.append(",");
			sb.append("\"attributes\":");
			sb.append("{");
			for (Map.Entry<String, Object> entry : nodeInfo.entrySet()) {
				sb.append("\"").append(entry.getKey()).append("\":");
				sb.append("\"").append(entry.getValue()).append("\"");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
		}
		List<TreeNode> sons = getSonNodes();
		if (sons != null && sons.size() > 0) {
			sb.append(",");
			sb.append("\"children\":");
			sb.append("[");
			for (TreeNode node : sons) {
				sb.append(node.toJson());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return toJson();
	}
}
