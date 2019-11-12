<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>招聘单位管理</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="background-color: #E0EEEE; overflow: hidden;">

		<form id="qryForm" method="post">
			<input type="hidden" name="C_TYPE" value="${C_TYPE }" /> <input
				type="hidden" name="C_ID_WT" value="${C_ID_WT }" />
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">单位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_NAME_LIKE" style="width: 100%" /></td>
					<td style="width: 66%; text-align: center"><a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="loadDatagridData();" style="width: 80px">查询</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div data-options="region:'center', border:false">
		<table id="datagrid">
		</table>
	</div>

	<div id="dd"></div>

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/company/loadComList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			var ctype = '${C_TYPE}';
			var c_id_wt = '${C_ID_WT}';
			var toolbar = [
					{
						text : '新增',
						iconCls : 'icon-add',
						handler : function() {
							$('#dd').dialog({
								title : '新增单位',
								width : 1000,
								height : 350,
								closed : false,
								cache : false,
								href : 'busi/company/showComInfoSaveUI.do',
								queryParams : {
									'c_type' : ctype,
									'c_id_wt' : c_id_wt
								},
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
									ids.push(rows[i].C_ID);
									names.push(rows[i].C_NAME);
								}
								var msg = '你确定要删除' + names.join(',') + "这"
										+ names.length + "家单位？";
								$.messager
										.confirm(
												'Confirm',
												msg,
												function(r) {
													if (r) {
														$
																.ajax({
																	type : "post",
																	url : "busi/company/delComInfo.do",
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
									var cid = rows[0].C_ID;
									$('#dd')
											.dialog(
													{
														title : '修改单位',
														width : 1000,
														height : 350,
														closed : false,
														cache : false,
														href : 'busi/company/showComInfoSaveUI.do',
														queryParams : {
															'cid' : cid
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
					} ];
			if (ctype != 4) {
				toolbar
						.push({
							text : '账号管理',
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
										var cid = rows[0].C_ID;
										$('#dd')
												.dialog(
														{
															title : '单位账号管理',
															width : 1000,
															height : 400,
															closed : false,
															cache : false,
															href : 'busi/company/showComAccountMgeUI.do',
															queryParams : {
																'cid' : cid
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
						});
			}
			try {
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					singleSelect : true,
					toolbar : toolbar,
					pagination : true,
					striped : true,
					fit : true,
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
						field : 'C_ZPR',
						title : '联系人',
						width : '15%',
						align : 'center'
					}, {
						field : 'C_ZPDH',
						title : '联系电话',
						width : '8%',
						align : 'center'
					}, {
						field : 'C_ADDRESS',
						title : '地址',
						width : '20%',
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