package com.xl.frame.util.tree;

import java.util.List;
import java.util.Set;

public interface TreeView {

	/**
	 * 根据id获取树中的节点
	 * 
	 * @param id
	 * @return
	 */
	TreeNode getNode(String id);

	/**
	 * 获取所有的根节点
	 * 
	 * @return
	 */
	List<TreeNode> getRoots();

	/**
	 * 获取一个节点的所有子节点
	 * 
	 * @param treeNode
	 * @return
	 */
	List<TreeNode> getSons(TreeNode treeNode);

	List<TreeNode> getSons(String id);

	/**
	 * 向树中增加节点
	 * 
	 * @param node
	 */
	void addNode(TreeNode node);

	/**
	 * 获取树的最大层高
	 * 
	 * @return
	 */
	int getMaxLevel();

	/**
	 * 获取节点的根节点
	 * 
	 * @param id
	 * @return
	 */
	TreeNode getRoot(String id);

	TreeNode getRoot(TreeNode node);

	/**
	 * 获取一组节点的所有根节点
	 * 
	 * @param id
	 * @return
	 */
	List<TreeNode> getRoots(List<String> ids);

	Set<TreeNode> getRelatedHigherTreeNode(List<String> ids);

	/**
	 * 获取id所有的上级节点，包括id自己
	 * 
	 * @param id
	 * @return
	 */
	Set<TreeNode> getRelatedHigherTreeNode(String id);

	Set<TreeNode> getRelatedHigherTreeNode(TreeNode node);

	String getJson(String id);

	String getJson(TreeNode node);

	String getJson(String id, int limitLevel);

	String getJson(TreeNode node, int limitLevel);

	String getJson();

	String getJson(int limitLevel);

	String getJson(List<String> ids);

	String getJson(String id, TreeNodeFilter filter);

	String getJson(TreeNode node, TreeNodeFilter filter);

	String getJson(TreeNodeFilter filter);

	boolean isRoot(TreeNode node);

	boolean isRoot(String id);

	boolean isInited();

	boolean isTreeNodeExists(TreeNode treeNode);

	boolean isTreeNodeExists(String id);

}
