package com.xl.frame.util.tree;

import java.util.List;

/**
 * ����json������
 *
 */
public interface TreeJsonMaker {

	/**
	 * �����������nodeΪ���ڵ㣬����limitLevel��ڵ㣨������Ϊֹ��������json
	 * 
	 * @param id
	 * @return
	 */
	String getJson(TreeView tree, TreeNode node, int limitLevel);

	/**
	 * �������������limitLevel��ڵ㣨������Ϊֹ��json
	 * 
	 * @param tree
	 * @param limitLevel
	 * @return
	 */
	String getJson(TreeView tree, int limitLevel);

	/**
	 * ���������ֻ����ids�нڵ㼰���ϼ��ڵ��json
	 * 
	 * @param tree
	 * @param ids
	 * @return
	 */
	String getJson(TreeView tree, List<String> ids);
}
