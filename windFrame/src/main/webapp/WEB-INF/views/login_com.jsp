<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>单位登录</title>
<tags:commonHead />
</head>
<body
	style="background-image: url(resource/public/images/login_bg.jpg);">
	<div style="margin: 20px 0;"></div>
	<div class="easyui-panel" id="infoDiv"
		style="width: 100%; max-width: 600px; padding: 30px 60px; position: absolute; left: 50%; top: 50%; margin-left: -300px; margin-top: -200px; background-color: #dddddd;"
		data-options="border:false, noheader:true">
		<p style="font-size: 30px; text-align: center">${app_name }</p>
		<p style="font-size: 20px; text-align: center">单位用户登录</p>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center; font-size: 15px; background-color: #dddddd;"
			data-options="noheader:false, border:false"></div>
		<form class="easyui-form" method="post"
			action="loginaccount//loginCom.do" data-options="novalidate:true"
			id="ff">
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" name="account" style="width: 80%"
					data-options="label:'用户名:',required:true">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-passwordbox" name="password" style="width: 80%"
					data-options="label:'密码:', required:true">
			</div>
		</form>
		<div style="text-align: center; padding: 5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="submitForm()" style="width: 80px">登录</a>
		</div>
		<p style="text-align: center;">Copyright &copy;
			2017.${ownership_of_copyright } All rights reserved.</p>
	</div>
	<script>
		$(function() {
			$("#infoDiv").click(function() {
				$('#oprTip').panel('close');
			});
			$('#infoDiv').keydown(function(e) {
				if (e.keyCode == 13) {
					submitForm();
				}
			});
		});
		function submitForm() {
			$('#ff').form(
					'submit',
					{
						onSubmit : function() {
							return $(this).form('enableValidation').form(
									'validate');
						},
						success : function(rst) {
							var obj = JSON.parse(rst);
							if (obj.isSucc) {
								window.location.href = baseHref
										+ "loginaccount/showMainUI.do";
							} else {
								showOprTip("oprTip", obj.info.INFO_KEY_DEFAULT,
										"red");
							}
						}
					});
		}
	</script>
</body>
</html>