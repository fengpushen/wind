<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>账号管理</title>
<tags:commonHead />
</head>
<body>
	<div id="oprTipMge" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:true, closed:true, border:false"></div>
	<div class="easyui-panel" title="基本信息"
		style="width: 100%; background-color: #E0EEEE;"
		data-options="noheader:true">
		<table style="width: 100%">
			<tr>
				<td style="width: 10%; text-align: right">单位名称:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" style="width: 100%"
					data-options="disabled:true" value="${C_NAME }" /></td>
				<td style="width: 66%; text-align: right"></td>
			</tr>
		</table>
	</div>
	<div class="easyui-panel" title="账号列表" style="width: 100%">
		<table id="datagridJob">
		</table>
	</div>
	<div id="jobnojobOpr"></div>

	<script type="text/javascript">
		function closeJobnojobOpr() {
			$('#jobnojobOpr').dialog('close');
		}

		$(function() {
			try {
				var cid = '${C_ID}';
				var toolbar = [
						{
							text : '新增账号',
							iconCls : 'icon-add',
							handler : function() {
								closeOrpTip('oprTipMge');
								$('#jobnojobOpr').dialog({
									title : '新增账号',
									width : 900,
									height : 300,
									closed : false,
									cache : false,
									href : 'busi/company/showAddAccountUI.do',
									queryParams : {
										'cid' : cid
									},
									modal : false,
									onBeforeClose : function() {
										$('#datagridJob').datagrid('reload');
									}
								});
							}
						},
						{
							text : '重置密码',
							iconCls : 'icon-add',
							handler : function() {
								closeOrpTip('oprTipMge');
								var rows = $('#datagridJob').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									var account_id = rows[0].ACCOUNT_ID;
									$
											.ajax({
												type : "post",
												url : "busi/company/resetComAccountPassword.do",
												dataType : "json",
												data : {
													'account_id' : account_id
												},
												success : function(rst) {
													if (rst.isSucc) {
														var msg = '操作成功';
														if (rst.info.INFO_KEY_DEFAULT != null) {
															msg = msg
																	+ ','
																	+ rst.info.INFO_KEY_DEFAULT;
														}
														showOprTip('oprTipMge',
																msg, 'green');
													} else {
														var msg = '操作失败';
														if (rst.info.INFO_KEY_DEFAULT != null) {
															msg = msg
																	+ ','
																	+ rst.info.INFO_KEY_DEFAULT;
														}
														$.messager.alert("",
																msg);
														showOprTip('oprTipMge',
																msg, 'red');
													}
													$('#datagridJob').datagrid(
															'reload');
												}
											});
								}
							}
						},
						{
							text : '删除账号',
							iconCls : 'icon-remove',
							handler : function() {
								var rows = $('#datagridJob').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else {
									var ids = [];
									for (var i = 0; i < rows.length; i++) {
										ids.push(rows[i].ACCOUNT_ID);
									}
									var msg = '你确定要删除' + "这" + ids.length
											+ "条记录吗？";
									$.messager
											.confirm(
													'Confirm',
													msg,
													function(r) {
														if (r) {
															$
																	.ajax({
																		type : "post",
																		url : "busi/company/delComAccount.do",
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
																			$(
																					'#datagridJob')
																					.datagrid(
																							'reload');
																		}
																	});
														}
													});
								}
							}
						} ];
				$('#datagridJob').datagrid({
					method : 'POST',
					rownumbers : true,
					singleSelect : true,
					iconCls : 'icon-ok',
					toolbar : toolbar,
					noheader : true,
					pagination : true,
					striped : true,
					url : 'busi/company/loadComAccountList.do',
					queryParams : {
						"cid" : cid
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
						width : '40%',
						align : 'center'
					}, {
						field : 'IN_TIME',
						title : '创建时间',
						width : '40%',
						align : 'center'
					} ] ],
				});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>