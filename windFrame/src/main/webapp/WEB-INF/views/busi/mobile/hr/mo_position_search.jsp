<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>职位搜索</title>
<tags:commonMobileHead />
</head>
<body>
	<div class="easyui-navpanel" style="position: relative; padding: 20px">
		<header>
			<div class="m-toolbar">
				<div class="m-left">
					<a href="javascript:void(0)" class="easyui-linkbutton m-back"
						onclick="showMobileHrCenterUI()" plain="true" outline="true">返回</a>
				</div>
				<div class="m-title">职位搜索</div>
			</div>
		</header>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center;"
			data-options="noheader:false, closed:true, border:false"></div>
		<form id="theForm" method="post" style="width: 100%" action="busi/position/searchMobilePositionValidListUI.do">
			<input type="hidden" id="WANT_JOB_AREA" name="P_WORK_AREA" />
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="p_names"
					data-options="label:'职位名称',prompt:'多个职位名称之间用逗号分隔'"
					style="width: 100%" />
			</div>

			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" id="WANT_JOB_AREA_NAME"
					data-options="label:'工作地'" style="width: 100%">
			</div>

			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="pay_botton_bgn"
					data-options="label:'起薪',prompt:'元/月'" style="width: 100%" />
			</div>

		</form>
		<div style="text-align: center; margin-top: 30px">
			<a href="javascript:void(0)" onclick="$('#theForm').submit();"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 16px">搜索</span></a>
		</div>
	</div>



	<div id="dlgWantArea" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#wantButtons'">
		<ul id="areaWantTree" class="easyui-tree">
		</ul>
		<div id="wantButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanWantBtn" style="width: 80px">清除</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#dlgWantArea').dialog('close')" style="width: 80px">关闭</a>
		</div>
	</div>

	<script type="text/javascript">
		function showMobileHrCenterUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrCenterUI.do";
		}
		$(function() {
			var areaTreeWant = new AreaTree('dlgWantArea', 'WANT_JOB_AREA',
					'WANT_JOB_AREA_NAME');
			$('#areaWantTree').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree'
				},
				onClick : function(node) {
					areaTreeWant.nodeClick(node);
				}
			});
			$('#WANT_JOB_AREA_NAME').textbox({
				buttonText : '选择',
				onClickButton : function() {
					areaTreeWant.showAreaTree();
				},
				onClick : function() {
					areaTreeWant.showAreaTree();
				},
				editable : false
			});
			$("#cleanWantBtn").bind('click', function() {
				areaTreeWant.cleanChosed();
			});
			$('#datagrid')
					.datagrid(
							{
								method : 'POST',
								showHeader : false,
								singleSelect : false,
								closed:true,
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
