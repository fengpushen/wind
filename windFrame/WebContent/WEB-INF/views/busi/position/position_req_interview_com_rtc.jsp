<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>视频面试</title>
<meta name="google" value="notranslate" />
<tags:commonHead />
<script type='text/javascript'
	src='https://cdn.scaledrone.com/scaledrone.min.js'></script>

<style>
body {
	display: flex;
	height: 100vh;
	margin: 0;
	align-items: center;
	justify-content: center;
	padding: 0 50px;
	font-family: -apple-system, BlinkMacSystemFont, sans-serif;
}

video {
	max-width: calc(50% - 100px);
	margin: 0 50px;
	box-sizing: border-box;
	border-radius: 2px;
	padding: 0;
	box-shadow: rgba(156, 172, 172, 0.2) 0px 2px 2px,
		rgba(156, 172, 172, 0.2) 0px 4px 4px, rgba(156, 172, 172, 0.2) 0px 8px
		8px, rgba(156, 172, 172, 0.2) 0px 16px 16px, rgba(156, 172, 172, 0.2)
		0px 32px 32px, rgba(156, 172, 172, 0.2) 0px 64px 64px;
}

</style>


</head>
<body>

	<video id="localVideo" autoplay muted></video>
	<video id="remoteVideo" autoplay></video>
	<script type="text/javascript">

	var roomHash = "${room}";
	
	</script>
	<script type="text/javascript" src="resource/public/js/webrtc.js"></script>
	
</body>
</html>