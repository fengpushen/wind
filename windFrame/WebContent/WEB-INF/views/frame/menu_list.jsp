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
	<div data-options="region:'west',split:true" title="菜单"
		style="width: 200px; background-color: #F5F5F5;">
		<ul class="easyui-tree" id="menuTree"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: hidden;">
		<div class="easyui-tabs"
			data-options="fit:true,border:false,plain:true" id="tabs"
			style="overflow: hidden;">
			<div title="菜单信息" data-options="closable:false" style="padding: 1px">
				<div id="tip" class="easyui-panel" data-options="border:false">
					<p style="text-align: center">请选择需要操作的菜单</p>
				</div>
				<div id="menuInfo" class="easyui-panel"
					data-options="border:false,closed:true">
					<form id="theForm" method="post" style="width: 100%">
						<table style="width: 100%">
							<tr>
								<td style="width: 10%; text-align: right">上级菜单名称:</td>
								<td style="width: 90%; text-align: left" colspan="3"><input
									class="easyui-textbox" id="menu_p_name" style="width: 90%" /></td>
							</tr>
							<tr>
								<td style="width: 10%; text-align: right">菜单名:</td>
								<td style="width: 90%; text-align: left" colspan="3"><input
									class="easyui-textbox" name="menu_name" id="menu_name"
									style="width: 90%" /></td>
							</tr>
							<tr>
								<td style="width: 10%; text-align: right">菜单备注:</td>
								<td style="width: 90%; text-align: left" colspan="3"><input
									class="easyui-textbox" name="menu_memo" id="menu_memo"
									style="width: 90%" /></td>
							</tr>
							<tr>
								<td style="width: 10%; text-align: right">菜单URL:</td>
								<td style="width: 90%; text-align: left" colspan="3"><input
									class="easyui-textbox" name="menu_url" id="menu_url"
									style="width: 90%" /></td>
							</tr>
						</table>


						<div style="text-align: center;">
							<a href="javascript:void(0)" class="easyui-linkbutton" id="save"
								onclick="$('#theForm').form('submit');" style="width: 80px">保存</a>
							<a href="javascript:void(0)" class="easyui-linkbutton" id="del"
								onclick="$('#theForm').form('submit');" style="width: 80px">删除</a>
							<a href="javascript:void(0)" class="easyui-linkbutton"
								id="addSon" onclick="$('#theForm').form('submit');"
								style="width: 80px">新增子菜单</a> <a href="javascript:void(0)"
								class="easyui-linkbutton" id="addBro"
								onclick="$('#theForm').form('submit');" style="width: 80px">新增兄菜单</a>
						</div>
					</form>
				</div>
			</div>
			<div title="角色信息" data-options="closable:false" style="padding: 1px">
				<table id="datagrid">
				</table>
			</div>
		</div>

	</div>

	<script type="text/javascript">
		$(function() {
			$('#menuTree').tree({
				url : 'frame/loadAllMenu.do',
				onClick : function(node) {
					var menu_id = node.id;
					if (menu_id != null && menu_id != '') {
						$.ajax({
							type : "post",
							url : "frame/loadOneMenu.do",
							dataType : "json",
							data : {
								'menu_id' : node.id
							},
							success : function(rst) {
								if (rst.isSucc) {
									showMenuInfo(rst);
								} else {
									var msg = '操作失败';
									if (rst.info.INFO_KEY_DEFAULT != null) {
										msg = msg + ',' + rst.info.INFO_KEY_DEFAULT;
									}
									$.messager.alert("", msg);
								}
							}
						});
					}
				}
			});
			$('#datagrid').datagrid({
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
			$('#datagrid').datagrid('load');
		});

		function showMenuInfo(rst) {
			$('#menuInfo').panel('open');
			$('#tip').panel('close');
			
			$("#menu_p_name").textbox("setValue", "");
			$("#menu_name").textbox("setValue", "");
			$("#menu_memo").textbox("setValue", "");
			$("#menu_url").textbox("setValue", "");
			
			if (rst.info.pmenu != null) {
				$("#menu_p_name").textbox("setValue", rst.info.pmenu.name);
			}
			$("#menu_name").textbox("setValue", rst.info.menu.name);
			$("#menu_memo").textbox("setValue", rst.info.menu.nodeInfo.menu_memo);
			$("#menu_url").textbox("setValue", rst.info.menu.nodeInfo.menu_url);
			
			$("#datagrid").datagrid('uncheckAll');
			if(rst.info.ownTheMenuRoles != null){
				$.each(rst.info.ownTheMenuRoles, function (n, ownTheMenuRole) {
					//alert(ownTheMenuRole.ROLE_ID);
		            $("#datagrid").datagrid('selectRecord', ownTheMenuRole.ROLE_ID);
		        });
			}
		}
	</script>


</body>
</html>