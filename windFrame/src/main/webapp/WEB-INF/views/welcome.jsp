<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>欢迎</title>
<tags:commonHead />
</head>
<body>
	<div
		style="background-size: 100%; background-image: url(resource/public/images/welcome.jpg); width: 100%; height: 100%; text-align: center">
		<span style="font-size: 30px; text-align: center">欢迎登录 <c:if
				test="${sessionScope.accountInfo.ACCOUNT_KIND == '0'}">中心端</c:if> <c:if
				test="${sessionScope.accountInfo.ACCOUNT_KIND == '1'}">企业端</c:if></span>
	</div>

</body>
</html>