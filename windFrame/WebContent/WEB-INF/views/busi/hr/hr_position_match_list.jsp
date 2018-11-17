<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>人岗智能匹配</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="background-color: #E0EEEE; overflow: hidden;">
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
					<td style="width: 10%; text-align: right">区域意向:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="area_kind" id="area_kind"
						style="width: 100%" /></td>
					<td colspan="4" style="text-align: center;"><a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="loadDatagridData();" style="width: 80px">开始匹配</a></td>
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
			$('#datagrid').datagrid('options').url = "busi/hr/loadHrPositionMatchList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			try {
				var toolbar = [ {
					text : '推荐',
					iconCls : 'icon-remove',
					handler : function() {
						var rows = $('#datagrid').datagrid('getSelections');
						if (rows == null || rows.length == 0) {
							$.messager.alert("", "请选中要操作的记录");
						} else {
							var pids = [];
							var hr_ids = [];
							for (var i = 0; i < rows.length; i++) {
								pids.push(rows[i].P_ID);
								hr_ids.push(rows[i].HR_ID);
							}
							$.messager
									.confirm(
											'Confirm',
											'你确定要执行' + "这" + hr_ids.length
													+ "次推荐吗？",
											function(r) {
												if (r) {
													$
															.ajax({
																type : "post",
																url : "busi/position/sendPositionReqCenter.do",
																dataType : "json",
																data : {
																	'pids' : pids,
																	'hr_ids' : hr_ids
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
				$("#LD_TYPE_LIST").combobox({
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
				comboboxDefaultInit('area_kind', 'area_kind', false,
						'auto', false, true, {
							"text" : "请选择",
							"id" : ""
						});
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
					editable : false,
					required : true
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
						field : 'HR_NAME',
						title : '姓名',
						width : '6%',
						align : 'center'
					}, {
						field : 'IDCARD_COVERD',
						title : '身份证',
						width : '14%',
						align : 'center'
					}, {
						field : 'AGE',
						title : '年龄',
						width : '4%',
						align : 'center'
					}, {
						field : 'SEX_NAME',
						title : '性别',
						width : '4%',
						align : 'center'
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
						field : 'LD_TYPE_NAME',
						title : '是否劳动力',
						width : '6%',
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
						title : '区域意向',
						width : '6%',
						align : 'center'
					}, {
						field : 'WANT_JOB_NAME',
						title : '工种意愿',
						width : '6%',
						align : 'center'
					}, {
						field : 'DEGREE_NAME',
						title : '文化程度',
						width : '8%',
						align : 'center'
					}, {
						field : 'JNTC',
						title : '技能特长',
						width : '15%',
						align : 'center'
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
					} ] ]
				});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>