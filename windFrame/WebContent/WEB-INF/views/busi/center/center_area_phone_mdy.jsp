<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>地区联系方式</title>
<tags:commonHead />
<style>
body, td, th, input {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 14px;
	color: #1d1007;
	line-height: 24px
}
</style>
</head>
<body>
	<div id="oprTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="closed:true, border:false"></div>
	<form id="theForm" method="post" style="width: 100%">
		<div class="easyui-panel" style="width: 100%"
			data-options="noheader:true" id="infoDiv">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; background-color: #F0F8FF;" rowspan="4">地区联系方式</td>
					<td style="width: 10%; text-align: right">地区名:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" style="width: 100%"
						data-options="required:true, disabled:true" value="${area_name }" /></td>
					<td style="width: 10%; text-align: right">联系人:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="CONTRACTOR" style="width: 100%"
						data-options="required:true" value="${CONTRACTOR }" /></td>
					<td style="width: 10%; text-align: right">联系电话:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="phone"  data-options="required:true"
						value="${PHONE}" style="width: 100%" /></td>
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
				$("body").click(function() {
					$('#oprTip').panel('close');
				});
				$('#theForm').form({
					url : 'busi/company/setCenterPhone.do',
					success : function(data) {
						var rst = eval('(' + data + ')');
						if (rst.isSucc) {
							showOprTip("oprTip", '操作成功', 'green');
						} else {
							var msg = '操作失败';
							if (rst.info.INFO_KEY_DEFAULT != null) {
								msg = msg + ',' + rst.info.INFO_KEY_DEFAULT;
							}
							showOprTip("oprTip", msg, 'red');
						}
						$('#oprTip').panel('open');
					}
				});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>