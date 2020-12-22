<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>视频面试</title>
<meta name="google" value="notranslate" />
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
<tags:commonHead />
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

	<div id="flashContent">
		<p>To view this page ensure that Adobe Flash Player version 11.1.0
			or greater is installed.</p>
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
				<p>Either scripts and active content are not permitted to run or
					Adobe Flash Player version 11.1.0 or greater is not installed.</p>
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

	<table>
		<tr>
			<td style="width: 5%">姓名</td>
			<td style="width: 20%">${hrInfo.HR_NAME }</td>
			<td style="width: 5%">出生日期</td>
			<td style="width: 20%">${hrInfo.BIRTH }</td>
			<td style="width: 5%">性别</td>
			<td style="width: 20%">${hrInfo.SEX_NAME}</td>
			<td style="width: 5%">民族</td>
			<td style="width: 20%">${hrInfo.NATION_NAME}</td>
		</tr>
		<tr>
			<td style="width: 5%">户籍地</td>
			<td style="width: 20%">${hrInfo.HJ_AREA_NAME}</td>
			<td style="width: 5%">文化程度</td>
			<td style="width: 20%">${hrInfo.DEGREE_NAME}</td>
			<td style="width: 5%">毕业学校</td>
			<td style="width: 20%">${hrInfo.SCHOOL}</td>
			<td style="width: 5%">毕业年度</td>
			<td style="width: 20%">${hrInfo.GRA_YEAR}</td>
		</tr>
		<tr>
			<td style="width: 5%">专业</td>
			<td style="width: 20%">${hrInfo.PRO}</td>
			<td style="width: 5%">电话</td>
			<td style="width: 20%">${hrInfo.PHONE}</td>
			<td style="width: 5%">QQ</td>
			<td style="width: 20%">${hrInfo.QQ}</td>
			<td style="width: 5%">电子邮箱</td>
			<td style="width: 20%">${hrInfo.EMAIL}</td>
		</tr>
		<tr>
			<td style="width: 5%">地址</td>
			<td style="width: 45%" colspan="3">${hrInfo.ADDRESS }</td>
			<td style="width: 5%">技能特长</td>
			<td style="width: 45%" colspan="3">${hrInfo.JNTC }</td>
		</tr>
		<tr>
			<td>
				<p>${hrInfo.INTRO}</p>
			</td>
		</tr>
	</table>

	<script type="text/javascript">
		$(function() {
		});
	</script>
</body>
</html>