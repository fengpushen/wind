<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>劳动力资源信息查询</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="background-color: #E0EEEE; overflow: hidden;">

		<form id="qryForm" method="post">
			<input type="hidden" id="HJ_AREA_LIST" name="HJ_AREA" />
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">身份证:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="IDCARD_LIKE" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">姓名:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="HR_NAME_LIKE" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">户籍地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="HJ_AREA_NAME_LIST"
						value="${accountInfo.staffInfo.AREA_NAME}" style="width: 100%"></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">是否为劳动力:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="LD_TYPE" id="LD_TYPE_LIST"
						style="width: 100%" value="${LD_TYPE }" /></td>
					<td style="width: 10%; text-align: right">是否就业:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="IS_JOB" id="IS_JOB_LIST"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">是否有就业意愿:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="IS_WANT_JOB" id="IS_WANT_JOB_LIST"
						style="width: 100%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">民族:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="nation" id="nation"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">政治面貌:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="political_status"
						id="political_status" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">婚姻状况:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="marry_status" id="marry_status"
						style="width: 100%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">学历:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="degree" id="degree"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">是否贫困户:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="is_poor" id="is_poor"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">职业资格证书:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="job_lv" id="job_lv"
						style="width: 100%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">人员身份:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="hr_kind" id="hr_kind"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">困难群体:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="hard_type" id="hard_type"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">产业意向:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="want_industry" id="want_industry"
						style="width: 100%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">区域意向:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="want_work_area_kind"
						id="want_work_area_kind" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">就业形式意向:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="want_job_type" id="want_job_type"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">培训需求意向:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="want_train_type"
						id="want_train_type" style="width: 100%" /></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right">技能特长:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="JNTC" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">户口性质:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="hj_type" id="hj_type"
						style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">性别:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-combobox" name="sex" id="sex"
						style="width: 100%" /></td>
				</tr>

				<tr>
					<td colspan="6" style="text-align: center;"><a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="loadDatagridData('datagrid');" style="width: 80px">查询</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div data-options="region:'center', border:false">
		<table id="datagrid">
		</table>
	</div>

	<div id="dd"></div>

	<div id="dlgList" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true">
		<ul id="areaTreeList" class="easyui-tree">
		</ul>
	</div>

	<div id="dlgLdType" class="easyui-dialog" title="选择"
		style="width: 250px; height: 80px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#ldtypeButtons'">
		<ul id="ldtypeTree" class="easyui-tree">
		</ul>
		<div id="#ldtypeButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanLdTypeBtn" style="width: 80px">清除</a>
		</div>
	</div>

	<script type="text/javascript">

		$(function() {
			try {
				var toolbar = [
						{
							text : '新增',
							iconCls : 'icon-add',
							handler : function() {
								$('#dd').dialog({
									title : '新增人员',
									width : 1100,
									height : 500,
									closed : false,
									cache : false,
									href : 'busi/hr/showHrInfo.do',
									modal : false,
									onBeforeClose : function() {
										loadDatagridData('datagrid');
									},
									onLoad : function() {
										hrInfoPageLoaded();
									}
								});
							}
						},
						{
							text : '删除',
							iconCls : 'icon-remove',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else {
									var ids = [];
									var names = [];
									for (var i = 0; i < rows.length; i++) {
										ids.push(rows[i].HR_ID);
										names.push(rows[i].HR_NAME);
									}
									$.messager
											.confirm(
													'Confirm',
													'你确定要删除' + names.join(',')
															+ "这"
															+ names.length
															+ "人的记录吗？",
													function(r) {
														if (r) {
															$
																	.ajax({
																		type : "post",
																		url : "busi/hr/delHrInfo.do",
																		dataType : "json",
																		data : {
																			'ids' : ids
																		},
																		success : function(
																				rst) {
																			if (rst.isSucc) {
																				$.messager
																						.alert(
																								"",
																								"操作成功");
																			} else {
																				var msg = '操作失败';
																				if (rst.info.INFO_KEY_DEFAULT != null) {
																					msg = msg
																							+ ','
																							+ rst.info.INFO_KEY_DEFAULT;
																				}
																				$.messager
																						.alert(
																								"",
																								msg);
																			}
																			loadDatagridData('datagrid');
																		}
																	});
														}
													});
								}
							}
						},
						{
							text : '修改',
							iconCls : 'icon-cut',
							handler : function() {
								var rows = $('#datagrid').datagrid(
										'getSelections');
								if (rows == null || rows.length == 0) {
									$.messager.alert("", "请选中要操作的记录");
								} else if (rows.length > 1) {
									$.messager.alert("", "请选中单条记录进行操作");
								} else {
									var hr_id = rows[0].HR_ID;
									$('#dd').dialog({
										title : '修改',
										width : 1100,
										height : 500,
										closed : false,
										cache : false,
										href : 'busi/hr/showHrInfoMdy.do',
										queryParams : {
											'hr_id' : hr_id
										},
										modal : false,
										onBeforeClose : function() {
											loadDatagridData('datagrid');
										}
									});
								}
							}
						} ];
				$("#IS_JOB_LIST").combobox({
					method : 'post',
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto',
					editable : false,
					loader : function(param, success, error) {
						$.ajax({
							url : 'frame/loadCode.do?codeName=boolean',
							dataType : 'json',
							success : function(data) {
								data.unshift({
									"text" : "请选择",
									"id" : ""
								});
								success(data);
							},
							error : function() {
								error.apply(this, arguments);
							}
						});
					}
				});
				$("#IS_WANT_JOB_LIST").combobox({
					method : 'post',
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto',
					editable : false,
					loader : function(param, success, error) {
						$.ajax({
							url : 'frame/loadCode.do?codeName=boolean',
							dataType : 'json',
							success : function(data) {
								data.unshift({
									"text" : "请选择",
									"id" : ""
								});
								success(data);
							},
							error : function() {
								error.apply(this, arguments);
							}
						});
					}
				});
				$("#LD_TYPE_LIST").combobox({
					method : 'post',
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto',
					editable : false,
					loader : function(param, success, error) {
						$.ajax({
							url : 'frame/loadCode.do?codeName=boolean',
							dataType : 'json',
							success : function(data) {
								data.unshift({
									"text" : "请选择",
									"id" : ""
								});
								success(data);
							},
							error : function() {
								error.apply(this, arguments);
							}
						});
					}
				});
				comboboxDefaultInit('nation', 'nation', false, 200, false, true);
				comboboxDefaultInit('political_status', 'political_status',
						false, 'auto', false, true);
				comboboxDefaultInit('marry_status', 'marry_status', false,
						'auto', false, true);
				comboboxDefaultInit('degree', 'degree', false, 'auto', false,
						true);
				comboboxDefaultInit('is_poor', 'boolean', false, 'auto', false,
						true);
				comboboxDefaultInit('job_lv', 'job_lv', false, 'auto', false,
						true);
				comboboxDefaultInit('hr_kind', 'hr_kind', false, 'auto', false,
						true);
				comboboxDefaultInit('hj_type', 'hj_type', false, 'auto', false,
						true);
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
				comboboxDefaultInit('want_train_type', 'train_type', false,
						'auto', false, true);
				comboboxDefaultInit('sex', 'sex', false, 'auto', false,
						true);

				var areaTree = new AreaTree('dlgList', 'HJ_AREA_LIST',
						'HJ_AREA_NAME_LIST');
				var accountArea = '${accountInfo.staffInfo.AREA_CODE}';
				$('#areaTreeList').tree({
					url : 'busi/common/loadTree.do',
					method : 'post',
					queryParams : {
						'treeName' : 'busi_com_area_tree_bj',
						'rootId' : accountArea
					},
					onClick : function(node) {
						areaTree.nodeClick(node);
					}
				});
				$('#HJ_AREA_NAME_LIST').textbox({
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
				$('#datagrid').datagrid({
					url : 'busi/hr/loadHrList.do',
					method : 'POST',
					formId : 'qryForm',
					rownumbers : true,
					pagination : true,
					striped : true,
					singleSelect : false,
					fit : true,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'HR_NAME',
						title : '姓名',
						width : '6%',
						align : 'center'
					}, {
						field : 'IDCARD_COVERD',
						title : '身份证',
						width : '14%',
						align : 'center'
					}, {
						field : 'AGE',
						title : '年龄',
						width : '4%',
						align : 'center'
					}, {
						field : 'SEX_NAME',
						title : '性别',
						width : '4%',
						align : 'center'
					}, {
						field : 'HJ_AREA_NAME',
						title : '户籍地',
						width : '8%',
						align : 'center'
					}, {
						field : 'NATION_NAME',
						title : '民族',
						width : '6%',
						align : 'center'
					}, {
						field : 'LD_TYPE_NAME',
						title : '是否劳动力',
						width : '6%',
						align : 'center'
					}, {
						field : 'IS_JOB_NAME',
						title : '是否就业',
						width : '5%',
						align : 'center'
					}, {
						field : 'IS_WANT_JOB_NAME',
						title : '有就业意愿',
						width : '6%',
						align : 'center'
					}, {
						field : 'DEGREE_NAME',
						title : '文化程度',
						width : '8%',
						align : 'center'
					}, {
						field : 'JNTC',
						title : '技能特长',
						width : '15%',
						align : 'center'
					} ] ]
				});
				addExportExlBtn('datagrid');
				loadDatagridData('datagrid');
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>