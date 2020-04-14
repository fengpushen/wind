<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>人员信息更新情况统计</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="overflow: hidden; background-color: #E0EEEE;">

		<form id="qryForm" method="post">
			<input type="hidden" id="area_scope" name="area_scope"
				value="${area_scope}" />
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">地区范围:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="area_scope_name"
						value="${area_scope_name}" style="width: 100%"></td>
					<td style="width: 10%; text-align: right">地区层级:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="area_level" name="area_level"
						style="width: 100%" value="${area_level }"></td>
					<td style="width: 10%; text-align: right">地区性质:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="area_type" name="area_type"
						style="width: 100%" ></td>
				</tr>
			</table>
		</form>
		<div style="text-align: center; padding: 5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="loadDatagridData();" style="width: 80px">查询</a>
		</div>

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

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/hr/loadHrChangeTjList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			try {
				var area_scope = "${area_scope}";
				var areaTree = new AreaTree('dlgList', 'area_scope',
						'area_scope_name');
				$('#areaTreeList').tree({
					url : 'busi/common/loadTree.do',
					method : 'post',
					queryParams : {
						'treeName' : 'busi_com_area_tree_bj',
						'rootId' : area_scope
					},
					onClick : function(node) {
						areaTree.nodeClick(node);
					}
				});
				$('#area_scope_name').textbox({
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
				$("#area_level").combobox({
					method : 'post',
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto',
					required : true,
					editable : false,
					loader : function(param, success, error) {
						$.ajax({
							url : 'frame/loadCode.do?codeName=area_level',
							dataType : 'json',
							success : function(data) {
								success(data);
							},
							error : function() {
								error.apply(this, arguments);
							}
						});
					},
					loadFilter : function(data) {
						for ( var i in data) {
							if (data[i].id == "0" || data[i].id == "1") {
								data.splice(i, 1);
							}
						}
						return data;
					}
				});
				$("#area_type").combobox({
					method : 'post',
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto',
					required : false,
					editable : false,
					loader : function(param, success, error) {
						$.ajax({
							url : 'frame/loadCode.do?codeName=area_type',
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
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					pagination : true,
					striped : true,
					singleSelect : false,
					fit : true,
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					columns : [ [ {
						field : 'AREA_NAME',
						title : '地区',
						width : '8%',
						align : 'center'
					}, {
						field : 'ALL_COUNT',
						title : '人员总数',
						width : '6%',
						sortable : true,
						align : 'center'
					},{
						field : 'CHANGED',
						title : '变更人数',
						width : '6%',
						sortable : true,
						align : 'center'
					}, {
						field : 'JOB_CHANGE',
						title : '就业信息变更人数',
						width : '10%',
						sortable : true,
						align : 'center'
					}, {
						field : 'JYXX_CHANGE',
						title : '基本信息变更人数',
						width : '8%',
						sortable : true,
						align : 'center',
					},{
						field : 'GXL',
						title : '变更占比',
						width : '8%',
						sortable : true,
						align : 'center'
					} ]]
				});
				loadDatagridData();
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>