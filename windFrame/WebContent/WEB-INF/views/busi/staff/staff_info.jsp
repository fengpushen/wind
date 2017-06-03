<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>账号管理</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div id="oprTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:false, closed:true, border:false"></div>
	<form id="theForm" method="post" style="width: 100%">
		<input type="hidden" name="staffInfo.STAFF_ID"
			value="${staffInfo.STAFF_ID }" /><input type="hidden"
			name="accountInfo.ACCOUNT_ID"
			value="${requestScope.accountInfo.ACCOUNT_ID }" /> <input
			type="hidden" id="AREA_CODE" name="staffInfo.AREA_CODE"
			value="${staffInfo.AREA_CODE }" />
		<div class="easyui-panel"
			style="width: 100%; background-color: #E0EEEE;"
			data-options="noheader:true" id="infoDiv">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">账号:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="accountInfo.ACCOUNT"
						value="${requestScope.accountInfo.ACCOUNT }" style="width: 100%"
						data-options="required:true, validType:['length[2, 20]']" /></td>

					<td style="width: 10%; text-align: right">密码:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-passwordbox" name="accountInfo.PASSWORD"
						style="width: 100%" data-options="prompt:'${pswPrompt }'" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">用户名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="staffInfo.STAFF_NAME"
						value="${staffInfo.STAFF_NAME }" style="width: 100%"
						data-options="required:true, validType:'length[2,20]'" /></td>
					<td style="width: 10%; text-align: right">行政区划:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="AREA_NAME"
						value="${staffInfo.AREA_NAME }"
						data-options="buttonText:'选择',onClickButton:showAreaTree,onClick:showAreaTree, editable:false,required:true"
						style="width: 100%"></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">部门名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="staffInfo.UNIT_NAME"
						value="${staffInfo.UNIT_NAME }" style="width: 100%"
						data-options="required:true" /></td>
					<td style="width: 10%; text-align: right">联系电话:</td>
					<td style="width: 23%; text-align: left"><input
						value="${staffInfo.PHONE }" class="easyui-textbox"
						name="staffInfo.PHONE" style="width: 100%"></td>

				</tr>
				<tr>
					<td style="width: 10%; text-align: right">权限:</td>
					<td style="width: 80%; text-align: left" colspan="2"><input
						class="easyui-combobox" id="roles" name="accountInfo.ROLES"
						style="width: 90%" /></td>
				</tr>
			</table>
		</div>

		<div style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#theForm').form('submit');" style="width: 80px">保存</a>
		</div>
	</form>

	<div id="dlg" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#hjButtons'">
		<ul id="areaTree" class="easyui-tree">
		</ul>
		<div id="hjButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="cleanHj()" style="width: 80px">清除</a>
		</div>
	</div>



	<script type="text/javascript">
		function showAreaTree() {
			$('#dlg').dialog('open');
		}

		function cleanHj() {
			$("#AREA_NAME").textbox("setValue", "");
			$("#AREA_CODE").val('');
			$('#dlg').dialog('close');
		}

		$(function() {
			try {
				var staff_id = "${staffInfo.STAFF_ID}";
				$("#infoDiv").click(function() {
					$('#oprTip').panel('close');
				});
				var roleMgds = '${roleMgds}';
				$("#roles")
						.combobox(
								{
									url : 'frame/loadAccountRolesMgd.do',
									method : 'post',
									valueField : 'ROLE_ID',
									textField : 'ROLE_NAME',
									multiple : true,
									editable : false,
									required : true,
									onLoadSuccess : function() {
										if (roleMgds != '') {
											var roles = eval('(' + roleMgds
													+ ')');
											var val = $(this).combobox(
													"getData");
											for (var i = 0; i < val.length; i++) {
												for (var j = 0; j < roles.length; j++) {
													if (roles[j].ROLE_ID == val[i].ROLE_ID) {
														$(this).combobox(
																"select",
																val[i].ROLE_ID);
														break;
													}
												}
											}
										}
									}
								});
				var accountArea = '${accountInfo.staffInfo.AREA_CODE}';
				$('#areaTree').tree({
					url : 'busi/common/loadTree.do',
					method : 'post',
					queryParams : {
						'treeName' : 'busi_com_area_tree',
						'rootId' : accountArea
					},
					onClick : function(node) {
						$("#AREA_NAME").textbox("setValue", node.text);
						$("#AREA_CODE").val(node.id);
						$('#dlg').dialog('close');
					}
				});
				$('#theForm').form(
						{
							url : 'busi/staff/saveStaffInfo.do',
							success : function(data) {
								var rst = eval('(' + data + ')');
								if (rst.isSucc) {
									if (staff_id == '') {
										loadDatagridData();
										showOprTip("oprTip", "操作成功，你可继续添加下一条",
												'green');
										$('#theForm').form("clear");
									} else {
										$.messager.alert('提示', '操作成功');
										$('#dd').dialog('close');
									}
								} else {
									var msg = '操作失败';
									if (rst.info.INFO_KEY_DEFAULT != null) {
										msg = msg + ','
												+ rst.info.INFO_KEY_DEFAULT;
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