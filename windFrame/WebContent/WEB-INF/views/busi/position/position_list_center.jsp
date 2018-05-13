<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>招聘岗位</title>
<tags:commonHead />
</head>
<body class="easyui-layout">

	<div class="easyui-panel" data-options="region:'north',noheader:true"
		style="overflow: hidden; background-color: #E0EEEE;">
		<form id="qryForm" method="post">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">单位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="c_name_like" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">岗位名称:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="P_NAME_LIKE" style="width: 100%" /></td>
					<td style="width: 10%; text-align: right">月工资大于:</td>
					<td style="width: 23%; text-align: left"><input
						class="easyui-textbox" name="pay_botton_bgn" style="width: 100%" /></td>
					<td style="width: 33%; text-align: center"><a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="loadDatagridData();" style="width: 80px">查询</a> <a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="exportExl();" style="width: 80px">导出</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div data-options="region:'center', border:false">
		<table id="datagrid">
		</table>
	</div>

	<div id="dd"></div>

	<script type="text/javascript">
		function loadDatagridData() {
			$('#datagrid').datagrid('load', $("#qryForm").serializeJson());
		}
		$(function() {
			var toolbar = [{
				text : '查看岗位详情',
				iconCls : 'icon-cut',
				handler : function() {
					try {
						var rows = $('#datagrid').datagrid('getSelections');
						if (rows == null || rows.length == 0) {
							$.messager.alert("", "请选中要操作的记录");
						} else if (rows.length > 1) {
							$.messager.alert("", "请选中单条记录进行操作");
						} else {
							var pid = rows[0].P_ID;
							$('#dd').dialog({
								title : '岗位详情',
								width : 1000,
								height : 450,
								closed : false,
								cache : false,
								href : 'busi/position/showPositionInfoUI.do',
								queryParams : {
									'pid' : pid
								},
								modal : false,
								onBeforeClose : function() {
									loadDatagridData();
								}
							});
						}
					} catch (e) {
						alert(e);
					}
				}
			}];
			try {
				$('#datagrid').datagrid({
					url : "busi/position/loadPositionValidList.do",
					method : 'POST',
					formId : 'qryForm',
					rownumbers : true,
					singleSelect : true,
					toolbar : toolbar,
					fit : true,
					pagination : true,
					striped : true,
					singleSelect : false,
					pageSize : 20,
					pageList : [20, 50, 100, 150, 200],
					columns : [[{
						field : 'ck',
						checkbox : true
					}, {
						field : 'C_NAME',
						title : '单位名称',
						width : '15%',
						align : 'center'
					}, {
						field : 'P_NAME',
						title : '岗位名称',
						width : '15%',
						align : 'center'
					}, {
						field : 'P_NUM',
						title : '招聘人数',
						width : '15%',
						align : 'center'
					}, {
						field : 'P_PAY_BOTTON',
						title : '月工资从',
						width : '8%',
						align : 'center'
					}, {
						field : 'P_PAY_TOP',
						title : '月工资到',
						width : '8%',
						align : 'center'
					}, {
						field : 'P_WORK_AREA_NAME',
						title : '工作地点',
						width : '8%',
						align : 'center'
					}, {
						field : 'END_TIME',
						title : '截止时间（包含）',
						width : '10%',
						align : 'center'
					}, {
						field : 'P_ZPR',
						title : '招聘负责人',
						width : '8%',
						align : 'center'
					}, {
						field : 'P_ZPDH',
						title : '招聘电话',
						width : '8%',
						align : 'center'
					}]],
				});
				loadDatagridData();
			} catch (e) {
				alert(e);
			}
		});

		function exportExl() {
			var opts = $('#datagrid').datagrid('options').columns;
			var head = JSON.stringify(opts);
			console.log(head);
			var formId = $('#datagrid').datagrid('options').formId;
			console.log(formId);
			/*	console.log(opts);
				for (var i = 0; i < opts[0].length; i++) {
					console.log(opts[0][i].title);
				}*/
			//JSONToExcelConvertor(JSON_DATA.data, "export", opts);
		}

		function JSONToExcelConvertor(JSONData, FileName, columns) {

			var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;

			var excel = '<table>';
			for (var i = 0; i < columns.length; i++) {
				//设置表头
				var row = "<tr>";
				for (var j = 0, l = columns[i].length; j < l; j++) {
					if (columns[i][j].title != null) {
						row += "<td style='font-weight:blod;'>" + columns[i][j].title + '</td>';
					}
				}
				excel += row + "</tr>";
			}

			//设置数据
			for (var i = 0; i < arrData.length; i++) {
				var row = "<tr>";

				for (var j = 0; j < arrData[i].length; j++) {
					var value = arrData[i][j].value === "." ? "" : arrData[i][j].value;
					row += '<td>' + value + '</td>';
				}
				excel += row + "</tr>";
			}

			excel += "</table>";
			console.log(excel);

			var excelFile = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
			excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8">';
			excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel';
			excelFile += '; charset=UTF-8">';
			excelFile += "<head>";
			excelFile += "<!--[if gte mso 9]>";
			excelFile += "<xml>";
			excelFile += "<x:ExcelWorkbook>";
			excelFile += "<x:ExcelWorksheets>";
			excelFile += "<x:ExcelWorksheet>";
			excelFile += "<x:Name>";
			excelFile += "sheet1";
			excelFile += "</x:Name>";
			excelFile += "<x:WorksheetOptions>";
			excelFile += "<x:DisplayGridlines/>";
			excelFile += "</x:WorksheetOptions>";
			excelFile += "</x:ExcelWorksheet>";
			excelFile += "</x:ExcelWorksheets>";
			excelFile += "</x:ExcelWorkbook>";
			excelFile += "</xml>";
			excelFile += "<![endif]-->";
			excelFile += "</head>";
			excelFile += "<body>";
			excelFile += excel;
			excelFile += "</body>";
			excelFile += "</html>";

			var uri = 'data:application/vnd.ms-excel;charset=utf-8,' + encodeURIComponent(excelFile);

			var link = document.createElement("a");
			link.href = uri;

			link.style = "visibility:hidden";
			link.download = FileName + ".xls";

			document.body.appendChild(link);
			link.click();
			document.body.removeChild(link);
		}
		var JSON_DATA = {
			"title" : [{
				"value" : "司机",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "日期",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "物流单数量",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "退货单数量",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "发货总件数",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "退货总件数",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "拒收总件数",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "取消发货总件数",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "物流费总金额    ",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "代收手续费总金额",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}, {
				"value" : "代收货款总金额",
				"type" : "ROW_HEADER_HEADER",
				"datatype" : "string"
			}],
			"data" : [

			]
		};
	</script>


</body>
</html>