<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>视频连线</title>
<tags:commonHead />
</head>
<body class="easyui-layout">

	<div data-options="region:'center', border:false">
		<table id="datagrid">
		</table>
	</div>



	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/company/loadAreaVideoList.do";
			$('#datagrid').datagrid('load');
		}
		$(function() {
			var toolbar = [ {
				text : '视频连线',
				iconCls : 'icon-save',
				handler : function() {
					var rows = $('#datagrid').datagrid('getSelections');
					if (rows == null || rows.length == 0) {
						$.messager.alert("", "请选中要操作的记录");
					} else if (rows.length > 1) {
						$.messager.alert("", "请选中单条记录进行操作");
					} else {
						var c_id = rows[0].C_ID;
						window
								.open(
										baseHref
												+ "busi/company/showComAreaVideoCenterUI.do?c_id="
												+ c_id, "_blank");
					}
				}
			} ];

			try {
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					singleSelect : true,
					toolbar : toolbar,
					pagination : true,
					striped : true,
					fit : true,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'C_NAME',
						title : '企业名称',
						width : '15%',
						align : 'center'
					} ] ],
				});
				loadDatagridData();
				window.setInterval(loadDatagridData, 10000);
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>