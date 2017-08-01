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
	<div class="easyui-panel" data-options="noheader:true">
		<div class="easyui-panel" data-options="noheader:true"
			style="background-color: #E0EEEE;" id="divForm"></div>
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
						var req_id = rows[0].C_AREA_VIDEO_ID;
						window
								.open(
										baseHref
												+ "busi/company/showComAreaVideoCenterUI.do?c_area_video_id="
												+ req_id, "_blank");
					}
				}
			} ];

			var divForm = document.getElementById('divForm');
			var tableHeight = document.body.clientHeight - divForm.offsetHeight
					- 70;
			try {
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					singleSelect : true,
					toolbar : toolbar,
					pagination : true,
					striped : true,
					fit : true,
					style : {
						'height' : tableHeight + 'px'
					},
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