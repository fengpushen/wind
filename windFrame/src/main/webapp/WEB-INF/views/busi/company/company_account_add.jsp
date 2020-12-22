<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>增加账号</title>
<tags:commonHead />
</head>
<body>
	<div id="oprNOjobRegTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:false, closed:true, border:false"></div>
	<div class="easyui-panel" title="基本信息" style="width: 100%">
		<table style="width: 100%">
			<tr>
				<td style="width: 50%; text-align: right">单位名称:</td>
				<td style="width: 50%; text-align: left"><input
					class="easyui-textbox" style="width: 100%"
					data-options="disabled:true" value="${busi_name }" /></td>


			</tr>
		</table>
	</div>
	<form id="theForm" method="post" style="width: 100%">
		<input type="hidden" name="cid" value="${busi_id }" />
		<div class="easyui-panel" title="账号信息" style="width: 100%;">
			<table style="width: 100%">
				<tr>
					<td style="width: 20%; text-align: right">账号:</td>
					<td style="width: 30%; text-align: left"><input
						class="easyui-textbox" name="account" data-options="required:true"
						style="width: 100%"></td>
					<td style="width: 20%; text-align: right">密码:</td>
					<td style="width: 30%; text-align: left"><input
						class="easyui-textbox" name="password" style="width: 100%"
						data-options="prompt:'不输入密码将设置为默认密码${password }'"></td>
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
			$('#theForm').form({
				url : 'busi/company/addComAccount.do',
				success : function(data) {
					try {
						var rst = eval('(' + data + ')');
						if (rst.isSucc) {
							showOprTip('oprTipMge', '操作成功', 'green');
							closeJobnojobOpr();
						} else {
							var msg = '操作失败';
							if (rst.info.INFO_KEY_DEFAULT != null) {
								msg = msg + ',' + rst.info.INFO_KEY_DEFAULT;
							}
							showOprTip('oprNOjobRegTip', msg, 'red');
						}
					} catch (e) {
						alert(e);
					}
				},
				onSubmit : function() {
					closeOrpTip('oprNOjobRegTip');
					return true;
				}
			});

		});
	</script>


</body>
</html>