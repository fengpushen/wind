<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>招聘岗位</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div id="oprTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="closed:true, border:false"></div>
	<form id="theForm" method="post" style="width: 100%">
		<input type="hidden" name="P_ID" value="${P_ID }" /> <input
			type="hidden" id="P_WORK_AREA" name="P_WORK_AREA"
			value="${P_WORK_AREA }" />
		<div class="easyui-panel" title="岗位信息"
			style="width: 100%; background-color: #E0EEEE;" id="infoDiv"
			data-options="noheader:true">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">招聘单位:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" id="C_ID" name="C_ID" style="width: 100%"
						value="${ C_ID}" /></td>
					<td style="width: 10%; text-align: right">直接使用单位招聘信息:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="P_DW_ZPR" id="P_DW_ZPR"
						style="width: 100%" value="${ P_DW_ZPR}"></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">岗位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_NAME" style="width: 100%"
						data-options="required:true, validType:'length[2,20]'"
						value="${P_NAME }" /></td>
					<td style="width: 10%; text-align: right">招聘人数:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_NUM" style="width: 100%"
						data-options="required:true" value="${P_NUM }" /></td>
					<td style="width: 10%; text-align: right">学历（及以上）:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="P_DEGREE" style="width: 100%"
						data-options="
					url:'frame/loadCode.do?codeName=degree',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false"
						value="${ P_DEGREE}" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">年龄下限:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_AGE_BOTTOM" style="width: 100%"
						value="${P_AGE_BOTTOM }"></td>
					<td style="width: 10%; text-align: right">年龄上限:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_AGE_TOP" style="width: 100%"
						value="${P_AGE_TOP }"></td>
					<td style="width: 10%; text-align: right">截止时间:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-datebox" name="END_TIME"
						data-options="sharedCalendar:'#cc',required:true,prompt:'包含'"
						style="width: 100%" value="${END_TIME }"></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">工作地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="P_WORK_AREA_NAME" style="width: 100%"
						value="${P_WORK_AREA_NAME }"></td>
					<td style="width: 10%; text-align: right">月工资下限:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_PAY_BOTTON" style="width: 100%"
						value="${P_PAY_BOTTON }" data-options="prompt:'元',required:true" /></td>
					<td style="width: 10%; text-align: right">月工资上限:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_PAY_TOP" style="width: 100%"
						value="${P_PAY_TOP }" data-options="prompt:'元'" /></td>
				</tr>

				<tr id="positionRow">
					<td style="width: 10%; text-align: right">招聘负责人:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_ZPR" style="width: 100%"
						value="${P_ZPR }"></td>
					<td style="width: 10%; text-align: right">招聘联系电话:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_ZPDH" style="width: 100%"
						value="${P_ZPDH }"></td>
				</tr>
				<tr id="comRow">
					<td style="width: 10%; text-align: right">招聘负责人:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" style="width: 100%"
						data-options="disabled:true" id="C_ZPR" value="${comInfo.C_ZPR }"></td>
					<td style="width: 10%; text-align: right">招聘联系电话:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" style="width: 100%"
						data-options="disabled:true" id="C_ZPDH"
						value="${comInfo.C_ZPDH }"></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">其他说明:</td>
					<td style="width: 80%; text-align: left" colspan="5"><input
						class="easyui-textbox" name="P_DETAIL"
						data-options="multiline:true" style="width: 95%; height: 150px;"
						value="${P_DETAIL }" /></td>
				</tr>
			</table>
		</div>

		<div style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#theForm').form('submit');" style="width: 80px">保存</a>
		</div>
	</form>

	<div id="cc" class="easyui-calendar" data-options="closed:true"></div>

	<div id="dlg" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#hjButtons'">
		<ul id="areaTree" class="easyui-tree">
		</ul>
		<div id="hjButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanAreaBtn" style="width: 80px">清除</a>
		</div>
	</div>

	<script type="text/javascript">
		function p_dw_zprChange() {
			try {
				if (this.value == '0') {
					$("#comRow").hide();
					$("#positionRow").show();
				} else {
					$("#comRow").show();
					$("#positionRow").hide();
				}
			} catch (e) {
				alert(e);
			}
		}
		function cidChange() {
			var cid = this.value;
			$
					.ajax({
						type : "post",
						url : "busi/company/loadComInfo.do?cid=" + cid,
						success : function(data) {
							var rst = eval('(' + data + ')');
							if (rst.isSucc) {
								$("#C_ZPR").textbox("setValue",
										rst.info.comInfo.C_ZPR);
								$("#C_ZPDH").textbox("setValue",
										rst.info.comInfo.C_ZPDH);
							}
						}
					});
		}
		$(function() {
			var P_ID = "${P_ID}";
			$("#infoDiv").click(function() {
				$('#oprTip').panel('close');
			});
			var areaTree = new AreaTree('dlg', 'P_WORK_AREA',
					'P_WORK_AREA_NAME');
			$('#areaTree').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree'
				},
				onClick : function(node) {
					areaTree.nodeClick(node);
				}
			});
			$('#P_WORK_AREA_NAME').textbox({
				buttonText : '选择',
				onClickButton : function() {
					areaTree.showAreaTree();
				},
				onClick : function() {
					areaTree.showAreaTree();
				},
				editable : false,
				required : true
			});
			$("#cleanAreaBtn").bind('click', function() {
				areaTree.cleanChosed();
			});
			$("#C_ID").combobox({
				url : 'busi/company/loadCompanyMgd.do',
				method : 'post',
				valueField : 'C_ID',
				textField : 'C_NAME',
				editable : false,
				required : true,
				onLoadSuccess : function() {
					var val = $(this).combobox("getData");
					if (val.length == 1) {
						$(this).combobox("select", val[0].C_ID);
					}
				},
				onChange : cidChange
			});
			$("#P_DW_ZPR").combobox({
				url : 'frame/loadCode.do?codeName=boolean',
				method : 'post',
				valueField : 'id',
				textField : 'text',
				panelHeight : 'auto',
				editable : false,
				required : true,
				onChange : p_dw_zprChange,
				onLoadSuccess : function() {
					if (this.value == '') {
						$(this).combobox("select", "1");
					}
					p_dw_zprChange();
				}
			});
			$('#theForm').form({
				url : 'busi/position/saveComPositionInfo.do',
				success : function(data) {
					var rst = eval('(' + data + ')');
					if (rst.isSucc) {
						if (P_ID == '') {
							loadDatagridData();
							showOprTip("oprTip", "操作成功，你可继续添加下一条", 'green');
							var cid = $("#C_ID").combobox("getValue");
							$('#theForm').form("clear");
							$("#C_ID").combobox("select", cid);
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