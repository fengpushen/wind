<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>岗位申请管理</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="noheader:true">
		<div class="easyui-panel" data-options="noheader:true"
			style="background-color: #E0EEEE;" id="divForm">
			<form id="qryForm" method="post">
				<table style="width: 100%">
					<tr>
						<td style="width: 10%; text-align: right">岗位名称:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-textbox" name="P_NAME_LIKE" style="width: 100%" /></td>
						<td style="width: 10%; text-align: right">申请状态:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-combobox" name="req_status" id="req_status"
							style="width: 100%"></td>
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
			$('#datagrid').datagrid('options').url = "busi/position/loadCorpReqList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		function batchChangeReqStatus(status_code, status_name) {
			var rows = $('#datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				$.messager.alert("", "请选中要操作的记录");
			} else {
				var ids = [];
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].REQ_ID);
				}
				var msg = '你确定要设置这' + ids.length + '个申请为' + status_name + '？';
				$.messager.confirm('Confirm', msg, function(r) {
					if (r) {
						$.ajax({
							type : "post",
							url : "busi/position/changeReqsStatus.do",
							dataType : "json",
							data : {
								'ids' : ids,
								'req_status' : status_code
							},
							success : function(rst) {
								if (rst.isSucc) {
									$.messager.alert("", "操作成功");
								} else {
									var msg = '操作失败';
									if (rst.info.INFO_KEY_DEFAULT != null) {
										msg = msg + ','
												+ rst.info.INFO_KEY_DEFAULT;
									}
									$.messager.alert("", msg);
								}
								loadDatagridData();
							}
						});
					}
				});
			}
		}
		$(function() {
			try {
				var req_status = "${req_status}";
				$("#req_status").combobox({
					method : 'post',
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto',
					editable : false,
					onLoadSuccess : function() {
						$(this).combobox("select", req_status);
						loadDatagridData();
					},
					loader : function(param, success, error) {
						$.ajax({
							url : 'frame/loadCode.do?codeName=req_status',
							dataType : 'json',
							success : function(data) {
								data.unshift({
									"text" : "请选择",
									"id" : ""
								});
								success(data);
							},
							error : function() {
								error.apply(this, arguments);
							}
						});
					}
				});
				var toolbar = [
						{
							text : '视频面试',
							iconCls : 'icon-save',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									var req_id = rows[0].REQ_ID;
									window
											.open(
													baseHref
															+ "busi/position/showComPositionReqInterviewUI.do?req_id="
															+ req_id, "_blank");
								}
							}
						},
						{
							text : '查看详情',
							iconCls : 'icon-save',
							handler : function() {
								try {
									var rows = $('#datagrid').datagrid(
											'getSelections');
									if (rows == null || rows.length == 0) {
										$.messager.alert("", "请选中要操作的记录");
									} else if (rows.length > 1) {
										$.messager.alert("", "请选中单条记录进行操作");
									} else {
										var req_id = rows[0].REQ_ID;
										$('#dd')
												.dialog(
														{
															title : '申请详情',
															width : 1000,
															height : 500,
															closed : false,
															cache : false,
															href : 'busi/position/showComPositionReqDetail.do',
															queryParams : {
																'req_id' : req_id
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
							text : '已通知',
							iconCls : 'icon-save',
							handler : function() {
								batchChangeReqStatus('03', '已通知');
							}
						},
						{
							text : '忽略',
							iconCls : 'icon-save',
							handler : function() {
								batchChangeReqStatus('04', '忽略');
							}
						},
						{
							text : '符合条件',
							iconCls : 'icon-save',
							handler : function() {
								batchChangeReqStatus('02', '符合条件');
							}
						},
						{
							text : '入职登记',
							iconCls : 'icon-save',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									$('#dd').dialog({
										title : '入职登记',
										width : 1000,
										height : 300,
										closed : false,
										cache : false,
										href : 'busi/position/showPositionReqJobReg.do',
										queryParams : {
											'req_id' : rows[0].REQ_ID
										},
										modal : false,
										onBeforeClose : function() {
											loadDatagridData();
										}
									});
								}
							}
						} ];
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					singleSelect : true,
					iconCls : 'icon-ok',
					toolbar : toolbar,
					pagination : true,
					striped : true,
					singleSelect : false,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'P_NAME',
						title : '岗位名称',
						width : '10%',
						align : 'center'
					}, {
						field : 'HR_NAME',
						title : '申请人',
						width : '8%',
						align : 'center'
					}, {
						field : 'IN_TIME_STR',
						title : '申请时间',
						width : '12%',
						align : 'center'
					}, {
						field : 'REQ_STATUS_NAME',
						title : '申请状态',
						width : '8%',
						align : 'center'
					}, {
						field : 'HJ_AREA_NAME',
						title : '户籍地',
						width : '8%',
						align : 'center'
					}, {
						field : 'SEX_NAME',
						title : '性别',
						width : '6%',
						align : 'center'
					}, {
						field : 'AGE',
						title : '年龄',
						width : '6%',
						align : 'center'
					}, {
						field : 'DEGREE_NAME',
						title : '学历',
						width : '8%',
						align : 'center'
					}, {
						field : 'JNTC',
						title : '技能特长',
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