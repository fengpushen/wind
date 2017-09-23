<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>企业录用人员明细</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="overflow: hidden; background-color: #E0EEEE;">

		<form id="qryForm" method="post">
			<input type="hidden" id="HJ_AREA_LIST" name="HJ_AREA" />
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">是否转正:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="is_zz" id="is_zz"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">是否已过试用期:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="is_overtime" id="is_overtime"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">户籍地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="HJ_AREA_NAME_LIST"
						value="${accountInfo.staffInfo.AREA_NAME}" style="width: 100%"></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">是否离职:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="is_quit" id="is_quit"
						style="width: 100%" /></td>
					<td colspan="4" style="text-align: center;"><a
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

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/company/loadComRegJobList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			try {
				var toolbar = [
						{
							text : '离职登记',
							iconCls : 'icon-save',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									var QUIT_TIME = rows[0].QUIT_TIME;
									if (QUIT_TIME != null && QUIT_TIME != '') {
										$.messager.alert("", "请不要重复进行离职登记");
									} else {
										$('#dd').dialog({
											title : '离职登记',
											width : 1000,
											height : 300,
											closed : false,
											cache : false,
											href : 'busi/hr/showHrQuitUI.do',
											queryParams : {
												'hire_id' : rows[0].HIRE_ID
											},
											modal : false,
											onBeforeClose : function() {
												loadDatagridData();
											}
										});
									}
								}
							}
						},
						{
							text : '转正',
							iconCls : 'icon-save',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									var ids = [];
									for (var i = 0; i < rows.length; i++) {
										ids.push(rows[i].HIRE_ID);
									}
									$
											.ajax({
												type : "post",
												url : "busi/hr/turnRegularEmployees.do",
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
				comboboxDefaultInit('is_zz', 'boolean', false, 'auto', false,
						true);
				comboboxDefaultInit('is_overtime', 'boolean', false, 'auto',
						false, true);
				comboboxDefaultInit('is_quit', 'boolean', false, 'auto', false,
						true);
				var areaTree = new AreaTree('dlgList', 'HJ_AREA_LIST',
						'HJ_AREA_NAME_LIST');
				$('#areaTreeList').tree({
					url : 'busi/common/loadTree.do',
					method : 'post',
					queryParams : {
						'treeName' : 'busi_com_area_tree_bj',
						'rootId' : '430900000000'
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
						field : 'HR_NAME',
						title : '姓名',
						width : '6%',
						align : 'center'
					}, {
						field : 'HJ_AREA_NAME',
						title : '户籍地',
						width : '10%',
						align : 'center'
					}, {
						field : 'IN_TIME_STR',
						title : '登记时间',
						width : '8%',
						align : 'center'
					}, {
						field : 'C_NAME',
						title : '单位',
						width : '10%',
						align : 'center'
					}, {
						field : 'HIRE_TIME',
						title : '入职时间',
						width : '8%',
						align : 'center'
					}, {
						field : 'SY_MONTH',
						title : '试用期（月）',
						width : '8%',
						align : 'center'
					}, {
						field : 'SY_END_TIME_STR',
						title : '试用结束时间',
						width : '8%',
						align : 'center'
					}, {
						field : 'QUIT_TIME',
						title : '离职时间',
						width : '8%',
						align : 'center'
					}, {
						field : 'IS_WD_NAME',
						title : '是否转正',
						width : '8%',
						align : 'center'
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