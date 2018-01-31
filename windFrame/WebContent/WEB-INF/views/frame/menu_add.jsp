<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>菜单管理</title>
<tags:commonHead />
</head>
<body class="easyui-layout">

	<div class="easyui-tabs"
		data-options="fit:true,border:false,plain:true" id="tabs"
		style="overflow: hidden;">
		<div title="菜单信息" data-options="closable:false" style="padding: 1px">
			<div id="menuInfo" class="easyui-panel" data-options="border:false">
				<form id="theForm" method="post" style="width: 100%">
					<input type="hidden" id="menu_p_id" name="menu_p_id"
						value="${pmenu.id }" /> <input type="hidden" name="role_ids"
						id="role_ids" />
					<table style="width: 100%">
						<tr>
							<td style="width: 20%; text-align: right">上级菜单名称:</td>
							<td style="width: 80%; text-align: left" colspan="3"><input
								class="easyui-textbox" id="menu_p_name" style="width: 90%"
								value="${pmenu.name }" /></td>
						</tr>
						<tr>
							<td style="width: 20%; text-align: right">菜单名:</td>
							<td style="width: 80%; text-align: left" colspan="3"><input
								class="easyui-textbox" name="menu_name" id="menu_name"
								style="width: 90%"
								data-options="required:true, validType:['length[2, 20]']" /></td>
						</tr>
						<tr>
							<td style="width: 20%; text-align: right">菜单备注:</td>
							<td style="width: 80%; text-align: left" colspan="3"><input
								class="easyui-textbox" name="menu_memo" id="menu_memo"
								style="width: 90%" data-options="validType:['length[0, 20]']" /></td>
						</tr>
						<tr>
							<td style="width: 20%; text-align: right">菜单URL:</td>
							<td style="width: 80%; text-align: left" colspan="3"><input
								class="easyui-textbox" name="menu_url" id="menu_url"
								style="width: 90%" data-options="validType:['length[0, 100]']" /></td>
						</tr>
					</table>


					<div style="text-align: center;">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							id="addSave" style="width: 80px">保存</a>
					</div>
				</form>
			</div>
			<div id="dlg" class="easyui-dialog" title="选择"
				style="width: 250px; height: 350px; padding: 10px;"
				data-options="iconCls:'icon-save',closed:true,buttons: '#oprButtons'">
				<ul id="pMenuTree" class="easyui-tree">
				</ul>
				<div id="#oprButtons" style="text-align: center;">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						id="cleanBtn" style="width: 80px">清除</a>
				</div>
			</div>
		</div>
		<div title="角色信息" data-options="closable:false" style="padding: 1px">
			<table id="datagridRole">
			</table>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {

			//初始化父菜单选择按钮
			var pMenuTree = new AreaTree('dlg', 'menu_p_id', 'menu_p_name');
			$('#menu_p_name').textbox({
				buttonText : '选择',
				onClickButton : function() {
					pMenuTree.showAreaTree();
				},
				onClick : function() {
					pMenuTree.showAreaTree();
				},
				editable : false
			});
			$('#pMenuTree').tree({
				url : 'frame/loadAllMenuGroup.do',
				method : 'post',
				onClick : function(node) {
					pMenuTree.nodeClick(node);
				}
			});
			$("#cleanBtn").bind('click', function() {
				pMenuTree.cleanChosed();
			});

			//初始化角色列表
			$('#datagridRole').datagrid({
				method : 'POST',
				rownumbers : true,
				url : "frame/loadRoleList.do",
				fitColumns : true,
				fit : true,
				pagination : false,
				striped : true,
				singleSelect : false,
				idField : 'ROLE_ID',
				columns : [[{
					field : 'ck',
					checkbox : true
				}, {
					field : 'ROLE_NAME',
					title : '角色名',
					align : 'center'
				}]]
			});
			$('#datagridRole').datagrid('load');

			$('#theForm').form({
				url : 'busi/hr/updateHrInfoStaff.do',
				success : function(data) {
					var rst = eval('(' + data + ')');
					if (rst.isSucc) {
						$.messager.alert('提示', '操作成功');
						$('#dd').dialog('close');
					} else {
						var msg = '操作失败';
						if (rst.info.INFO_KEY_DEFAULT != null) {
							msg = msg + ',' + rst.info.INFO_KEY_DEFAULT;
						}
					}
				}
			});

			$('#addSave').bind("click", function() {
				var roles = $('#datagridRole').datagrid('getSelections');
				alert(roles);
				//$('#theForm').form('submit');
			});
		});
	</script>


</body>
</html>