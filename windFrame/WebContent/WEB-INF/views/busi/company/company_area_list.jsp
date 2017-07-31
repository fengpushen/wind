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
	<div class="easyui-panel" data-options="noheader:true">
		<div class="easyui-panel" data-options="noheader:true"
			style="background-color: #E0EEEE;" id="divForm">
			<form id="qryForm" method="post">
				<input type="hidden" id="HJ_AREA_LIST" name="area_code_high" value="${area_code_high }"/>
				<table style="width: 100%">
					<tr>
						<td style="width: 10%; text-align: right">地区名称:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-textbox" name="C_NAME_LIKE" style="width: 100%" /></td>
						<td style="width: 10%; text-align: right">所在地:</td>
						<td style="width: 23%; text-align: left"><input
							class="easyui-textbox" id="HJ_AREA_NAME_LIST" name="area_name_high" 
							style="width: 100%" value="${area_name_high }"></td>
						<td style="width: 33%; text-align: center"><a
							href="javascript:void(0)" class="easyui-linkbutton"
							onclick="loadDatagridData();" style="width: 80px">查询</a></td>
					</tr>
				</table>
			</form>
		</div>
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
				text : '发起视频',
				iconCls : 'icon-add',
				handler : function() {
					$('#dd').dialog({
						title : '新增单位',
						width : 1000,
						height : 350,
						closed : false,
						cache : false,
						href : 'busi/company/showComInfoSaveUI.do',
						queryParams : {
							'c_type' : ctype,
							'c_id_wt' : c_id_wt
						},
						modal : false,
						onBeforeClose : function() {
							loadDatagridData();
						}
					});
				}
			} ];
			var areaTree = new AreaTree('dlgList', 'HJ_AREA_LIST',
					'HJ_AREA_NAME_LIST');
			var accountArea = '${accountInfo.staffInfo.AREA_CODE}';
			$('#areaTreeList').tree({
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
			var divForm = document.getElementById('divForm');
			var tableHeight = document.body.clientHeight - divForm.offsetHeight
					- 70;
			try {
				$('#datagrid').datagrid({
					method : 'POST',
					rownumbers : true,
					singleSelect : true,
					toolbar : toolbar,
					pagination : true,
					striped : true,
					fit : true,
					style : {
						'height' : tableHeight + 'px'
					},
					singleSelect : false,
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
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>