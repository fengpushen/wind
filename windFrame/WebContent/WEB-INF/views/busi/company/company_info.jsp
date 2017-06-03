<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>招聘单位信息管理</title>
<tags:commonHead />
</head>
<body>
	<div id="oprTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:false, closed:true, border:false"></div>
	<form id="theForm" method="post" style="width: 100%">
		<input type="hidden" name="C_ID" value="${C_ID }" /> <input
			type="hidden" id="ctype" name="C_TYPE" value="${C_TYPE }" /><input
			type="hidden" id="C_ID_WT" name="C_ID_WT" value="${C_ID_WT }" />
		<div class="easyui-panel" data-options="noheader:true"
			style="width: 100%; background-color: #E0EEEE;" id="infoDiv">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">单位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_NAME" style="width: 100%"
						data-options="required:true, validType:'length[2,20]'"
						value="${C_NAME }" /></td>
					<td style="width: 10%; text-align: right">营业执照:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_LICENCE" style="width: 100%"
						value="${C_LICENCE }" /></td>
					<td style="width: 10%; text-align: right">法人姓名:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_FR" style="width: 100%"
						value="${C_FR }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">法人身份证:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_FR_IDCARD"
						data-options="validType:['length[18, 18]', 'validIdcard']"
						style="width: 100%" value="${C_FR_IDCARD }"></td>
					<td style="width: 10%; text-align: right">成立时间:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-datebox" name="C_CREATETIME"
						data-options="sharedCalendar:'#cc'" style="width: 100%"
						value="${C_CREATETIME }"></td>
					<td style="width: 10%; text-align: right">注册资金:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_FUND" data-options="prompt:'万元'"
						style="width: 100%" value="${C_FUND }"></td>

				</tr>
				<tr>
					<td style="width: 10%; text-align: right">员工数量:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_DIMENSIONS" style="width: 100%"
						value="${C_DIMENSIONS }" /></td>
					<td style="width: 10%; text-align: right">招聘联系人:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_ZPR" style="width: 100%"
						value="${C_ZPR }" /></td>
					<td style="width: 10%; text-align: right">招聘联系电话:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_ZPDH" style="width: 100%"
						value="${C_ZPDH }" /></td>

				</tr>
				<tr>
					<td style="width: 10%; text-align: right">单位地址:</td>
					<td style="width: 56%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="C_ADDRESS" style="width: 95%"
						value="${C_ADDRESS }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">单位简介:</td>
					<td style="width: 56%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="C_INTRO"
						data-options="multiline:true" style="width: 95%; height: 100px;"
						value="${C_INTRO }" /></td>
				</tr>
			</table>
		</div>

		<div style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#theForm').form('submit');" style="width: 80px">保存</a>
		</div>
	</form>

	<div id="cc" class="easyui-calendar" data-options="closed:true"></div>

	<script type="text/javascript">
		$(function() {
			var c_id = '${C_ID}';
			var c_type = '${C_TYPE}';
			var C_ID_WT = '${C_ID_WT}';
			$("#infoDiv").click(function() {
				$('#oprTip').panel('close');
			});
			$('#theForm').form({
				url : 'busi/company/saveComInfo.do',
				success : function(data) {
					var rst = eval('(' + data + ')');
					if (rst.isSucc) {
						if (c_id == '') {
							loadDatagridData();
							showOprTip("oprTip", "操作成功，你可继续添加下一条", 'green');
							$('#theForm').form("clear");
							$('#ctype').val(c_type);
							$('#C_ID_WT').val(C_ID_WT);
						} else {
							$.messager.alert('提示', '操作成功');
							$('#dd').dialog('close');
						}
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

		});
	</script>


</body>
</html>