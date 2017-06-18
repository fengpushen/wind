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
	<div id="oprJobRegTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:false, closed:true, border:false"></div>
	<div class="easyui-panel" title="基本信息" style="width: 100%">
		<table style="width: 100%">
			<tr>
				<td style="width: 10%; text-align: right">身份证:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="IDCARD" style="width: 100%"
					data-options="disabled:true" value="${positionReqInfo.IDCARD }" /></td>
				<td style="width: 10%; text-align: right">姓名:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="HR_NAME" style="width: 100%"
					data-options="disabled:true" value="${positionReqInfo.HR_NAME }" /></td>
			</tr>
		</table>
	</div>
	<form id="theForm" method="post" style="width: 100%">
		<input type="hidden" id="GZ_AREA" name="job.JOB_AREA"
			value="${positionReqInfo.P_WORK_AREA }" /> <input type="hidden"
			name="job.HR_ID" value="${positionReqInfo.HR_ID }" /><input
			type="hidden" name="req_id" value="${positionReqInfo.REQ_ID }" />
		<div class="easyui-panel" title="就业信息" style="width: 100%;"
			id="jobInfoDiv" data-option="closed:true">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">入职时间:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-datebox" name="job.JOB_TIME"
						data-options="sharedCalendar:'#cc', required:true"
						style="width: 100%"></td>
					<td style="width: 10%; text-align: right">工作地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="GZ_AREA_NAME"
						value="${positionReqInfo.P_WORK_AREA_NAME }"
						name="job.JOB_AREA_NAME"
						data-options="buttonText:'选择',onClickButton:showGzAreaTree,onClick:showGzAreaTree, editable:false, required:true"
						style="width: 100%"></td>
					<td style="width: 10%; text-align: right">工作类型:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="job.JOB_TYPE" value="014"
						style="width: 100%"
						data-options="
					url:'frame/loadCode.do?codeName=job_type',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false, required:true" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">单位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="job.JOB_DW" style="width: 100%"
						value="${positionReqInfo.C_NAME }"></td>
					<td style="width: 10%; text-align: right">岗位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="job.JOB_GW"
						value="${positionReqInfo.P_NAME }" style="width: 100%"></td>
					<td style="width: 10%; text-align: right">月收入:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="job.INCOME" style="width: 100%"
						value="${positionReqInfo.P_PAY_BOTTON }"
						data-options="prompt:'元/月'" /></td>
				</tr>
			</table>
		</div>

		<div style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#theForm').form('submit');" style="width: 80px">保存</a>
		</div>
	</form>

	<div id="cc" class="easyui-calendar" data-options="closed:true"></div>

	<div id="dlgGzArea" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#gzButtons'">
		<ul id="areaGzTree" class="easyui-tree">
		</ul>
		<div id="gzButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="cleanGz()" style="width: 80px">清除</a>
		</div>
	</div>

	<script type="text/javascript">
		function showGzAreaTree() {
			$('#dlgGzArea').dialog('open');
		}

		function cleanGz() {
			$("#GZ_AREA_NAME").textbox("setValue", "");
			$("#GZ_AREA").val('');
			$('#dlgGZArea').dialog('close');
		}

		$(function() {
			$('#areaGzTree').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree'
				},
				onClick : function(node) {
					$("#GZ_AREA_NAME").textbox("setValue", node.text);
					$("#GZ_AREA").val(node.id);
					$('#dlgGzArea').dialog('close');
				}
			});
			$('#theForm').form({
				url : 'busi/position/saveJobInfo.do',
				success : function(data) {
					try {
						var rst = eval('(' + data + ')');
						if (rst.isSucc) {
							$.messager.alert('提示', '操作成功');
							$('#dd').dialog('close');
						} else {
							var msg = '操作失败';
							if (rst.info.INFO_KEY_DEFAULT != null) {
								msg = msg + ',' + rst.info.INFO_KEY_DEFAULT;
							}
							showOprTip('oprJobRegTip', msg, 'red');
						}
					} catch (e) {
						alert(e);
					}
				},
				onSubmit : function() {
					closeOrpTip('oprJobRegTip');
				}
			});

		});
	</script>


</body>
</html>