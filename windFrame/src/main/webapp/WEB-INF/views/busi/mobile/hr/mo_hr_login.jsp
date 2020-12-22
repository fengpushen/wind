<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>用户登录</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel"
		style="background-size: 100%; background-image: url(resource/public/images/mobile_bind_bg.jpg); width: 100%; height: 100%; text-align: center">

		<p style="padding-top: 20px; font-size: 20px">益阳市劳动力资源信息管理平台</p>
		<p style="font-size: 18px">移动个人端</p>

		<div
			style="margin: 20px auto; width: 100px; height: 100px; border-radius: 100px; overflow: hidden">
			<img src="resource/public/images/timg.jpg"
				style="margin: 0; width: 100%; height: 100%;">
		</div>
		<div style="padding: 0 20px">
			<p>您已经绑定${HR_NAME }
			<p>
			<div style="text-align: center; margin-top: 30px">
				<a href="javascript:void(0)" onclick="unbind()"
					class="easyui-linkbutton" plain="true" outline="true"
					style="width: 100px; height: 35px"><span
					style="font-size: 16px">解除绑定</span></a>&nbsp;&nbsp; <a href="javascript:void(0)"
					onclick="goLogin();" class="easyui-linkbutton"
					plain="true" outline="true" style="width: 100px; height: 35px"><span
					style="font-size: 16px">登录</span></a>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		function unbind() {
			window.location.href = baseHref + "busi/hr/unbindHr.do";
		}
		function goLogin(){
			var hr_id = localStorage.getItem("hr_id");
			if (hr_id != null && hr_id != '') {
				window.location.href = baseHref
						+ "loginaccount/loginHr.do?hr_id=" + hr_id;
			}else{
				unbind();
			}
		}
	</script>
</body>
</html>
