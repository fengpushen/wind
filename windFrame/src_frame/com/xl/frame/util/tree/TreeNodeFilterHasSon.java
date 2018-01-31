package com.xl.frame.util.tree;

/**
 * ���ڵ������������������ڵ�Ľڵ����ͨ������֤
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
