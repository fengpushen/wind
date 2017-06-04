<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>中心登录管理</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="noheader:true,region:'center'">
		<div class="easyui-panel" data-options="noheader:true" id="divForm"
			style="background-color: #E0EEEE;">
			<form id="qryForm" method="post">
				<input type="hidden" id="AREA_CODE_LIST" name="area_code"
					value="${accountInfo.staffInfo.AREA_CODE}" />
				<table style="width: 100%">
					<tr>
						<td style="width: 10%; text-align: right">账号:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-textbox" name="account_like" style="width: 100%" /></td>
						<td style="width: 10%; text-align: right">部门:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-textbox" name="unit_name_like" style="width: 100%" /></td>
						<td style="width: 10%; text-align: right">行政区划:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-textbox" id="AREA_NAME_LIST"
							value="${accountInfo.staffInfo.AREA_NAME}" style="width: 100%"></td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px 0">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="loadDatagridData();" style="width: 80px">查询</a>
			</div>
		</div>
		<div class="easyui-panel" data-options="noheader:true"
			style="background-color: #E0EEEE;">
			<table id="datagrid">
			</table>
		</div>
	</div>

	<div id="dd"></div>

	<div id="dlgList" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true">
		<ul id="areaTreeList" class="easyui-tree">
		</ul>
	</div>

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/staff/loadStaffList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			try {
				var toolbar = [
						{
							text : '新增',
							iconCls : 'icon-add',
							handler : function() {
								$('#dd').dialog({
									title : '新增',
									width : 800,
									height : 250,
									closed : false,
									cache : false,
									href : 'busi/staff/showStaffInfo.do',
									modal : false,
									onBeforeClose : function() {
										loadDatagridData();
									}
								});
							}
						},
						{
							text : '删除',
							iconCls : 'icon-remove',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else {
									var ids = [];
									var names = [];
									for (var i = 0; i < rows.length; i++) {
										ids.push(rows[i].STAFF_ID);
										names.push(rows[i].STAFF_NAME);
									}
									$.messager
											.confirm(
													'Confirm',
													'你确定要删除' + names.join(',')
															+ "这"
															+ names.length
															+ "条记录吗？",
													function(r) {
														if (r) {
															$
																	.ajax({
																		type : "post",
																		url : "busi/staff/delStaff.do",
																		dataType : "json",
																		data : {
																			'ids' : ids
																		},
																		success : function(
																				rst) {
																			if (rst.isSucc) {
																				$.messager
																						.alert(
																								"",
																								"操作成功");
																			} else {
																				var msg = '操作失败';
																				if (rst.info.INFO_KEY_DEFAULT != null) {
																					msg = msg
																							+ ':'
																							+ rst.info.INFO_KEY_DEFAULT;
																				}
																				$.messager
																						.alert(
																								"",
																								msg);
																			}
																			loadDatagridData();
																		}
																	});
														}
													});
								}
							}
						},
						{
							text : '修改',
							iconCls : 'icon-cut',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									var staff_id = rows[0].STAFF_ID;
									$('#dd').dialog({
										title : '修改',
										width : 800,
										height : 250,
										closed : false,
										cache : false,
										href : 'busi/staff/showStaffMdy.do',
										queryParams : {
											'staff_id' : staff_id
										},
										modal : false,
										onBeforeClose : function() {
											loadDatagridData();
										}
									});
								}
							}
						} ];
				var areaTree = new AreaTree('dlgList', 'AREA_CODE_LIST',
						'AREA_NAME_LIST');
				var accountArea = '${accountInfo.staffInfo.AREA_CODE}';
				$('#areaTreeList').tree({
					url : 'busi/common/loadTree.do',
					method : 'post',
					queryParams : {
						'treeName' : 'busi_com_area_tree_bj',
						'rootId' : accountArea
					},
					onClick : function(node) {
						areaTree.nodeClick(node);
					}
				});
				$('#AREA_NAME_LIST').textbox({
					buttonText : '选择',
					onClickButton : function() {
						areaTree.showAreaTree();
					},
					onClick : function() {
						areaTree.showAreaTree();
					},
					editable : false,
					required : true
				});
				var divForm = document.getElementById('divForm');
				var tableHeight = document.body.clientHeight
						- divForm.offsetHeight - 70;
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					toolbar : toolbar,
					pagination : true,
					striped : true,
					fit : true,
					style : {
						'height' : tableHeight + 'px'
					},
					singleSelect : false,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'ACCOUNT',
						title : '账号',
						width : '15%',
						align : 'center'
					}, {
						field : 'STAFF_NAME',
						title : '用户名称',
						width : '15%',
						align : 'center'
					}, {
						field : 'AREA_NAME',
						title : '行政区划',
						width : '10%',
						align : 'center'
					}, {
						field : 'UNIT_NAME',
						title : '部门',
						width : '10%',
						align : 'center'
					}, {
						field : 'ROLE_NAMES',
						title : '权限',
						width : '50%',
						align : 'left'
					} ] ]
				});
				loadDatagridData();
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>