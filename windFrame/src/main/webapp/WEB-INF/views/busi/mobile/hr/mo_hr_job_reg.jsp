<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>入职登记</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel" style="position: relative; padding: 20px">
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center;"
			data-options="noheader:false, closed:true, border:false"></div>
		<form id="theForm" method="post" style="width: 100%">
			<input type="hidden" id="HJ_AREA" name="job.JOB_AREA" /> 
			<input type="hidden" id="HR_ID" name="job.HR_ID"/>
			<div style="margin-bottom: 10px">
				<input
						class="easyui-datebox" name="job.JOB_TIME"
						data-options="label:'入职时间', required:true" style="width: 100%">
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" id="HJ_AREA_NAME"
					data-options="label:'工作地', buttonText:'选择', editable:false, onClickButton:showAreaTree, onClick:showAreaTree, required:true"
					style="width: 100%" >
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-combobox" name="job.JOB_TYPE" style="width: 100%"
					data-options="
					url:'frame/loadCode.do?codeName=job_type',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false, label:'工作类型', required:true" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="job.JOB_DW"
					data-options="label:'单位名称'" style="width: 100%">
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="job.JOB_GW"
					data-options="label:'岗位名称'" style="width: 100%" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="job.INCOME"
					data-options="label:'月收入',prompt:'元/月'" style="width: 100%" />
			</div>
			
		</form>
		<div style="text-align: center; margin-top: 30px">
			<a href="javascript:void(0)" onclick="$('#theForm').form('submit');"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 16px">保存</span></a>
			<a href="javascript:void(0)" onclick="closeJobnojobOpr()"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 16px">取消</span></a>
		</div>

	</div>

	<div id="dlg" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#hjButtons'">
		<ul id="areaTree" class="easyui-tree">
		</ul>
		<div id="hjButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="cleanHj()" style="width: 80px">清除</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#dlg').dialog('close')" style="width: 80px">关闭</a>
		</div>
	</div>

	

	<script type="text/javascript">
		
		function cleanHj() {
			$("#HJ_AREA_NAME").textbox("setValue", "");
			$("#HJ_AREA").val('');
			$('#dlg').dialog('close');
		}
		
		function showAreaTree() {
			$('#dlg').dialog('open');
		}
		$(function() {
			var hr_id = localStorage.getItem("hr_id");
			$("#HR_ID").val(hr_id);
			$('#areaTree').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree'
				},
				onClick : function(node) {
					$("#HJ_AREA_NAME").textbox("setValue", node.text);
					$("#HJ_AREA").val(node.id);
					$('#dlg').dialog('close');
				}
			});
			$('#theForm')
					.form(
							{
								url : 'busi/hr/saveJobInfo.do',
								success : function(data) {
									try{
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
									}catch(e){
										alert(e);
									}
								}
							});
		});
	</script>
</body>
</html>
