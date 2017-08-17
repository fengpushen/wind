<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>${app_name }</title>
<tags:commonHead />
</head>
<body class="easyui-layout">

	<div data-options="region:'north'"
		style="height: 80px; background-color: #B0C4DE; line-height: 80px; overflow: hidden;">
		<span style="font-size: 30px; margin-left: 30px;">${app_name }</span>
		<span style="float: right; margin-right: 30px;">欢迎您：${accountShowName }<c:if
				test="${accountAreaName != null }">(${accountAreaName})</c:if>&nbsp;&nbsp;<a
			href="javascript:void(0)" class="easyui-linkbutton" id="changePwdBtn"
			data-options="plain:true">修改密码</a>&nbsp;&nbsp;<a id="logoutBtn"
			href="javascript:void(0)" class="easyui-linkbutton"
			data-options="plain:true">退出登录</a></span>
	</div>
	<div data-options="region:'west',split:true" title="功能菜单"
		style="width: 200px; background-color: #F5F5F5;">

		<ul class="easyui-tree" id="menuTree"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: hidden;">
		<div class="easyui-tabs"
			data-options="fit:true,border:false,plain:true" id="tabs"
			style="overflow: hidden;">
			<div title="欢迎页"
				data-options="href:'loginaccount/showWelcomeUI.do',closable:false"
				style="padding: 1px"></div>
		</div>
		<div id="passwordDiv"></div>
	</div>
	<div data-options="region:'south'"
		style="text-align: center; background-color: #B0C4DE; overflow: hidden;">2017.${ownership_of_copyright }
		All rights reserved.</div>
	<script type="text/javascript">
		$(function() {
			var loadedMenu = {};
			$('#tabs').tabs({
				onBeforeClose : function(title, index) {
					var closeingTag = $('#tabs').tabs('getTab', index);
					var id = closeingTag.panel('options').id;
					delete loadedMenu[id];
					return true;
				}
			});
			$("#logoutBtn")
					.bind(
							'click',
							function() {
								$.messager
										.confirm(
												'确认',
												'您确定要退出本系统吗？',
												function(r) {
													if (r) {
														$
																.ajax({
																	type : "post",
																	url : "loginaccount/logout.do",
																	success : function(
																			data) {
																		var rst = eval('('
																				+ data
																				+ ')');
																		if (rst.isSucc) {
																			window.location.href = baseHref
																					+ rst.info.INFO_KEY_DEFAULT;
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
																	}
																});
													}
												});
							});
			$("#changePwdBtn").bind('click', function() {
				$('#passwordDiv').dialog({
					title : '修改密码',
					width : 400,
					height : 200,
					closed : false,
					cache : false,
					href : 'loginaccount/showChangePwdUI.do',
					modal : false
				});
			});
			$('#menuTree')
					.tree(
							{
								url : 'loginaccount/loadUserMenu.do',
								onClick : function(node) {
									if (!(node.id in loadedMenu)) {
										var content = '<iframe frameborder="0"  src="'
												+ node.attributes.menu_url
												+ '" style="width:100%;height:100%;"></iframe>';
										$('#tabs').tabs('add', {
											id : node.id,
											title : node.text,
											content : content,
											closable : true,
											fit : true,
											style : {
												"padding" : "1px",
												"overflow" : "hidden"
											}
										});
										loadedMenu[node.id] = node.id;
									} else {
										$('#tabs').tabs('select', node.text);
									}
								}
							});
		});
	</script>
</body>
</html>