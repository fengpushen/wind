<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>人力资源信息管理</title>
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
		<input type="hidden" name="hr_id" value="${HR_ID }" /> <input
			type="hidden" id="hj_area" name="hj_area" value="${HJ_AREA}" /><input
			type="hidden" id="job_area" name="job.job_area"
			value="${job.JOB_AREA}" />
		<div class="easyui-panel" style="width: 100%"
			data-options="noheader:true" id="infoDiv">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; background-color: #F0F8FF;" rowspan="4">个人基本信息</td>
					<td style="width: 10%; text-align: right">身份证号码:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="idcard" id="idcard"
						style="width: 100%"
						data-options="required:true, validType:['length[18, 18]', 'validIdcard']"
						value="${IDCARD }" /></td>
					<td style="width: 10%; text-align: right">姓名:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="hr_name" style="width: 100%"
						data-options="required:true" value="${HR_NAME }" /></td>
					<td style="width: 10%; text-align: right">户籍:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="hj_area_name" id="hj_area_name"
						value="${HJ_AREA_NAME}" style="width: 100%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">民族:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="nation" id="nation"
						style="width: 100%" value="${NATION }" /></td>
					<td style="width: 10%; text-align: right">政治面貌:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="political_status"
						id="political_status" style="width: 100%"
						value="${POLITICAL_STATUS }" /></td>
					<td style="width: 10%; text-align: right">婚姻状况:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="marry_status" id="marry_status"
						style="width: 100%" value="${MARRY_STATUS }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">学历:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="degree" id="degree"
						style="width: 100%" value="${DEGREE }" /></td>
					<td style="width: 10%; text-align: right">联系电话:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="phone" style="width: 100%"
						data-options="required:true" value="${PHONE }" /></td>
					<td style="width: 10%; text-align: right">QQ:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="qq" style="width: 100%"
						value="${QQ }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">户口性质:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="hj_type" id="hj_type"
						style="width: 100%" value="${HJ_TYPE }" /></td>
					<td style="width: 10%; text-align: right">是否贫困户:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="is_poor" id="is_poor"
						style="width: 100%" value="${IS_POOR }" /></td>
					<td style="width: 10%; text-align: right">是否就业:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="is_job" id="is_job"
						style="width: 100%" value="${IS_JOB }" /></td>
				</tr>
				<tr style="background-color: #F0F8FF">
					<td style="width: 100%; font-size: 18px; text-align: center"
						colspan="7">劳动技能信息</td>
				</tr>
				<tr>
					<td style="width: 10%; background-color: #F0F8FF;" rowspan="2">劳动技能信息</td>
					<td style="width: 10%; text-align: right">技能类型:</td>
					<td style="width: 50%; text-align: left" colspan="3"><input
						class="easyui-textbox" name="jntc" style="width: 90%"
						data-options="prompt:'多个技能类型之间用逗号分开'" value="${JNTC }" /></td>
					<td style="width: 10%; text-align: right">职业资格证书:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="job_lv" id="job_lv"
						style="width: 100%" value="${JOB_LV }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">培训需求意向:</td>
					<td style="width: 50%; text-align: left" colspan="3"><input
						class="easyui-combobox" name="want_train_type"
						id="want_train_type" style="width: 90%" /></td>
				</tr>
				<tr style="background-color: #F0F8FF">
					<td style="width: 100%; font-size: 18px; text-align: center"
						colspan="7">已就业信息</td>
				</tr>
				<tr>
					<td style="width: 10%; background-color: #F0F8FF;" rowspan="2">已就业信息</td>
					<td style="width: 10%; text-align: right">就业单位:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="job.job_dw" id="job_dw"
						style="width: 100%" value="${job.JOB_DW }" /></td>
					<td style="width: 10%; text-align: right">就业时间:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-datebox" name="job.job_time" id="job_time"
						data-options="sharedCalendar:'#cc'" style="width: 100%"
						value="${job.JOB_TIME }" /></td>
					<td style="width: 10%; text-align: right">就业岗位:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="job.job_gw" id="job_gw"
						style="width: 100%" value="${job.JOB_GW }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">就业地点:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="job.job_area_name" id="job_area_name"
						style="width: 100%" value="${job.JOB_AREA_NAME }" /></td>
					<td style="width: 10%; text-align: right">从事产业:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="job.job_industry" id="job_industry"
						style="width: 100%" value="${job.JOB_INDUSTRY }" /></td>
					<td style="width: 10%; text-align: right">就业形式:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="job.job_type" id="job_type"
						style="width: 100%" value="${job.JOB_TYPE }" /></td>
				</tr>
				<tr style="background-color: #F0F8FF">
					<td style="width: 100%; font-size: 18px; text-align: center"
						colspan="7">暂未就业信息</td>
				</tr>
				<tr>
					<td style="width: 10%; background-color: #F0F8FF;" rowspan="3">暂未就业信息</td>
					<td style="width: 10%; text-align: right">就业意愿:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="is_want_job" id="is_want_job"
						style="width: 100%" value="${IS_WANT_JOB }" /></td>
					<td style="width: 10%; text-align: right">人员身份:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="hr_kind" id="hr_kind"
						style="width: 100%" value="${HR_KIND }" /></td>
					<td style="width: 10%; text-align: right">困难群体:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="hard_type" id="hard_type"
						style="width: 100%" value="${HARD_TYPE }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">工种意愿:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="want_job_name" id="want_job_name"
						style="width: 100%" value="${WANT_JOB_NAME }" /></td>
					<td style="width: 10%; text-align: right">月工资期望:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="want_income" id="want_income"
						style="width: 100%" value="${WANT_INCOME }" /></td>
					<td style="width: 10%; text-align: right">产业意向:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="want_industry" id="want_industry"
						style="width: 100%" value="${WANT_INDUSTRY }" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">区域意向:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-textbox" name="want_work_area_kind"
						id="want_work_area_kind" style="width: 100%"
						value="${WANT_WORK_AREA_KIND }" /></td>
					<td style="width: 10%; text-align: right">就业形式意向:</td>
					<td style="width: 20%; text-align: left"><input
						class="easyui-combobox" name="want_job_type" id="want_job_type"
						style="width: 100%" value="${WANT_JOB_TYPE }" /></td>
				</tr>
				
				<tr style="background-color: #F0F8FF">
					<td style="width: 100%; font-size: 18px; text-align: center"
						colspan="7">参加社会保险信息</td>
				</tr>
				<tr>
					<td style="width: 10%; background-color: #F0F8FF;">参保信息</td>
					<td style="width: 10%; text-align: right">&nbsp;</td>
					<td style="width: 80%; text-align: left" colspan="5"><input
						class="easyui-combobox" name="cbxx" id="cbxx" style="width: 90%" /></td>

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
				id="cleanHjBtn" style="width: 80px">清除</a>
		</div>
	</div>

	<div id="dlgJobArea" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#jobButtons'">
		<ul id="areaJobTree" class="easyui-tree">
		</ul>
		<div id="jobButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanJobBtn" style="width: 80px">清除</a>
		</div>
	</div>



	<script type="text/javascript">
		$(function() {
			try {
				$("body").click(function() {
					$('#oprTip').panel('close');
				});
				comboboxDefaultInit('degree', 'degree', true);
				comboboxDefaultInit('nation', 'nation', true, 200);
				comboboxDefaultInit('political_status', 'political_status',
						true);
				comboboxDefaultInit('hj_type', 'hj_type', true);
				comboboxDefaultInit('marry_status', 'marry_status', true);
				comboboxDefaultInit('is_poor', 'boolean', true);
				comboboxDefaultInit('job_lv', 'job_lv', false, 'auto', false,
						true);
				comboboxDefaultInit('job_industry', 'industry');
				comboboxDefaultInit('job_type', 'job_type');
				comboboxDefaultInit('is_want_job', 'boolean');
				comboboxDefaultInit('hr_kind', 'hr_kind');
				comboboxDefaultInit('hard_type', 'hard_type', false, 'auto',
						false, true);
				comboboxDefaultInit('want_industry', 'industry', false, 'auto',
						false, true, {
							"text" : "均可",
							"id" : ""
						});
				comboboxDefaultInit('want_work_area_kind', 'area_kind', false,
						'auto', false, true, {
							"text" : "均可",
							"id" : ""
						});
				comboboxDefaultInit('want_job_type', 'job_type', false, 'auto',
						false, true, {
							"text" : "均可",
							"id" : ""
						});

				var wantTrainTypesJson = '${wantTrainTypesJson}';
				$("#want_train_type")
						.combobox(
								{
									url : 'frame/loadCode.do?codeName=train_type',
									method : 'post',
									valueField : 'id',
									textField : 'text',
									multiple : true,
									panelHeight : 'auto',
									editable : false,
									onLoadSuccess : function() {
										if (wantTrainTypesJson != '') {
											var services = eval('('
													+ wantTrainTypesJson + ')');
											var val = $(this).combobox(
													"getData");
											for (var i = 0; i < val.length; i++) {
												for (var j = 0; j < services.length; j++) {
													if (services[j].TRAIN_TYPE == val[i].id) {
														$(this).combobox(
																"select",
																val[i].id);
														break;
													}
												}
											}
										}
									}
								});
				var cbxxsJson = '${cbxxsJson}';
				$("#cbxx").combobox({
					url : 'frame/loadCode.do?codeName=bx_type',
					method : 'post',
					valueField : 'id',
					textField : 'text',
					multiple : true,
					panelHeight : 'auto',
					editable : false,
					onLoadSuccess : function() {
						if (cbxxsJson != '') {
							var services = eval('(' + cbxxsJson + ')');
							var val = $(this).combobox("getData");
							for (var i = 0; i < val.length; i++) {
								for (var j = 0; j < services.length; j++) {
									if (services[j].BX_TYPE == val[i].id) {
										$(this).combobox("select", val[i].id);
										break;
									}
								}
							}
						}
					}
				});

				var jobIds = [ "job_dw", "job_time", "job_gw", "job_area_name",
						"job_industry", "job_type" ];
				var wantJobIds = [ "want_job_name", "want_income",
						"want_industry", "want_work_area_kind",
						"want_job_type" ];

				var areaTree = new AreaTree('dlg', 'hj_area', 'hj_area_name');
				var accountArea = '${accountInfo.staffInfo.AREA_CODE}';
				$('#hj_area_name').textbox({
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
				$("#cleanHjBtn").bind('click', function() {
					areaTree.cleanChosed();
				});

				var areaTreeJob = new AreaTree('dlgJobArea', 'job_area',
						'job_area_name');
				$('#job_area_name').textbox({
					buttonText : '选择',
					onClickButton : function() {
						areaTreeJob.showAreaTree();
					},
					onClick : function() {
						areaTreeJob.showAreaTree();
					},
					editable : false
				});
				$('#areaJobTree').tree({
					url : 'busi/common/loadTree.do',
					method : 'post',
					queryParams : {
						'treeName' : 'busi_com_area_tree'
					},
					onClick : function(node) {
						areaTreeJob.nodeClick(node);
					}
				});
				$("#cleanJobBtn").bind('click', function() {
					areaTreeJob.cleanChosed();
				});
				$('#theForm').form({
					url : 'busi/hr/updateHrInfoStaff.do',
					success : function(data) {
						console.log(data);
						var rst = eval('(' + data + ')');
						console.log(rst);
						if (rst.isSucc) {
							loadDatagridData('datagrid');
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
				$("#is_job")
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
											disableEles(jobIds);
										} else {
											enableEles(jobIds);
										}
									},
									onLoadSuccess : function() {
										if (this.value == '0') {
											disableEles(jobIds);
										} else {
											enableEles(jobIds);
										}
									},
									required : true,
									validType : [
											'equalTriggerRequired["是", "job_dw", "job_time", "job_gw", "job_area_name", "job_area", "job_industry", "job_type"]' ],
									invalidMessage : '必须填写就业相关信息'
								});

				$("#is_want_job")
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
											disableEles(wantJobIds);
										} else {
											enableEles(wantJobIds);
										}
									},
									onLoadSuccess : function() {
										if (this.value == '0') {
											disableEles(wantJobIds);
										} else {
											enableEles(wantJobIds);
										}
									},
									required : true,
									validType : 'equalTriggerRequired["是", "want_job_name", "want_income"]',
									invalidMessage : '工种意愿、月工资期望必须填写'
								});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>