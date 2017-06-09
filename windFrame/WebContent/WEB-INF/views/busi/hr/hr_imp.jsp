<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!DOCTYPE html>
<html>
<head>
<title>人力资源信息导入</title>
<tags:commonHead />
</head>
<body>
	<div id="oprTip" class="easyui-panel"
		style="width: 100%; text-align: center; font-size: 20px; background-color: #EEEEEE"
		data-options="closed:true, border:false"></div>
	<form id="theForm" method="post" enctype="multipart/form-data"
		style="width: 100%">
		<div class="easyui-panel" style="background-color: #E0EEEE;"
			data-options="noheader:true" id="infoDiv">
			<table style="width: 100%">
				<tr>
					<td style="width: 10%; text-align: right">文件:</td>
					<td style="width: 90%; text-align: left"><input
						class="easyui-filebox" name="hrImpFile"
						data-options="prompt:'选择文件...',required:true,buttonText:'&nbsp;选&nbsp;择&nbsp;'"
						style="width: 90%"></td>
				</tr>
			</table>

		</div>


		<div style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#theForm').form('submit');" style="width: 80px">保存</a>
		</div>
	</form>

	<script type="text/javascript">
		$(function() {
			try {
				$("#infoDiv").click(function() {
					$('#oprTip').panel('close');
				});
				$('#theForm')
						.form(
								{
									url : 'busi/hr/batchImpHrInfo.do',
									success : function(data) {
										var rst = eval('(' + data + ')');
										if (rst.isSucc) {
											loadDatagridData();
											var msg = "操作成功";
											if (rst.info.allNum != null) {
												msg = msg + ',共导入'
														+ rst.info.allNum
														+ '条记录';
											}
											showOprTip("oprTip", msg, 'green');
											$('#theForm').form("clear");
										} else {
											var msg = '操作失败';
											if (rst.info.INFO_KEY_DEFAULT != null) {
												msg = msg
														+ ','
														+ rst.info.INFO_KEY_DEFAULT;
											}
											if (rst.info.batchId != null) {
												msg = msg
														+ ',点<a href="'
														+ baseHref
														+ 'busi/hr/dwnBatchImpError.do?batch_id='
														+ rst.info.batchId
														+ '">这里</a>可以下载详情';
											}
											showOprTip("oprTip", msg, 'red');
										}
										$('#oprTip').panel('open');
									}
								});
			} catch (e) {
				alert(e);
			}
		});
	</script>


</body>
</html>