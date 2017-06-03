<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>用户绑定</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel">
		<header>
			<div class="m-toolbar">
				<span class="m-title">本网站为实名制网站，请先绑定您的身份证号码</span>
			</div>
		</header>
		<div
			style="margin: 20px auto; width: 100px; height: 100px; border-radius: 100px; overflow: hidden">
			<img src="../images/login1.jpg"
				style="margin: 0; width: 100%; height: 100%;">
		</div>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center; "
			data-options="noheader:false, closed:true, border:false"></div>
		<div style="padding: 0 20px">
			<form id="theForm" method="post" style="width: 100%">
				<div style="margin-bottom: 10px">
					<input class="easyui-textbox" name="idcard"
						data-options="required:true, validType:['length[18, 18]', 'validIdcard'], prompt:'身份证'"
						style="width: 100%; height: 38px">
				</div>
				<div>
					<input class="easyui-textbox" name="hr_name"
						data-options="required:true, validType:['length[2, 18]'], prompt:'姓名'"
						style="width: 100%; height: 38px">
				</div>
			</form>
			<div style="text-align: center; margin-top: 30px">
				<a href="javascript:void(0)" onclick="$('#theForm').form('submit');"
					class="easyui-linkbutton" plain="true" outline="true"
					style="width: 100px; height: 35px"><span
					style="font-size: 16px">绑定</span></a>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			var rebind = '${rebind}';
			if (rebind != null && rebind == '1') {
				localStorage.removeItem("hr_id");
			}
			var hr_id = localStorage.getItem("hr_id");
			if (hr_id != null && hr_id != '') {
				window.location.href = baseHref
						+ "loginaccount/loginHr.do?hr_id=" + hr_id;
			}
			$('#theForm')
					.form(
							{
								url : 'busi/hr/bindHr.do',
								success : function(data) {
									var rst = eval('(' + data + ')');
									if (rst.isSucc) {
										showOprTip("oprTip", "操作成功", "green");
										var hr_id = rst.info.hr_id;
										localStorage.setItem("hr_id", hr_id);
										window.location.href = baseHref
												+ "loginaccount/loginHr.do?hr_id="
												+ hr_id;
									} else {
										var msg = '操作失败';
										if (rst.info.INFO_KEY_DEFAULT != null) {
											msg = msg + ','
													+ rst.info.INFO_KEY_DEFAULT;
										}
										showOprTip("oprTip", msg, "red");
									}
									$('#oprTip').panel('open');

								}
							});

		});
	</script>
</body>
</html>
