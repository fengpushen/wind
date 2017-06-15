<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>职位信息</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel">
		<header>
			<div class="m-toolbar">
				<div class="m-left">
					<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
						outline="true" onclick="history.back()">返回</a>
				</div>

			</div>
		</header>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center; background-color: #EEEEEE"
			data-options="noheader:false, closed:true, border:false"></div>
		<div class="easyui-tabs"
			data-options="fit:true,border:false,tabWidth:80,tabHeight:35">
			<div title="职位信息" style="padding: 10px">
				<p style="font-size: 14px">${P_NAME }</p>
				<p>更新于：${IN_TIME_STR }</p>
				<hr
					style="FILTER: alpha(opacity = 100, finishopacity = 0, style = 3)"
					width="95%" color='#987cb9' size='3' />
				<p>
					薪资：${P_PAY_BOTTON }
					<c:if test="${P_PAY_TOP != null }">~${P_PAY_TOP}</c:if>
				</p>

				<table>
					<tr>
						<td style="width: 20%">公司</td>
						<td style="width: 80%" colspan="3">${comInfo.C_NAME }</td>
					</tr>
					<tr>
						<td style="width: 20%">工作地</td>
						<td style="width: 30%">${P_WORK_AREA_NAME}</td>
						<td style="width: 20%">招聘人数</td>
						<td style="width: 30%">${P_NUM}</td>
					</tr>
					<tr>
						<td style="width: 20%">截止时间</td>
						<td style="width: 30%">${END_TIME}</td>
						<td style="width: 20%">学历要求</td>
						<td style="width: 30%">${P_DEGREE_NAME}</td>
					</tr>
					<tr>
						<td style="width: 20%">年龄从</td>
						<td style="width: 30%">${P_AGE_BOTTON}</td>
						<td style="width: 20%">到</td>
						<td style="width: 30%">${P_AGE_TOP}</td>
					</tr>
				</table>
				<hr
					style="FILTER: alpha(opacity = 100, finishopacity = 0, style = 3)"
					width="95%" color='#987cb9' size='3' />
				<p>
					其他说明：<br /> ${P_DETAIL}
				</p>
			</div>
			<div title="企业信息" style="padding: 10px">
				<p style="font-size: 14px">${comInfo.C_NAME }</p>
				<table>
					<tr>
						<td style="width: 20%">地址</td>
						<td style="width: 80%" colspan="3">${comInfo.C_ADDRESS }</td>
					</tr>
					<tr>
						<td style="width: 20%">成立时间</td>
						<td style="width: 30%">${comInfo.C_CREATETIME}</td>
						<td style="width: 20%">员工规模</td>
						<td style="width: 30%">${comInfo.C_DIMENSIONS}</td>
					</tr>
				</table>
				<hr
					style="FILTER: alpha(opacity = 100, finishopacity = 0, style = 3)"
					width="95%" color='#987cb9' size='3' />
				<p>企业介绍</p>
				<p>${comInfo.C_INTRO}</p>
			</div>
			<c:if test="${ comWtInfo != null}">
				<div title="中介信息" style="padding: 10px">
					<p style="font-size: 14px">${comWtInfo.C_NAME }</p>
					<table>
						<tr>
							<td style="width: 20%">地址</td>
							<td style="width: 80%" colspan="3">${comWtInfo.C_ADDRESS }</td>
						</tr>
						<tr>
							<td style="width: 20%">成立时间</td>
							<td style="width: 30%">${comWtInfo.C_CREATETIME}</td>
							<td style="width: 20%">员工规模</td>
							<td style="width: 30%">${comWtInfo.C_DIMENSIONS}</td>
						</tr>
					</table>
					<hr
						style="FILTER: alpha(opacity = 100, finishopacity = 0, style = 3)"
						width="95%" color='#987cb9' size='3' />
					<p>企业介绍</p>
					<p>${comWtInfo.C_INTRO}</p>
				</div>
			</c:if>
		</div>
		<div id="jobnojobOpr"></div>
		<footer style="padding: 2px 3px">
			<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
				outline="true" onclick="showMobileHrResumeUI()" style="width: 70px">申请职位</a>
		</footer>
	</div>
	<script type="text/javascript">
		function closeJobnojobOpr() {
			$('#jobnojobOpr').dialog('close');
		}
		function showMobileHrResumeUI() {
			var hr_id = localStorage.getItem("hr_id");
			var pid = '${P_ID}';
			$('#jobnojobOpr').dialog({
				title : '信息确认',
				width : '100%',
				height : '80%',
				closed : false,
				cache : false,
				href : 'busi/position/showMobilePositionConfirmUI.do',
				queryParams : {
					'pid' : pid,
					'hr_id' : hr_id
				},
				modal : true
			});
		}
		$(function() {

		});
	</script>
</body>
</html>
