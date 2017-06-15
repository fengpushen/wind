<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>个人用户控制台</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel">
		<header>
			<div class="m-toolbar">
				<div class="m-left">
					<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true" onclick="showMobileHrInfoUI()">个人信息</a> <a
						href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true" onclick="showMobilePositionSearchUI()">职位搜索</a> <a
						href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true" onclick="showMobilePositionHrReqsUI()">我申请的职位</a>
				</div>

			</div>
		</header>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center; background-color: #EEEEEE"
			data-options="noheader:false, closed:true, border:false"></div>
		<div class="easyui-panel" title="岗位推荐" style="width: 100%">
			<table id="datagrid">
			</table>
		</div>
	</div>
	<script type="text/javascript">
		function showMobilePositionHrReqsUI() {
			window.location.href = baseHref
					+ "busi/position/showMobilePositionHrReqsUI.do";
		}
		function showMobileHrInfoUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrInfoUI.do";
		}
		function showMobilePositionSearchUI() {
			window.location.href = baseHref
					+ "busi/position/showMobilePositionSearch.do";
		}
		$(function() {
			var hr_id = localStorage.getItem("hr_id");
			$('#datagrid')
					.datagrid(
							{
								method : 'POST',
								showHeader : false,
								url : 'busi/position/loadPositionValidList.do',
								queryParams : {
									"hr_id" : hr_id
								},
								singleSelect : false,
								onClickRow : function(rowIndex, rowData) {
									window.location.href = baseHref
											+ "busi/position/showMobilePositionInfo.do?pid="
											+ rowData.P_ID;
								},
								striped : true,
								columns : [ [ {
									field : 'P_ID',
									"title" : "岗位信息",
									width : '99%',
									align : 'left',
									formatter : function(value, row, index) {
										var space = "|";
										var showValue = row.P_NAME;
										showValue += "&nbsp;&nbsp;&nbsp;&nbsp;"
										showValue += row.P_WORK_AREA_NAME;
										showValue += "<br />";
										showValue += row.C_NAME;
										if (row.P_PAY_BOTTON != null
												&& row.P_PAY_BOTTON != '') {
											showValue += space + '￥'
													+ row.P_PAY_BOTTON;
										}
										if (row.P_PAY_TOP != null
												&& row.P_PAY_TOP != '') {
											showValue += '-' + row.P_PAY_TOP;
										}
										return showValue;
									}
								} ] ],
							});
		});
	</script>
</body>
</html>
