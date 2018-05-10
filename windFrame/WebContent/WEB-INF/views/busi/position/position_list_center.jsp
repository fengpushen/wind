<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>招聘岗位</title>
<tags:commonHead />
</head>
<body class="easyui-layout">

	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="overflow: hidden; background-color: #E0EEEE;">
		<form id="qryForm" method="post">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">单位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="c_name_like" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">岗位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_NAME_LIKE" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">月工资大于:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="pay_botton_bgn" style="width: 100%" /></td>
					<td style="width: 33%; text-align: center"><a
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
			$('#datagrid').datagrid('options').url = "busi/position/loadPositionValidList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			var toolbar = [ {
				text : '查看岗位详情',
				iconCls : 'icon-cut',
				handler : function() {
					try {
						var rows = $('#datagrid').datagrid('getSelections');
						if (rows == null || rows.length == 0) {
							$.messager.alert("", "请选中要操作的记录");
						} else if (rows.length > 1) {
							$.messager.alert("", "请选中单条记录进行操作");
						} else {
							var pid = rows[0].P_ID;
							$('#dd').dialog({
								title : '岗位详情',
								width : 1000,
								height : 450,
								closed : false,
								cache : false,
								href : 'busi/position/showPositionInfoUI.do',
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
			}];
			try {
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					singleSelect : true,
					toolbar : toolbar,
					fit : true,
					pagination : true,
					striped : true,
					singleSelect : false,
					pageSize : 20,
					pageList : [20, 50, 100, 150, 200],
					columns : [[{
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
						field : 'P_PAY_BOTTON',
						title : '月工资从',
						width : '8%',
						align : 'center'
					}, {
						field : 'P_PAY_TOP',
						title : '月工资到',
						width : '8%',
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
					}]],
				});
				loadDatagridData();
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>