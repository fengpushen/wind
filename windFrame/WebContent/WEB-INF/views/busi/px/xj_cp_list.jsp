<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>基层平台</title>
<tags:commonHead />
</head>
<body class="easyui-layout">

	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="overflow: hidden; background-color: #E0EEEE;"></div>

	<div data-options="region:'center', border:false">
		<table id="datagrid">
		</table>
	</div>

	<div id="dd"></div>

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		function closeWin(){
			$('#dd').dialog('close');
		}
		$(function() {
			var toolbar = [ {
				text : '参评信息',
				iconCls : 'icon-cut',
				handler : function() {
					try {
						var rows = $('#datagrid').datagrid('getSelections');
						if (rows == null || rows.length == 0) {
							$.messager.alert("", "请选中要操作的记录");
						} else if (rows.length > 1) {
							$.messager.alert("", "请选中单条记录进行操作");
						} else {
							var cp_id = rows[0].CP_ID;
							var url = "px/jcptpx_cp_detail.html?cp_id="+cp_id;
							$('#dd')
									.window({
												title : '参评信息',
												width : 1240,
												height : 600,
												content : '<iframe scrolling="yes" frameborder="0"  src="'
														+ url
														+ '" style="width:100%;height:98%;"></iframe>',
												onBeforeClose : function() {
													loadDatagridData();
												},
											});
						}
					} catch (e) {
						alert(e);
					}
				}
			} ];
			try {
				$('#datagrid').datagrid({
					url : "busi/px/loadXjCpList.do",
					method : 'POST',
					formId : 'qryForm',
					rownumbers : true,
					singleSelect : true,
					toolbar : toolbar,
					fit : true,
					pagination : false,
					striped : true,
					singleSelect : false,
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'PX_YEAR',
						title : '评选年度',
						width : '15%',
						align : 'center'
					}, {
						field : 'AREA_NAME',
						title : '参评地区',
						width : '20%',
						align : 'center'
					}, {
						field : 'CP_STATUS_NAME',
						title : '参评状态',
						width : '15%',
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