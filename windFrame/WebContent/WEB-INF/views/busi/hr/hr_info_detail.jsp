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
	<div class="easyui-panel" style="width: 100%"
		data-options="noheader:true" id="infoDiv">
		<table style="width: 100%">
			<tr>
				<td style="width: 10%; text-align: right">姓名:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${HR_NAME }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">性别:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${SEX_NAME }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">年龄:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${AGE }" style="width: 100%"
					data-options="disabled:true" /></td>

			</tr>
			<tr>
				<td style="width: 10%; text-align: right">民族:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" name="NATION" id="nation"
					value="${NATION_NAME }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">文化程度:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${DEGREE_NAME }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">是否就业:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${IS_JOB_NAME }" style="width: 100%"
					data-options="disabled:true" /></td>

			</tr>
			<tr>
				<td style="width: 10%; text-align: right">技能特长:</td>
				<td style="width: 56%; text-align: left" colspan="3"><input
					class="easyui-textbox" name="JNTC" style="width: 95%"
					data-options="disabled:true" value="${JNTC }" /></td>
				<td style="width: 10%; text-align: right">技能等级:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${JOB_LV_NAME }" style="width: 100%"
					data-options="disabled:true" /></td>
			</tr>
			<tr>
				<td style="width: 10%; text-align: right">有就业意愿:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${IS_WANT_JOB_NAME }"
					style="width: 100%" data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">意向薪资:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${WANT_INCOME }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">意向职位:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${WANT_JOB_NAME }"
					style="width: 100%" data-options="disabled:true" /></td>
			</tr>
			<tr>

				<td style="width: 10%; text-align: right">户籍地:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${HJ_AREA_NAME }" style="width: 100%"
					data-options="disabled:true"></td>
				<td style="width: 10%; text-align: right">户籍地联系人:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${CONTRACTOR }" style="width: 100%"
					data-options="disabled:true" /></td>
				<td style="width: 10%; text-align: right">户籍地联系方式:</td>
				<td style="width: 23%; text-align: left"><input
					class="easyui-textbox" value="${CONTRACOR_PHONE }"
					style="width: 100%" data-options="disabled:true" /></td>
			</tr>
		</table>


	</div>
	<div class="easyui-panel" title="历史入职信息" style="width: 100%">
		<table id="datagridJob">
		</table>
	</div>

	<script type="text/javascript">
		$(function() {
			try {
				var hr_id = '${HR_ID}';
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
						title : '入职时间',
						width : '10%',
						align : 'center'
					} ] ],
					columns : [ [ {
						"title" : "入职信息",
						"colspan" : 5
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
					} ] ],
				});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>