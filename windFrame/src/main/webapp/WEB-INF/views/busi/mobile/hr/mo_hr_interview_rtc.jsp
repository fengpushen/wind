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

<tags:commonMobileHead />

<script type='text/javascript'
	src='https://cdn.scaledrone.com/scaledrone.min.js'></script>
<style>
.vid-wrap {
	width: 100%;
	height: 100%;
	background: #000;
}

.vid-wrap #remoteVideo {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%
}

.vid-wrap #localVideo {
	position: absolute;
	right: 0px;
	bottom: 0px;
	width: 20%;
	bottom: 0px;
	width: 30%;
	height: 30%
}
</style>
</head>
<body>
	<div class="vid-wrap">
		<video id="remoteVideo" autoplay></video>
		<video id="localVideo" autoplay muted></video>
	</div>
	<script type="text/javascript">

	var roomHash = "${room}";
	alert(roomHash);
	
	</script>
	<script type="text/javascript" src="resource/public/js/webrtc.js"></script>
</body>
</html>
