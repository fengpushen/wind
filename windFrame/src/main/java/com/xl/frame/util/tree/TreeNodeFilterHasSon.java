package com.xl.frame.util.tree;

/**
 * 父节点过滤器，所有有子孙节点的节点才能通过此验证
 *
 */
public class TreeNodeFilterHasSon implements TreeNodeFilter {

	@Override
	public boolean isPass(TreeNode node) {
		if (node != null && node.isParent()) {
			return true;
		}
		return false;
	}

}
