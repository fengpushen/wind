<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>人力资源信息修改</title>
<tags:commonHead />
</head>
<body>
	<div id="oprTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="closed:true, border:false"></div>
	<form id="theForm" method="post" style="width: 100%">
		<input type="hidden" name="HR_ID" value="${HR_ID }" /> <input
			type="hidden" id="HJ_AREA" name="HJ_AREA" value="${HJ_AREA }" /> <input
			type="hidden" id="WANT_JOB_AREA" name="want_job.WANT_JOB_AREA"
			value="${WANT_JOB_AREA }" />
		<div class="easyui-panel" style="background-color: #E0EEEE;"
			data-options="noheader:true" id="infoDiv">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">身份证:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="IDCARD" style="width: 100%"
						data-options="required:true, validType:['length[18, 18]', 'validIdcard']"
						value="${IDCARD }" /></td>
					<td style="width: 10%; text-align: right">姓名:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="HR_NAME" style="width: 100%"
						data-options="required:true, validType:'length[2,20]'"
						value="${HR_NAME }" /></td>
					<td style="width: 10%; text-align: right">户籍地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="HJ_AREA_NAME" style="width: 100%"
						value="${HJ_AREA_NAME }"></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">文化程度:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="DEGREE" id="DEGREE"
						style="width: 100%"
						data-options="
					url:'frame/loadCode.do?codeName=degree',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false"
						value="${DEGREE }" /></td>
					<td style="width: 10%; text-align: right">毕业学校:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="SCHOOL" id="SCHOOL"
						style="width: 100%" value="${SCHOOL }" /></td>
					<td style="width: 10%; text-align: right">毕业年份:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="GRA_YEAR" id="GRA_YEAR"
						style="width: 100%" value="${GRA_YEAR }" /></td>

				</tr>
				<tr>
					<td style="width: 10%; text-align: right">所学专业:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="PRO" style="width: 100%"
						value="${PRO }" /></td>
					<td style="width: 10%; text-align: right">家庭地址:</td>
					<td style="width: 56%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="ADDRESS" value="${ADDRESS }"
						style="width: 95%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">联系电话:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="PHONE" id="PHONE" style="width: 100%"
						value="${PHONE }" /></td>
					<td style="width: 10%; text-align: right">QQ:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="QQ" id="QQ" style="width: 100%"
						value="${QQ }" /></td>
					<td style="width: 10%; text-align: right">电子邮箱:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="EMAIL" id="EMAIL" style="width: 100%"
						value="${EMAIL }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">民族:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="NATION" id="nation"
						style="width: 100%"
						data-options="
					url:'frame/loadCode.do?codeName=nation',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:200, editable:false"
						value="${NATION }" /></td>
					<td style="width: 10%; text-align: right">是否为劳动力:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="LD_TYPE" style="width: 100%" value="${LD_TYPE }"
						data-options="
					url:'frame/loadCode.do?codeName=boolean',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false" /></td>
					<td style="width: 10%; text-align: right">有就业意愿:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="IS_WANT_JOB" id="IS_WANT_JOB_Mdy"
						style="width: 100%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">技能特长:</td>
					<td style="width: 56%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="JNTC" style="width: 95%"
						data-options="prompt:'多个技能特长之间用逗号分开'" value="${JNTC }" /></td>
				</tr>
			</table>


		</div>

		<div id="yyInfoDiv" class="easyui-panel" title="就业意愿信息"
			style="background-color: #E0EEEE;" data-option="closed:true">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">意向工作地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="WANT_JOB_AREA_NAME"
						name="want_job.JOB_AREA_NAME" style="width: 100%"
						value="${WANT_JOB_AREA_NAME }"></td>
					<td style="width: 10%; text-align: right">意向待遇:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="WANT_INCOME" value="${WANT_INCOME }"
						style="width: 100%" data-options="prompt:'元/月'" /></td>
					<td style="width: 33%; text-align: right" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">意向职位:</td>
					<td style="width: 46%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="WANT_JOB_NAME" id="WANT_JOB_NAME"
						style="width: 80%" data-options="prompt:'多个职位之间用逗号分开'"
						value="${WANT_JOB_NAME }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">就业服务需求:</td>
					<td style="width: 46%; text-align: left" colspan="3"><input
						class="easyui-combobox" name="want_job.service_codes"
						id="service_codes" style="width: 80%" /></td>
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
				id="cleanHjBtn" style="width: 80px">清除</a>
		</div>
	</div>


	<div id="dlgWantArea" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#wantButtons'">
		<ul id="areaWantTree" class="easyui-tree">
		</ul>
		<div id="wantButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanWantBtn" style="width: 80px">清除</a>
		</div>
	</div>



	<script type="text/javascript">
		$(function() {
			$("#infoDiv").click(function() {
				$('#oprTip').panel('close');
			});
			$("#yyInfoDiv").click(function() {
				$('#oprTip').panel('close');
			});
			var areaTree = new AreaTree('dlg', 'HJ_AREA', 'HJ_AREA_NAME');
			var areaTreeWant = new AreaTree('dlgWantArea', 'WANT_JOB_AREA',
					'WANT_JOB_AREA_NAME');

			var accountArea = '${accountInfo.staffInfo.AREA_CODE}';
			$('#HJ_AREA_NAME').textbox({
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
			$('#areaTree').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree',
					'rootId' : accountArea
				},
				onClick : function(node) {
					areaTree.nodeClick(node);
				}
			});

			$('#WANT_JOB_AREA_NAME').textbox({
				buttonText : '选择',
				onClickButton : function() {
					areaTreeWant.showAreaTree();
				},
				onClick : function() {
					areaTreeWant.showAreaTree();
				},
				editable : false
			});
			$('#areaWantTree').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree'
				},
				onClick : function(node) {
					areaTreeWant.nodeClick(node);
				}
			});
			$("#cleanHjBtn").bind('click', function() {
				areaTree.cleanChosed();
			});
			$("#cleanWantBtn").bind('click', function() {
				areaTreeWant.cleanChosed();
			});
			var IS_WANT_JOB = "${IS_WANT_JOB }";
			$("#IS_WANT_JOB_Mdy")
					.combobox(
							{
								url : 'frame/loadCode.do?codeName=boolean',
								method : 'post',
								valueField : 'id',
								textField : 'text',
								panelHeight : 'auto',
								editable : false,
								onChange : function() {
									if (this.value == '0') {
										$('#yyInfoDiv').panel('close');
									} else {
										$('#yyInfoDiv').panel('open');
									}
								},
								onLoadSuccess : function() {
									$(this).combobox("select", IS_WANT_JOB);
								},
								required : true,
								validType : 'equalTriggerRequired["是", "WANT_JOB_AREA_NAME", "WANT_JOB_NAME"]',
								invalidMessage : '意向工作地、意向职位必须填写'
							});
			var needServicesJson = '${needServicesJson}';
			$("#service_codes").combobox({
				url : 'frame/loadCode.do?codeName=service_code',
				method : 'post',
				valueField : 'id',
				textField : 'text',
				multiple : true,
				panelHeight : '200',
				editable : false,
				onLoadSuccess : function() {
					if (needServicesJson != '') {
						var services = eval('(' + needServicesJson + ')');
						var val = $(this).combobox("getData");
						for (var i = 0; i < val.length; i++) {
							for (var j = 0; j < services.length; j++) {
								if (services[j].SERVICE_CODE == val[i].id) {
									$(this).combobox("select", val[i].id);
									break;
								}
							}
						}
					}
				}
			});
			$('#theForm').form({
				url : 'busi/hr/updateHrInfoStaff.do',
				success : function(data) {
					var rst = eval('(' + data + ')');
					if (rst.isSucc) {
						loadDatagridData();
						$.messager.alert('提示', '操作成功');
						$('#dd').dialog('close');
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