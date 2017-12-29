<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>菜单管理</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div data-options="region:'west',split:true" title="菜单"
		style="width: 200px; background-color: #F5F5F5;">
		<ul class="easyui-tree" id="menuTree"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: hidden;">
		<div id="tip" class="easyui-panel" data-options="border:false">
			<p style="text-align: center">请选择需要操作的菜单</p>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			var loadedMenu = {};
			$('#menuTree').tree({
				url : 'frame/loadAllMenu.do',
				onClick : function(node) {
					alert(node.id);
				}
			});
		});
	</script>


</body>
</html>