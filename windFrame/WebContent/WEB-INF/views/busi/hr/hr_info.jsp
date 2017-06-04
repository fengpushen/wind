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
	<div id="oprTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="closed:true, border:false"></div>
	<form id="theForm" method="post" style="width: 100%">
		<input type="hidden" id="HJ_AREA" name="HJ_AREA" /> <input
			type="hidden" id="GZ_AREA" name="job.JOB_AREA" /><input
			type="hidden" id="WANT_JOB_AREA" name="want_job.WANT_JOB_AREA" />
		<div class="easyui-panel" style="width: 100%"
			data-options="noheader:true" id="infoDiv">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">身份证:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="IDCARD" style="width: 100%"
						data-options="required:true, validType:['length[18, 18]', 'validIdcard']" /></td>
					<td style="width: 10%; text-align: right">姓名:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="HR_NAME" style="width: 100%"
						data-options="required:true, validType:'length[2,20]'" /></td>
					<td style="width: 10%; text-align: right">户籍地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="HJ_AREA_NAME" style="width: 100%"></td>
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
					panelHeight:'auto', editable:false" /></td>
					<td style="width: 10%; text-align: right">毕业学校:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="SCHOOL" id="SCHOOL"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">毕业年份:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="GRA_YEAR" id="GRA_YEAR"
						style="width: 100%" /></td>

				</tr>
				<tr>
					<td style="width: 10%; text-align: right">所学专业:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="PRO" id="PRO" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">家庭地址:</td>
					<td style="width: 56%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="ADDRESS" id="ADDRESS"
						style="width: 95%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">联系电话:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="PHONE" id="PHONE" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">QQ:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="QQ" id="QQ" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">电子邮箱:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="EMAIL" id="EMAIL" style="width: 100%" /></td>
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
					panelHeight:200, editable:false" /></td>
					<td style="width: 10%; text-align: right">已就业:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="IS_JOB" id="IS_JOB"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">有就业意愿:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="IS_WANT_JOB" id="IS_WANT_JOB"
						style="width: 100%" /></td>
					<td style="width: 33%; text-align: right" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">技能特长:</td>
					<td style="width: 56%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="JNTC" style="width: 95%"
						data-options="prompt:'多个技能特长之间用逗号分开'" /></td>
				</tr>
			</table>


		</div>
		<div class="easyui-panel" title="就业信息" style="width: 100%;"
			id="jobInfoDiv" data-option="closed:true">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">入职时间:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-datebox" name="job.JOB_TIME" id="JOB_TIME"
						data-options="sharedCalendar:'#cc'" style="width: 100%"></td>
					<td style="width: 10%; text-align: right">工作地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="GZ_AREA_NAME" name="job.JOB_AREA_NAME"
						style="width: 100%"></td>
					<td style="width: 10%; text-align: right">工作类型:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="job.JOB_TYPE" id="JOB_TYPE"
						style="width: 100%"
						data-options="
					url:'frame/loadCode.do?codeName=job_type',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:200, editable:false" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">单位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="job.JOB_DW" style="width: 100%"></td>
					<td style="width: 10%; text-align: right">岗位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="job.JOB_GW" style="width: 100%"></td>
					<td style="width: 10%; text-align: right">月收入:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="job.INCOME" style="width: 100%"
						data-options="prompt:'元/月'" /></td>
				</tr>
			</table>
		</div>
		<div id="yyInfoDiv" class="easyui-panel" title="就业意愿信息"
			style="width: 100%;" data-option="closed:true">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">意向工作地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="WANT_JOB_AREA_NAME"
						name="want_job.JOB_AREA_NAME" style="width: 100%"></td>
					<td style="width: 10%; text-align: right">意向待遇:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="want_job.WANT_INCOME"
						style="width: 100%" data-options="prompt:'元/月'" /></td>
					<td style="width: 33%; text-align: right" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">意向职位:</td>
					<td style="width: 46%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="want_job.WANT_JOB_NAME"
						id="WANT_JOB_NAME" style="width: 90%"
						data-options="prompt:'多个职位之间用逗号分开'" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">就业服务需求:</td>
					<td style="width: 46%; text-align: left" colspan="3"><input
						class="easyui-combobox" name="want_job.service_codes"
						id="service_codes" style="width: 90%"
						data-options="
					url:'frame/loadCode.do?codeName=service_code',
					method:'post',
					valueField:'id',
					textField:'text',
					multiple:true,
					panelHeight:'auto', editable:false" /></td>
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
				id="cleanHjBtn" onclick="cleanHj()" style="width: 80px">清除</a>
		</div>
	</div>

	<div id="dlgGzArea" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#gzButtons'">
		<ul id="areaGzTree" class="easyui-tree">
		</ul>
		<div id="gzButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanGzBtn" onclick="cleanGz()" style="width: 80px">清除</a>
		</div>
	</div>

	<div id="dlgWantArea" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#wantButtons'">
		<ul id="areaWantTree" class="easyui-tree">
		</ul>
		<div id="wantButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanWantBtn" onclick="cleanWant()" style="width: 80px">清除</a>
		</div>
	</div>



	<script type="text/javascript">
		$(function() {
			$("#infoDiv").click(function() {
				$('#oprTip').panel('close');
			});
			$("#jobInfoDiv").click(function() {
				$('#oprTip').panel('close');
			});
			$("#yyInfoDiv").click(function() {
				$('#oprTip').panel('close');
			});
			var areaTreeGz = new AreaTree('dlgGzArea', 'GZ_AREA',
					'GZ_AREA_NAME');
			var areaTree = new AreaTree('dlg', 'HJ_AREA', 'HJ_AREA_NAME');
			var areaTreeWant = new AreaTree('dlgWantArea', 'WANT_JOB_AREA',
					'WANT_JOB_AREA_NAME');
			$('#GZ_AREA_NAME').textbox({
				buttonText : '选择',
				onClickButton : function() {
					areaTreeGz.showAreaTree();
				},
				onClick : function() {
					areaTreeGz.showAreaTree();
				},
				editable : false
			});
			$('#areaGzTree').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree'
				},
				onClick : function(node) {
					areaTreeGz.nodeClick(node);
				}
			});

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
			$("#cleanGzBtn").bind('click', function() {
				areaTreeGz.cleanChosed();
			});
			$("#cleanWantBtn").bind('click', function() {
				areaTreeWant.cleanChosed();
			});
			$("#IS_JOB")
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
										$('#jobInfoDiv').panel('close');
									} else {
										$('#jobInfoDiv').panel('open');
									}
								},
								required : true,
								validType : 'equalTriggerRequired["是", "JOB_TIME", "GZ_AREA_NAME", "JOB_TYPE"]',
								invalidMessage : '入职时间、工作地点、工作类型必须填写'
							});
			$("#IS_WANT_JOB")
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
								required : true,
								validType : 'equalTriggerRequired["是", "WANT_JOB_AREA_NAME", "WANT_JOB_NAME"]',
								invalidMessage : '意向工作地、意向职位必须填写'
							});
			$('#JOB_TIME').datebox({
				sharedCalendar : '#cc'
			});
			$('#theForm').form({
				url : 'busi/hr/saveHrInfo.do',
				success : function(data) {
					var rst = eval('(' + data + ')');
					if (rst.isSucc) {
						loadDatagridData();
						showOprTip("oprTip", "操作成功，你可继续添加下一条", 'green');
						$('#theForm').form("clear");
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