<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>个人申请职位</title>
<style type="text/css" media="screen">
html, body {
	height: 100%;
}

body {
	margin: 0;
	padding: 0;
	overflow: auto;
	text-align: center;
	background-color: #ffffff;
}

object:focus {
	outline: none;
}

#flashContent {
	display: none;
}
</style>
<tags:commonMobileHead />
<script type="text/javascript" src="resource/flash/swfobject.js"></script>
<script type="text/javascript">
	var swfVersionStr = "11.1.0";
	// To use express install, set to playerProductInstall.swf, otherwise the empty string. 
	var xiSwfUrlStr = baseHref + "resource/flash/playerProductInstall.swf";
	var flashvars = {};
	var params = {};
	params.quality = "high";
	params.bgcolor = "#ffffff";
	params.allowscriptaccess = "sameDomain";
	params.allowfullscreen = "true";
	var attributes = {};
	attributes.id = "MyVideoChat";
	attributes.name = "MyVideoChat";
	attributes.align = "middle";
	swfobject.embedSWF(baseHref + "resource/flash/MyVideoChat.swf",
			"flashContent", "100%", "100%", swfVersionStr, xiSwfUrlStr,
			flashvars, params, attributes);
	// JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
	swfobject.createCSS("#flashContent", "display:block;text-align:left;");

	function getUrl() {
		var url = "${rtmp_url}";
		return url;
	}
	function getRoom() {
		var room = "${room}";
		return room;
	}
</script>
</head>
<body>
	<div class="easyui-navpanel">
		<header>
			<div class="m-toolbar">
				<div class="m-left">
					<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true" onclick="showMobileHrCenterUI()">个人控制台</a> <a
						href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true" onclick="showMobileHrInfoUI()">个人信息</a> <a
						href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true">职位搜索</a>
				</div>

			</div>
		</header>
		<div id="flashContent">
			<p>To view this page ensure that Adobe Flash Player version
				11.1.0 or greater is installed.</p>
			<a href='http://www.adobe.com/go/getflashplayer'><img
				src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif'
				alt='Get Adobe Flash player' /></a>
		</div>

		<noscript>
			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
				width="100%" height="100%" id="MyVideoChat">
				<param name="movie" value="resource/flash/MyVideoChat.swf" />
				<param name="quality" value="high" />
				<param name="bgcolor" value="#ffffff" />
				<param name="allowScriptAccess" value="sameDomain" />
				<param name="allowFullScreen" value="true" />
				<!--[if !IE]>-->
				<object type="application/x-shockwave-flash"
					data="resource/flash/MyVideoChat.swf" width="100%" height="100%">
					<param name="quality" value="high" />
					<param name="bgcolor" value="#ffffff" />
					<param name="allowScriptAccess" value="sameDomain" />
					<param name="allowFullScreen" value="true" />
					<!--<![endif]-->
					<!--[if gte IE 6]>-->
					<p>Either scripts and active content are not permitted to run
						or Adobe Flash Player version 11.1.0 or greater is not installed.</p>
					<!--<![endif]-->
					<a href="http://www.adobe.com/go/getflashplayer"> <img
						src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif"
						alt="Get Adobe Flash Player" />
					</a>
					<!--[if !IE]>-->
				</object>
				<!--<![endif]-->
			</object>
		</noscript>
	</div>
	<script type="text/javascript">
		function showMobileHrInfoUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrInfoUI.do";
		}
		function showMobileHrCenterUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrCenterUI.do";
		}
		$(function() {
			var hr_id = localStorage.getItem("hr_id");
			$('#datagrid')
					.datagrid(
							{
								method : 'POST',
								url : 'busi/position/loadHrReqList.do',
								queryParams : {
									"hr_id" : hr_id
								},
								columns : [ [
										{
											field : 'P_ID',
											"title" : "申请信息",
											width : '80%',
											align : 'left',
											formatter : function(value, row,
													index) {
												var space = "|";
												var showValue = row.P_NAME
														+ space
														+ row.IN_TIME_STR;
												showValue += "<br />";
												showValue += row.C_NAME;
												var room_no = row.ROOM_NO;
												if (room_no != null
														&& room_no != '') {
													var url = baseHref
															+ "busi/hr/showMobilePositionInterview.do?room="
															+ room_no;
													showValue += "<br />";
													showValue += "<a href='"+url+"'>视频面试</a>";
												}
												return showValue;
											}
										}, {
											field : 'REQ_STATUS_NAME',
											"title" : "状态",
											width : '20%',
											align : 'left'
										} ] ],
							});
		});
	</script>
</body>
</html>
