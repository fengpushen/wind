<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>入离职登记管理</title>
<tags:commonHead />
</head>
<body>
	<div id="oprTipMge" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:true, closed:true, border:false"></div>
	<div class="easyui-panel" style="background-color: #E0EEEE;"
		data-options="noheader:true">
		<table style="width: 100%">
			<tr>
				<td style="width: 10%; text-align: right">身份证:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="IDCARD" style="width: 100%"
					data-options="disabled:true" value="${IDCARD }" /></td>
				<td style="width: 10%; text-align: right">姓名:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="HR_NAME" style="width: 100%"
					data-options="disabled:true" value="${HR_NAME }" /></td>
				<td style="width: 10%; text-align: right">已就业:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-combobox" name="IS_JOB" id="IS_JOB"
					style="width: 100%"
					data-options="
					url:'frame/loadCode.do?codeName=boolean',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false, readonly:true"
					value="${IS_JOB }" /></td>
			</tr>
		</table>
	</div>
	<div class="easyui-panel" title="入离职信息" style="width: 100%">
		<table id="datagridJob">
		</table>
	</div>
	<div id="jobnojobOpr"></div>

	<script type="text/javascript">
		function closeJobnojobOpr() {
			$('#jobnojobOpr').dialog('close');
		}

		function refreshHrInfo(hr_id) {
			$.ajax({
				type : "post",
				url : "busi/hr/loadHrInfo.do",
				dataType : "json",
				data : {
					'hr_id' : hr_id
				},
				success : function(rst) {
					if (rst.isSucc) {
						$('#IS_JOB').combobox('setValue',
								rst.info.hrInfo.IS_JOB);
					}
				}
			});
		}

		$(function() {
			try {
				var hr_id = '${HR_ID}';
				var toolbar = [
						{
							text : '入职登记',
							iconCls : 'icon-add',
							handler : function() {
								closeOrpTip('oprTipMge');
								$('#jobnojobOpr').dialog({
									title : '入职登记',
									width : 900,
									height : 300,
									closed : false,
									cache : false,
									href : 'busi/hr/showHrJobReg.do',
									queryParams : {
										'hr_id' : hr_id
									},
									modal : false,
									onBeforeClose : function() {
										$('#datagridJob').datagrid('reload');
										refreshHrInfo(hr_id);
									}
								});
							}
						},
						{
							text : '离职登记',
							iconCls : 'icon-add',
							handler : function() {
								closeOrpTip('oprTipMge');
								$('#jobnojobOpr').dialog({
									title : '离职登记',
									width : 900,
									height : 300,
									closed : false,
									cache : false,
									href : 'busi/hr/showHrNOjobReg.do',
									modal : false,
									onBeforeClose : function() {
										$('#datagridJob').datagrid('reload');
										refreshHrInfo(hr_id);
									}
								});
							}
						},
						{
							text : '删除',
							iconCls : 'icon-remove',
							handler : function() {
								var rows = $('#datagridJob').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else {
									var ids = [];
									var is_jobs = [];
									for (var i = 0; i < rows.length; i++) {
										ids.push(rows[i].ID);
										is_jobs.push(rows[i].IS_JOB);
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
																		url : "busi/hr/delJobNojobInfo.do",
																		dataType : "json",
																		data : {
																			'ids' : ids,
																			'is_jobs' : is_jobs,
																			'hr_id' : hr_id
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
																			refreshHrInfo(hr_id);
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
					url : 'busi/hr/loadHrJobList.do',
					queryParams : {
						"hr_id" : hr_id
					},
					singleSelect : false,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					frozenColumns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'JOB_TIME',
						title : '入/离职时间',
						width : '10%',
						align : 'center'
					}, {
						field : 'IS_JOB',
						title : '类型',
						width : '5%',
						align : 'center'
					} ] ],
					columns : [ [ {
						"title" : "入职信息",
						"colspan" : 5
					}, {
						"title" : "离职信息",
						"colspan" : 2
					} ], [ {
						field : 'JOB_AREA',
						title : '工作地',
						width : '8%',
						align : 'center'
					}, {
						field : 'JOB_TYPE',
						title : '工作类型',
						width : '8%',
						align : 'center'
					}, {
						field : 'JOB_DW',
						title : '工作单位',
						width : '15%',
						align : 'center'
					}, {
						field : 'JOB_GW',
						title : '工作岗位',
						width : '10%',
						align : 'center'
					}, {
						field : 'INCOME',
						title : '月收入(元)',
						width : '10%',
						align : 'center'
					}, {
						field : 'NOJOB_DW',
						title : '离职单位',
						width : '15%',
						align : 'center'
					}, {
						field : 'NOJOB_REASON',
						title : '离职时间',
						width : '15%',
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