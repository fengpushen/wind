<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>入离职信息管理</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel" style="position: relative;">
		<header>
			<div class="m-toolbar">
				<div class="m-left">
					<a href="javascript:void(0)" class="easyui-linkbutton m-back"
						onclick="showMobileHrCenterUI()" plain="true" outline="true">返回</a>
				</div>
				<div class="m-title">入离职信息管理</div>
			</div>
		</header>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center;"
			data-options="noheader:false, closed:true, border:false"></div>

		<table id="datagridJob">
		</table>
		<footer style="padding: 2px 3px">
			<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
				outline="true" onclick="showJobRegUI()" style="width: 70px">入职登记</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
				outline="true" onclick="showNojobRegUI()" style="width: 60px">离职登记</a>
		</footer>

	</div>


	<div id="jobnojobOpr"></div>

	<script type="text/javascript">
		function closeJobnojobOpr() {
			$('#jobnojobOpr').dialog('close');
		}
		function showJobRegUI() {
			$('#jobnojobOpr').dialog({
				title : '入职登记',
				width : '100%',
				height : '80%',
				closed : false,
				cache : false,
				href : 'busi/hr/showMobileHrJobRegUI.do',
				modal : true,
				onBeforeClose : function() {
					$('#datagridJob').datagrid('reload');
				}
			});
		}
		function showNojobRegUI() {
			$('#jobnojobOpr').dialog({
				title : '离职登记',
				width : '100%',
				height : '80%',
				closed : false,
				cache : false,
				href : 'busi/hr/showMobileHrNojobRegUI.do',
				modal : true,
				onBeforeClose : function() {
					$('#datagridJob').datagrid('reload');
				}
			});
		}
		function showMobileHrCenterUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrCenterUI.do";
		}
		function unbind() {
			window.location.href = baseHref + "busi/hr/unbindHr.do";
		}
		$(function() {
			var hr_id = localStorage.getItem("hr_id");
			$('#datagridJob')
					.datagrid(
							{
								method : 'POST',
								rownumbers : true,
								striped : true,
								url : 'busi/hr/loadHrJobList.do',
								queryParams : {
									"hr_id" : hr_id
								},
								singleSelect : false,
								columns : [ [ {
									field : 'JOB_AREA',
									"title" : "入离职信息",
									width : '90%',
									align : 'left',
									formatter : function(value, row, index) {
										var space = "&nbsp;&nbsp;&nbsp;&nbsp;";
										var showValue = row.IS_JOB_NAME;
										if (row.JOB_DW != null
												&& row.JOB_DW != '') {
											showValue += space + row.JOB_DW;
										}
										if (row.NOJOB_DW != null
												&& row.NOJOB_DW != '') {
											showValue += space + row.NOJOB_DW;
										}
										showValue += "<br />";
										if (row.JOB_TIME != null
												&& row.JOB_TIME != '') {
											showValue += row.JOB_TIME + space;
										}
										if (row.JOB_AREA_NAME != null
												&& row.JOB_AREA_NAME != '') {
											showValue += row.JOB_AREA_NAME
													+ space;
										}
										if (row.JOB_TYPE_NAME != null
												&& row.JOB_TYPE_NAME != '') {
											showValue += row.JOB_TYPE_NAME
													+ space;
										}
										if (row.INCOME != null
												&& row.INCOME != '') {
											showValue += row.INCOME + space;
										}
										if (row.NOWJOB_REASON != null
												&& row.NOWJOB_REASON != '') {
											showValue += row.NOWJOB_REASON
													+ space;
										}
										return showValue;
									}
								} ] ],
							});
		});
	</script>
</body>
</html>
