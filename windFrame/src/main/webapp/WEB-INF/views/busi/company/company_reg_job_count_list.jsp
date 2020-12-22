<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>企业录用人员统计</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="noheader:true">
		<div class="easyui-panel" data-options="noheader:true"
			style="background-color: #E0EEEE;" id="divForm">
			<form id="qryForm" method="post">
				<input type="hidden" id="HJ_AREA_LIST" name="HJ_AREA" />
				<table style="width: 100%">
					<tr>
						<td style="width: 10%; text-align: right">登记开始时间:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-datebox" name="in_time_bgn" style="width: 100%"
							data-options="sharedCalendar:'#cc'" /></td>
						<td style="width: 10%; text-align: right">登记结束时间:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-datebox" name="in_time_end" style="width: 100%"
							data-options="sharedCalendar:'#cc'" /></td>
						<td style="width: 10%; text-align: right">户籍地:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-textbox" id="HJ_AREA_NAME_LIST"
							value="${accountInfo.staffInfo.AREA_NAME}" style="width: 100%"></td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px 0">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="loadDatagridData();" style="width: 80px">查询</a>
			</div>
		</div>

		<table id="datagrid">
		</table>

	</div>

	<div id="dd"></div>

	<div id="cc" class="easyui-calendar" data-options="closed:true"></div>

	<div id="dlgList" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true">
		<ul id="areaTreeList" class="easyui-tree">
		</ul>
	</div>

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/company/loadComRegJobCountList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			try {
				var areaTree = new AreaTree('dlgList', 'HJ_AREA_LIST',
						'HJ_AREA_NAME_LIST');
				$('#areaTreeList').tree({
					url : 'busi/common/loadTree.do',
					method : 'post',
					queryParams : {
						'treeName' : 'busi_com_area_tree_bj',
						'rootId' : '430900000000'
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
					editable : false
				});
				var divForm = document.getElementById('divForm');
				var tableHeight = document.body.clientHeight
						- divForm.offsetHeight - 70;
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					pagination : true,
					striped : true,
					singleSelect : false,
					fit : true,
					style : {
						'height' : tableHeight + 'px'
					},
					pageSize : 20,
					pageList : [ 20, 50, 100, 150, 200 ],
					columns : [ [ {
						field : 'COUNTRY_NAME',
						title : '区县',
						width : '10%',
						align : 'center'
					}, {
						field : 'STREET_NAME',
						title : '街道/乡镇',
						width : '10%',
						align : 'center'
					}, {
						field : 'VILLAGE_NAME',
						title : '社区/村',
						width : '10%',
						align : 'center'
					}, {
						field : 'JOB_COUNT',
						title : '录用人次',
						width : '10%',
						align : 'center'
					} ] ]
				});
				loadDatagridData();
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>