<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>离职登记</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel" style="position: relative; padding: 20px">
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center;"
			data-options="noheader:false, closed:true, border:false"></div>
		<form id="theNoJobForm" method="post" style="width: 100%">
			<input type="hidden" id="HR_ID" name="nojob.HR_ID"/>
			<div style="margin-bottom: 10px">
				<input
						class="easyui-datebox" name="nojob.NOJOB_TIME"
						data-options="label:'离职时间', required:true" style="width: 100%" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="nojob.NOJOB_DW"
					data-options="label:'离职单位'" style="width: 100%" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="nojob.NOJOB_REASON"
					data-options="label:'离职原因'" style="width: 100%" />
			</div>
			
		</form>
		<div style="text-align: center; margin-top: 30px">
			<a href="javascript:void(0)" onclick="$('#theNoJobForm').form('submit');"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 16px">保存</span></a>
			<a href="javascript:void(0)" onclick="closeJobnojobOpr()"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 16px">取消</span></a>
		</div>

	</div>

	<script type="text/javascript">
		
		$(function() {
			var hr_id = localStorage.getItem("hr_id");
			$("#HR_ID").val(hr_id);
			$('#theNoJobForm')
					.form(
							{
								url : 'busi/hr/saveNOjobInfo.do',
								success : function(data) {
									var rst = eval('(' + data + ')');
									if (rst.isSucc) {
										showOprTip("oprTip", "操作成功", "green");
										closeJobnojobOpr();
									} else {
										var msg = '操作失败';
										if (rst.info.INFO_KEY_DEFAULT != null) {
											msg = msg + ','
													+ rst.info.INFO_KEY_DEFAULT;
										}
										showOprTip("oprTip", msg, "red");
									}
									$('#oprTip').panel('open');
								}
							});
		});
	</script>
</body>
</html>
