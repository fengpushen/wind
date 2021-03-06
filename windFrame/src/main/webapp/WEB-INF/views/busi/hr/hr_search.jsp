<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>劳动力资源信息查询</title>
<tags:commonHead />
</head> 
<body class="easyui-layout">
	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="overflow: hidden; background-color: #E0EEEE;">
		<form id="qryForm" method="post">
			<input type="hidden" id="HJ_AREA_LIST" name="HJ_AREA" />
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">身份证:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="IDCARD_LIKE" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">姓名:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="HR_NAME_LIKE" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">户籍地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="HJ_AREA_NAME_LIST"
						value="${accountInfo.staffInfo.AREA_NAME}" style="width: 100%"></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">是否就业:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="IS_JOB" id="IS_JOB_LIST"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">技能特长:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="JNTC" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">期望职位:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="WANT_JOB_NAME" style="width: 100%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">是否贫困户:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="is_poor" id="is_poor"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">就业区域意向:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="want_work_area_kind"
						id="want_work_area_kind" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">是否关注:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="is_label" id="is_label"
						style="width: 100%" /></td>
				</tr>
				<tr>
					<td colspan="6" style="text-align: center;"><a
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

	<div id="dlgList" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true">
		<ul id="areaTreeList" class="easyui-tree">
		</ul>
	</div>

	<div id="dlgLdType" class="easyui-dialog" title="选择"
		style="width: 250px; height: 80px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#ldtypeButtons'">
		<ul id="ldtypeTree" class="easyui-tree">
		</ul>
		<div id="#ldtypeButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanLdTypeBtn" style="width: 80px">清除</a>
		</div>
	</div>

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/hr/searchHrListCom.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		function closeJobnojobOpr() {
			$('#dd').dialog('close');
		}
		$(function() {
			try {
				var toolbar = [
						{
							text : '查看详情',
							iconCls : 'icon-save',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									$('#dd')
											.dialog(
													{
														title : '人员详情',
														width : 1000,
														height : 500,
														closed : false,
														cache : false,
														href : 'busi/hr/showHrInfoDetailCom.do',
														queryParams : {
															'hr_id' : rows[0].HR_ID
														},
														modal : false,
														onBeforeClose : function() {
															loadDatagridData();
														}
													});
								}
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
										href : 'busi/hr/showHrJobReg.do',
										queryParams : {
											'hr_id' : rows[0].HR_ID
										},
										modal : false,
										onBeforeClose : function() {
											loadDatagridData();
										}
									});
								}
							}
						},
						{
							text : '关注',
							iconCls : 'icon-save',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else {
									var ids = [];
									for (var i = 0; i < rows.length; i++) {
										ids.push(rows[i].HR_ID);
									}
									$
											.ajax({
												type : "post",
												url : "busi/hr/attentionHrs.do",
												dataType : "json",
												data : {
													'ids' : ids
												},
												success : function(rst) {
													if (rst.isSucc) {
														$.messager.alert("",
																"操作成功");
													} else {
														var msg = '操作失败';
														if (rst.info.INFO_KEY_DEFAULT != null) {
															msg = msg
																	+ ','
																	+ rst.info.INFO_KEY_DEFAULT;
														}
														$.messager.alert("",
																msg);
													}
													loadDatagridData();
												}
											});
								}
							}
						},
						{
							text : '取消关注',
							iconCls : 'icon-save',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else {
									var ids = [];
									for (var i = 0; i < rows.length; i++) {
										ids.push(rows[i].LABEL_ID);
									}
									$
											.ajax({
												type : "post",
												url : "busi/hr/unattentionHrs.do",
												dataType : "json",
												data : {
													'ids' : ids
												},
												success : function(rst) {
													if (rst.isSucc) {
														$.messager.alert("",
																"操作成功");
													} else {
														var msg = '操作失败';
														if (rst.info.INFO_KEY_DEFAULT != null) {
															msg = msg
																	+ ','
																	+ rst.info.INFO_KEY_DEFAULT;
														}
														$.messager.alert("",
																msg);
													}
													loadDatagridData();
												}
											});
								}
							}
						} ];
				$("#IS_JOB_LIST").combobox({
					method : 'post',
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto',
					editable : false,
					loader : function(param, success, error) {
						$.ajax({
							url : 'frame/loadCode.do?codeName=boolean',
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
				$("#IS_WANT_JOB_LIST").combobox({
					method : 'post',
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto',
					editable : false,
					loader : function(param, success, error) {
						$.ajax({
							url : 'frame/loadCode.do?codeName=boolean',
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
				comboboxDefaultInit('is_poor', 'boolean', false, 'auto', false,
						true);
				comboboxDefaultInit('want_work_area_kind', 'area_kind', false,
						'auto', false, true, {
							"text" : "均可",
							"id" : ""
						});
				comboboxDefaultInit('is_label', 'boolean', false, 'auto',
						false, true);
				var areaTree = new AreaTree('dlgList', 'HJ_AREA_LIST',
						'HJ_AREA_NAME_LIST');
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
				$('#HJ_AREA_NAME_LIST').textbox({
					buttonText : '选择',
					onClickButton : function() {
						areaTree.showAreaTree();
					},
					onClick : function() {
						areaTree.showAreaTree();
					},
					editable : false
				});
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					toolbar : toolbar,
					pagination : true,
					striped : true,
					singleSelect : false,
					fit : true,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'HR_NAME_COVERD',
						title : '姓名',
						width : '6%',
						align : 'center',
						sortable : true
					}, {
						field : 'AGE',
						title : '年龄',
						width : '4%',
						align : 'center',
						sortable : true
					}, {
						field : 'SEX_NAME',
						title : '性别',
						width : '4%',
						align : 'center',
						sortable : true
					}, {
						field : 'HJ_AREA_NAME',
						title : '户籍地',
						width : '8%',
						align : 'center'
					}, {
						field : 'NATION_NAME',
						title : '民族',
						width : '6%',
						align : 'center'
					}, {
						field : 'DEGREE_NAME',
						title : '文化程度',
						width : '8%',
						align : 'center',
						sortable : true
					}, {
						field : 'JNTC',
						title : '技能特长',
						width : '15%',
						align : 'center'
					}, {
						field : 'IS_JOB_NAME',
						title : '是否就业',
						width : '5%',
						align : 'center'
					}, {
						field : 'IS_WANT_JOB_NAME',
						title : '有就业意愿',
						width : '6%',
						align : 'center'
					}, {
						field : 'WANT_WORK_AREA_KIND_NAME',
						title : '就业区域意向',
						width : '6%',
						align : 'center'
					}, {
						field : 'WANT_JOB_NAME',
						title : '意向职位',
						width : '10%',
						align : 'center'
					}, {
						field : 'WANT_INCOME',
						title : '期望月薪',
						width : '6%',
						align : 'center',
						sortable : true
					}, {
						field : 'LABEL_NAME',
						title : '关注状态',
						width : '15%',
						align : 'center'
					}] ]
				});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>