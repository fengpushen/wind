package com.xl.frame.util.tree;

import java.util.List;
import java.util.Set;

public interface TreeView {

	/**
	 * ����id��ȡ���еĽڵ�
	 * 
	 * @param id
	 * @return
	 */
	TreeNode getNode(String id);

	/**
	 * ��ȡ���еĸ��ڵ�
	 * 
	 * @return
	 */
	List<TreeNode> getRoots();

	/**
	 * ��ȡһ���ڵ�������ӽڵ�
	 * 
	 * @param treeNode
	 * @return
	 */
	List<TreeNode> getSons(TreeNode treeNode);

	List<TreeNode> getSons(String id);

	/**
	 * ���������ӽڵ�
	 * 
	 * @param node
	 */
	void addNode(TreeNode node);

	/**
	 * ��ȡ���������
	 * 
	 * @return
	 */
	int getMaxLevel();

	/**
	 * ��ȡ�ڵ�ĸ��ڵ�
	 * 
	 * @param id
	 * @return
	 */
	TreeNode getRoot(String id);

	TreeNode getRoot(TreeNode node);

	/**
	 * ��ȡһ��ڵ�����и��ڵ�
	 * 
	 * @param id
	 * @return
	 */
	List<TreeNode> getRoots(List<String> ids);

	Set<TreeNode> getRelatedHigherTreeNode(List<String> ids);

	/**
	 * ��ȡid���е��ϼ��ڵ㣬����id�Լ�
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
