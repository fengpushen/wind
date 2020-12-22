<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>视频连线</title>
<tags:commonHead />
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="overflow: hidden; background-color: #E0EEEE;">

		<form id="qryForm" method="post">
			<input type="hidden" id="HJ_AREA_LIST" name="area_code_high"
				value="${area_code_high }" />
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">地区名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="C_NAME_LIKE" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">所在地:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" id="HJ_AREA_NAME_LIST"
						name="area_name_high" style="width: 100%"
						value="${area_name_high }"></td>
					<td style="width: 33%; text-align: center"><a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="loadDatagridData();" style="width: 80px">查询</a></td>
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

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('options').url = "busi/company/loadAreaList.do";
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			var toolbar = [ {
				text : '视频连线',
				iconCls : 'icon-save',
				handler : function() {
					var rows = $('#datagrid').datagrid('getSelections');
					if (rows == null || rows.length == 0) {
						$.messager.alert("", "请选中要操作的记录");
					} else if (rows.length > 1) {
						$.messager.alert("", "请选中单条记录进行操作");
					} else {
						var req_id = rows[0].AREA_CODE;
						window
								.open(
										baseHref
												+ "busi/company/showComAreaVideoUI.do?area_code="
												+ req_id, "_blank");
					}
				}
			} ];
			var areaTree = new AreaTree('dlgList', 'HJ_AREA_LIST',
					'HJ_AREA_NAME_LIST');
			$('#areaTreeList').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree',
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
				editable : false,
				required : true
			});

			$('#datagrid').datagrid({
				method : 'POST',
				rownumbers : true,
				singleSelect : true,
				toolbar : toolbar,
				pagination : true,
				striped : true,
				fit : true,
				pageSize : 20,
				pageList : [ 20, 50, 100, 150, 200 ],
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'AREA_NAME',
					title : '地区名称',
					width : '15%',
					align : 'center'
				} ] ],
			});
			loadDatagridData();
		});
	</script>


</body>
</html>