<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>修改密码</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div id="oprTipChangePwd" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:false, closed:true, border:false"></div>
	<form id="theForm" method="post" style="width: 100%">
		<div class="easyui-panel"
			style="width: 100%; background-color: #E0EEEE;"
			data-options="noheader:true" id="infoPwdDiv">
			<table style="width: 100%">
				<tr>
					<td style="width: 30%; text-align: right">原密码:</td>
					<td style="width: 60%; text-align: left"><input
						class="easyui-passwordbox" name="password_old" style="width: 100%"
						data-options="required:true" /></td>
				</tr>
				<tr>
					<td style="width: 30%; text-align: right">新密码:</td>
					<td style="width: 60%; text-align: left"><input
						class="easyui-passwordbox" name="password" id="password"
						style="width: 100%"
						data-options="required:true, validType:['length[4, 16]']" /></td>
				</tr>
				<tr>
					<td style="width: 30%; text-align: right">新密码确认:</td>
					<td style="width: 60%; text-align: left"><input
						class="easyui-passwordbox" id="repassword" style="width: 100%"
						data-options="required:true, validType:'equalTo[\'password\']', invalidMessage:'两次密码输入不一致'" /></td>

				</tr>

			</table>
		</div>

		<div style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#theForm').form('submit');" style="width: 80px">保存</a>
		</div>
	</form>





	<script type="text/javascript">
		$(function() {
			try {
				$("#infoPwdDiv").click(function() {
					$('#oprTipChangePwd').panel('close');
				});
				$('#theForm').form({
					url : 'loginaccount/changeAccountSelfPwd.do',
					success : function(data) {
						var rst = eval('(' + data + ')');
						if (rst.isSucc) {

							$.messager.alert('提示', '操作成功');
							$('#passwordDiv').dialog('close');
						} else {
							var msg = '操作失败';
							if (rst.info.INFO_KEY_DEFAULT != null) {
								msg = msg + ',' + rst.info.INFO_KEY_DEFAULT;
							}
							showOprTip("oprTipChangePwd", msg, 'red');
							$('#oprTipChangePwd').panel('open');
						}

					}
				});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>