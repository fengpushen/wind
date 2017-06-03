<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>个人信息</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel" style="position: relative; padding: 20px">
		<p>您将要申请${comInfo.C_NAME }的${P_NAME }</p>
		<p>对方将会看到您的如下信息：</p>
		<table>
			<tr>
				<td style="width: 20%">姓名</td>
				<td style="width: 30%">${hrInfo.HR_NAME }</td>
				<td style="width: 20%">出生日期</td>
				<td style="width: 30%">${hrInfo.BIRTH }</td>
			</tr>
			<tr>
				<td style="width: 20%">性别</td>
				<td style="width: 30%">${hrInfo.SEX_NAME}</td>
				<td style="width: 20%">民族</td>
				<td style="width: 30%">${hrInfo.NATION_NAME}</td>
			</tr>
			<tr>
				<td style="width: 20%">户籍地</td>
				<td style="width: 30%">${hrInfo.HJ_AREA_NAME}</td>
				<td style="width: 20%">文化程度</td>
				<td style="width: 30%">${hrInfo.DEGREE_NAME}</td>
			</tr>
			<tr>
				<td style="width: 20%">毕业学校</td>
				<td style="width: 30%">${hrInfo.SCHOOL}</td>
				<td style="width: 20%">毕业年度</td>
				<td style="width: 30%">${hrInfo.GRA_YEAR}</td>
			</tr>
			<tr>
				<td style="width: 20%">专业</td>
				<td style="width: 30%">${hrInfo.PRO}</td>
				<td style="width: 20%">电话</td>
				<td style="width: 30%">${hrInfo.PHONE}</td>
			</tr>
			<tr>
				<td style="width: 20%">QQ</td>
				<td style="width: 30%">${hrInfo.QQ}</td>
				<td style="width: 20%">电子邮箱</td>
				<td style="width: 30%">${hrInfo.EMAIL}</td>
			</tr>
			<tr>
				<td style="width: 20%">地址</td>
				<td style="width: 80%" colspan="3">${hrInfo.ADDRESS }</td>
			</tr>
			<tr>
				<td style="width: 20%">技能特长</td>
				<td style="width: 80%" colspan="3">${hrInfo.JNTC }</td>
			</tr>
		</table>
		<p>个人介绍</p>
		<p>${hrInfo.INTRO}</p>
		<div style="text-align: center; margin-top: 30px">
			<a href="javascript:void(0)" onclick="sendPositionReq()"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 14px">确认申请</span></a>
			<a href="javascript:void(0)" onclick="showMobileHrInfoUI()"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 14px">修改信息</span></a>
			<a href="javascript:void(0)" onclick="closeJobnojobOpr()"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 14px">取消</span></a>
		</div>

	</div>




	<script type="text/javascript">
		function sendPositionReq() {
			var hr_id = '${hrInfo.HR_ID}';
			var pid = '${P_ID}';
			$.ajax({
				type : "post",
				url : "busi/position/sendPositionReq.do",
				dataType : "json",
				data : {
					'hr_id' : hr_id,
					'pid' : pid
				},
				success : function(rst) {
					if (rst.isSucc) {
						$.messager.alert("", "操作成功");
					} else {
						var msg = '操作失败';
						if (rst.info.INFO_KEY_DEFAULT != null) {
							msg = msg + ',' + rst.info.INFO_KEY_DEFAULT;
						}
						$.messager.alert("", msg);
					}
					closeJobnojobOpr();
				}
			});
		}
		function showMobileHrInfoUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrInfoUI.do";
		}
		$(function() {
		});
	</script>
</body>
</html>
