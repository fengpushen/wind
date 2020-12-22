package com.xl.frame.util.tree;

import java.util.List;

/**
 * 树的json生成器
 *
 */
public interface TreeJsonMaker {

	/**
	 * 生成这个树以node为根节点，到第limitLevel层节点（包含）为止的子树的json
	 * 
	 * @param id
	 * @return
	 */
	String getJson(TreeView tree, TreeNode node, int limitLevel);

	/**
	 * 生成这个树到第limitLevel层节点（包含）为止的json
	 * 
	 * @param tree
	 * @param limitLevel
	 * @return
	 */
	String getJson(TreeView tree, int limitLevel);

	/**
	 * 生成这个树只包含ids中节点及其上级节点的json
	 * 
	 * @param tree
	 * @param ids
	 * @return
	 */
	String getJson(TreeView tree, List<String> ids);

	/**
	 * 生成这个树以node为根节点，所有节点都符合过滤器限制的子树的json
	 * 
	 * @param id
	 * @return
	 */
	String getJson(TreeView tree, TreeNode node, TreeNodeFilter filter);

	String getJson(TreeView tree, TreeNodeFilter filter);
}
