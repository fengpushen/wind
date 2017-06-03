<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>人力资源信息管理</title>
<tags:commonHead />
</head>
<body>
	<div id="oprNOjobRegTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:false, closed:true, border:false"></div>
	<div class="easyui-panel" title="基本信息" style="width: 100%">
		<table style="width: 100%">
			<tr>
				<td style="width: 10%; text-align: right">身份证:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="IDCARD" style="width: 100%"
					data-options="disabled:true" value="${IDCARD }"/></td>
				<td style="width: 10%; text-align: right">姓名:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="HR_NAME" style="width: 100%"
					data-options="disabled:true" value="${HR_NAME }"/></td>
				<td style="width: 10%; text-align: right">劳动力类型:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-combobox" name="LD_TYPE" id="LD_TYPE"
					style="width: 100%"
					data-options="
					url:'frame/loadCode.do?codeName=ld_type',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto',required:true, editable:false,disabled:true" value="${LD_TYPE }"/></td>
			</tr>
		</table>
	</div>
	<form id="theForm" method="post" style="width: 100%">
		<input type="hidden" id="HR_ID" name="nojob.HR_ID" value="${HR_ID }"/>
		<div class="easyui-panel" title="就业信息" style="width: 100%;"
			id="jobInfoDiv" data-option="closed:true">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">离职时间:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-datebox" name="nojob.NOJOB_TIME"
						data-options="sharedCalendar:'#cc', required:true" style="width: 100%"></td>
					<td style="width: 10%; text-align: right">离职单位:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="nojob.NOJOB_DW" style="width: 100%"></td>
					<td style="width: 10%; text-align: right">离职原因:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="nojob.NOJOB_REASON" style="width: 100%"></td>
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
			$('#theForm').form({
				url : 'busi/hr/saveNOjobInfo.do',
				success : function(data) {
					try{
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
					}catch(e){
						alert(e);
					}
				},
				onSubmit:function(){
					closeOrpTip('oprNOjobRegTip');
				}
			});

		});
	</script>


</body>
</html>