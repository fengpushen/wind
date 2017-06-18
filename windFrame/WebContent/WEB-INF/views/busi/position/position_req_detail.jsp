<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>个人详情</title>
<tags:commonHead />
</head>
<body>
	<div id="oprTipMge" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="noheader:true, closed:true, border:false"></div>
	<div class="easyui-panel"
		style="width: 100%; background-color: #E0EEEE;"
		data-options="noheader:true" id="infoDiv">
		<div style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton" id="passBtn"
				style="width: 80px">符合条件</a>&nbsp;&nbsp; <a
				href="javascript:void(0)" class="easyui-linkbutton" id="noticeBtn"
				style="width: 80px">已通知</a>&nbsp;&nbsp; <a href="javascript:void(0)"
				class="easyui-linkbutton" id="nopassBtn" style="width: 80px">忽略</a>
		</div>
		<table style="width: 100%">
			<tr>
				<td style="width: 10%; text-align: right">身份证:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.IDCARD_COVERD }"
					style="width: 100%" data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">姓名:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.HR_NAME }"
					style="width: 100%" data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">户籍地:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.HJ_AREA_NAME }"
					style="width: 100%" data-options="disabled:true"></td>
			</tr>
			<tr>
				<td style="width: 10%; text-align: right">文化程度:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.DEGREE_NAME }"
					style="width: 100%" data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">毕业学校:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.SCHOOL }"
					style="width: 100%" data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">毕业年份:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.GRA_YEAR }"
					style="width: 100%" data-options="disabled:true" /></td>

			</tr>
			<tr>
				<td style="width: 10%; text-align: right">所学专业:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.PRO }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">家庭地址:</td>
				<td style="width: 56%; text-align: left" colspan="3"><input
					class="easyui-textbox" value="${hrInfo.ADDRESS }"
					style="width: 95%" data-options="disabled:true" /></td>
			</tr>
			<tr>
				<td style="width: 10%; text-align: right">联系电话:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.PHONE }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">QQ:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.QQ }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">电子邮箱:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="EMAIL" id="EMAIL" style="width: 100%"
					value="${hrInfo.EMAIL }" data-options="disabled:true" /></td>
			</tr>
			<tr>
				<td style="width: 10%; text-align: right">民族:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="NATION" id="nation"
					value="${hrInfo.NATION_NAME }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">已就业:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.IS_JOB_NAME }"
					style="width: 100%" data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">有就业意愿:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${hrInfo.IS_WANT_JOB_NAME }"
					style="width: 100%" data-options="disabled:true" /></td>
				<td style="width: 33%; text-align: right" colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td style="width: 10%; text-align: right">技能特长:</td>
				<td style="width: 56%; text-align: left" colspan="3"><input
					class="easyui-textbox" name="JNTC" style="width: 95%"
					data-options="disabled:true" value="${hrInfo.JNTC }" /></td>
			</tr>
		</table>


	</div>
	<div class="easyui-panel" title="入离职信息" style="width: 100%;">
		<table id="datagridJob">
		</table>
	</div>

	<script type="text/javascript">
		function changeReqStatus(req_id, status) {
			$.ajax({
				type : "post",
				url : "busi/position/changeReqStatus.do",
				dataType : "json",
				data : {
					'req_id' : req_id,
					'req_status' : status
				},
				success : function(rst) {
					if (rst.isSucc) {
						$.messager.alert("", "操作成功");
						$('#dd').dialog('close');
					} else {
						var msg = '操作失败';
						if (rst.info.INFO_KEY_DEFAULT != null) {
							msg = msg + ',' + rst.info.INFO_KEY_DEFAULT;
						}
						$.messager.alert("", msg);
					}
					loadDatagridData();
				}
			});
		}
		$(function() {
			try {
				var req_id = '${req_id}';
				$("#passBtn").bind('click', function() {
					changeReqStatus(req_id, '02');
				});
				$("#noticeBtn").bind('click', function() {
					changeReqStatus(req_id, '03');
				});
				$("#nopassBtn").bind('click', function() {
					changeReqStatus(req_id, '04');
				});
				var hr_id = '${hrInfo.HR_ID}';
				$('#datagridJob').datagrid({
					method : 'POST',
					rownumbers : true,
					iconCls : 'icon-ok',
					pagination : true,
					striped : true,
					url : 'busi/hr/loadHrJobList.do',
					queryParams : {
						"hr_id" : hr_id
					},
					singleSelect : false,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					frozenColumns : [ [ {
						field : 'JOB_TIME',
						title : '入/离职时间',
						width : '10%',
						align : 'center'
					}, {
						field : 'IS_JOB_NAME',
						title : '类型',
						width : '5%',
						align : 'center'
					} ] ],
					columns : [ [ {
						"title" : "入职信息",
						"colspan" : 5
					}, {
						"title" : "离职信息",
						"colspan" : 2
					} ], [ {
						field : 'JOB_AREA_NAME',
						title : '工作地',
						width : '8%',
						align : 'center'
					}, {
						field : 'JOB_TYPE_NAME',
						title : '工作类型',
						width : '8%',
						align : 'center'
					}, {
						field : 'JOB_DW',
						title : '工作单位',
						width : '15%',
						align : 'center'
					}, {
						field : 'JOB_GW',
						title : '工作岗位',
						width : '10%',
						align : 'center'
					}, {
						field : 'INCOME',
						title : '月收入(元)',
						width : '10%',
						align : 'center'
					}, {
						field : 'NOJOB_DW',
						title : '离职单位',
						width : '15%',
						align : 'center'
					}, {
						field : 'NOJOB_REASON',
						title : '离职时间',
						width : '15%',
						align : 'center'
					} ] ],
				});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>