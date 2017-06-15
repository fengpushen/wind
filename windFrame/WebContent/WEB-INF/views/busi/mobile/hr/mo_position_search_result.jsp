<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>职位搜索结果</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel">
		<header>
			<div class="m-toolbar">
				<div class="m-left">
					<a href="javascript:void(0)" class="easyui-linkbutton m-back"
						onclick="showMobilePositionSearch()" plain="true" outline="true">返回</a>
				</div>
				<div class="m-title">职位搜索结果</div>
			</div>
		</header>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center; background-color: #EEEEEE"
			data-options="noheader:false, closed:true, border:false"></div>

		<table id="datagrid">
		</table>
	</div>
	<script type="text/javascript">
		function showMobilePositionSearch() {
			window.location.href = baseHref
					+ "busi/position/showMobilePositionSearch.do";
		}
		$(function() {
			$('#datagrid')
					.datagrid(
							{
								showHeader : false,
								singleSelect : false,
								onClickRow : function(rowIndex, rowData) {
									window.location.href = baseHref
											+ "busi/position/showMobilePositionInfo.do?pid="
											+ rowData.P_ID;
								},
								striped : true,
								url : 'busi/position/searchMobilePositionValidList.do',
								queryParams : {
									'p_names' : '${p_names}',
									'P_WORK_AREA' : '${P_WORK_AREA}',
									'pay_botton_bgn' : '${pay_botton_bgn}'
								},
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
