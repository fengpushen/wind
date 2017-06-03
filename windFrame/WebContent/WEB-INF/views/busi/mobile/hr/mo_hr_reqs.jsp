<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>个人申请职位</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel">
		<header>
			<div class="m-toolbar">
				<div class="m-left">
					<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true" onclick="showMobileHrCenterUI()">个人控制台</a> <a
						href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true" onclick="showMobileHrInfoUI()">个人信息</a> <a
						href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true">职位搜索</a>
				</div>

			</div>
		</header>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center; background-color: #EEEEEE"
			data-options="noheader:false, closed:true, border:false"></div>
		<div class="easyui-panel" title="已申请岗位" style="width: 100%">
			<table id="datagrid">
			</table>
		</div>
	</div>
	<script type="text/javascript">
		function showMobileHrInfoUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrInfoUI.do";
		}
		function showMobileHrCenterUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrCenterUI.do";
		}
		$(function() {
			var hr_id = localStorage.getItem("hr_id");
			$('#datagrid')
					.datagrid(
							{
								method : 'POST',
								url : 'busi/position/loadHrReqList.do',
								queryParams : {
									"hr_id" : hr_id
								},
								columns : [ [
										{
											field : 'P_ID',
											"title" : "申请信息",
											width : '80%',
											align : 'left',
											formatter : function(value, row,
													index) {
												var space = "|";
												var showValue = row.P_NAME
														+ space
														+ row.IN_TIME_STR;
												showValue += "<br />";
												showValue += row.C_NAME;
												var room_no = row.ROOM_NO;
												if (room_no != null
														&& room_no != '') {
													var url = baseHref
															+ "busi/position/showMobilePositionInterview.do?room="
															+ room_no;
													showValue += "<br />";
													showValue += "<a href='"+url+"'>视频面试</a>";
												}
												return showValue;
											}
										}, {
											field : 'REQ_STATUS_NAME',
											"title" : "状态",
											width : '20%',
											align : 'left'
										} ] ],
							});
		});
	</script>
</body>
</html>
