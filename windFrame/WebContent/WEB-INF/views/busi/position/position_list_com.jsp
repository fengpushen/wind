<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>招聘岗位管理</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" style="width: 100%;"
		data-options="noheader:true">
		<div class="easyui-panel" data-options="noheader:true"
			style="background-color: #E0EEEE;" id="divForm">
			<form id="qryForm" method="post">
				<table style="width: 100%">
					<tr>
						<td style="width: 10%; text-align: right">岗位名称:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-textbox" name="C_NAME_LIKE" style="width: 100%" /></td>
						<td style="width: 10%; text-align: right">是否公开:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-combobox" name="P_STATUS" style="width: 100%"
							data-options="
					url:'frame/loadCode.do?codeName=boolean',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto',editable:false"></td>
						<td style="width: 33%; text-align: center"><a
							href="javascript:void(0)" class="easyui-linkbutton"
							onclick="loadDatagridData();" style="width: 80px">查询</a></td>
					</tr>
				</table>
			</form>
		</div>
		<table id="datagrid">
		</table>
	</div>

	<div id="dd"></div>

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/position/loadComPositionList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			var toolbar = [
					{
						text : '新增',
						iconCls : 'icon-add',
						handler : function() {
							$('#dd')
									.dialog(
											{
												title : '新增职位',
												width : 1000,
												height : 450,
												closed : false,
												cache : false,
												href : 'busi/position/showAddComPositionInfoUI.do',
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
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows == null || rows.length == 0) {
								$.messager.alert("", "请选中要操作的记录");
							} else {
								var ids = [];
								var names = [];
								for (var i = 0; i < rows.length; i++) {
									ids.push(rows[i].P_ID);
									names.push(rows[i].P_NAME);
								}
								var msg = '你确定要删除' + names.join(',') + "这"
										+ names.length + "个职位？";
								$.messager
										.confirm(
												'Confirm',
												msg,
												function(r) {
													if (r) {
														$
																.ajax({
																	type : "post",
																	url : "busi/position/delComPosition.do",
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
																						+ ','
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
							try {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									var pid = rows[0].P_ID;
									$('#dd')
											.dialog(
													{
														title : '修改职位',
														width : 1000,
														height : 450,
														closed : false,
														cache : false,
														href : 'busi/position/showComPositionInfoSaveUI.do',
														queryParams : {
															'pid' : pid
														},
														modal : false,
														onBeforeClose : function() {
															loadDatagridData();
														}
													});
								}
							} catch (e) {
								alert(e);
							}
						}
					},
					{
						text : '公开',
						iconCls : 'icon-remove',
						handler : function() {
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows == null || rows.length == 0) {
								$.messager.alert("", "请选中要操作的记录");
							} else {
								var ids = [];
								for (var i = 0; i < rows.length; i++) {
									ids.push(rows[i].P_ID);
								}
								$
										.ajax({
											type : "post",
											url : "busi/position/pubPosition.do",
											dataType : "json",
											data : {
												'ids' : ids
											},
											success : function(rst) {
												if (rst.isSucc) {
													$.messager
															.alert("", "操作成功");
												} else {
													var msg = '操作失败';
													if (rst.info.INFO_KEY_DEFAULT != null) {
														msg = msg
																+ ','
																+ rst.info.INFO_KEY_DEFAULT;
													}
													$.messager.alert("", msg);
												}
												loadDatagridData();
											}
										});
							}
						}
					},
					{
						text : '不公开',
						iconCls : 'icon-remove',
						handler : function() {
							var rows = $('#datagrid').datagrid('getSelections');
							if (rows == null || rows.length == 0) {
								$.messager.alert("", "请选中要操作的记录");
							} else {
								var ids = [];
								for (var i = 0; i < rows.length; i++) {
									ids.push(rows[i].P_ID);
								}
								$
										.ajax({
											type : "post",
											url : "busi/position/unpubPosition.do",
											dataType : "json",
											data : {
												'ids' : ids
											},
											success : function(rst) {
												if (rst.isSucc) {
													$.messager
															.alert("", "操作成功");
												} else {
													var msg = '操作失败';
													if (rst.info.INFO_KEY_DEFAULT != null) {
														msg = msg
																+ ','
																+ rst.info.INFO_KEY_DEFAULT;
													}
													$.messager.alert("", msg);
												}
												loadDatagridData();
											}
										});
							}
						}
					} ];
			var divForm = document.getElementById('divForm');
			var tableHeight = document.body.clientHeight - divForm.offsetHeight
					- 70;
			try {
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					singleSelect : true,
					toolbar : toolbar,
					fit : true,
					style : {
						'height' : tableHeight + 'px'
					},
					pagination : true,
					striped : true,
					singleSelect : false,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'C_NAME',
						title : '单位名称',
						width : '15%',
						align : 'center'
					}, {
						field : 'P_NAME',
						title : '岗位名称',
						width : '15%',
						align : 'center'
					}, {
						field : 'P_NUM',
						title : '招聘人数',
						width : '15%',
						align : 'center'
					}, {
						field : 'P_WORK_AREA_NAME',
						title : '工作地点',
						width : '8%',
						align : 'center'
					}, {
						field : 'END_TIME',
						title : '截止时间（包含）',
						width : '10%',
						align : 'center'
					}, {
						field : 'P_ZPR',
						title : '招聘负责人',
						width : '8%',
						align : 'center'
					}, {
						field : 'P_ZPDH',
						title : '招聘电话',
						width : '8%',
						align : 'center'
					}, {
						field : 'P_STATUS_NAME',
						title : '是否公开',
						width : '8%',
						align : 'center'
					} ] ],
				});
				loadDatagridData();
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>