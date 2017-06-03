package com.xl.frame.util.tree;

import java.util.List;

public interface TreeView {

	TreeNode getNode(String id);

	List<TreeNode> getRoots();

	List<TreeNode> getSons(TreeNode treeNode);

	List<TreeNode> getSons(String id);

	boolean isTreeNodeExists(TreeNode treeNode);

	String toJson();

}
