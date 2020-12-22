<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/frame"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>个人信息管理</title>
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
				<div class="m-title">个人信息管理</div>
			</div>
		</header>
		<div id="oprTip" class="easyui-panel"
			style="width: 100%; text-align: center;"
			data-options="noheader:false, closed:true, border:false"></div>
		<form id="theForm" method="post" style="width: 100%">
			<input type="hidden" id="HJ_AREA" name="HJ_AREA" /> <input
				type="hidden" id="WANT_JOB_AREA" name="want_job.WANT_JOB_AREA" />
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox"
					data-options="label:'身份证', disabled:true" style="width: 100%"
					value="${IDCARD }">
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox"
					data-options="label:'姓名', disabled:true" style="width: 100%"
					value="${HR_NAME }">
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="PHONE"
					data-options="label:'联系电话'" style="width: 100%" value="${PHONE }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="ADDRESS"
					data-options="label:'家庭地址'" style="width: 100%" value="${ADDRESS }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="JNTC"
					data-options="label:'技能特长',prompt:'多个特长之间用逗号分隔'"
					style="width: 100%" value="${JNTC }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="QQ" data-options="label:'QQ'"
					style="width: 100%" value="${QQ }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="EMAIL"
					data-options="label:'电子邮箱'" style="width: 100%" value="${EMAIL }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-combobox" name="NATION" style="width: 100%"
					data-options="
					url:'frame/loadCode.do?codeName=nation',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false, label:'民族'"
					value="${NATION }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-combobox" name="DEGREE" style="width: 100%"
					data-options="
					url:'frame/loadCode.do?codeName=degree',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false, label:'文化程度'"
					value="${DEGREE }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" id="HJ_AREA_NAME"
					data-options="label:'户籍地'" style="width: 100%"
					value="${HJ_AREA_NAME }">
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="SCHOOL"
					data-options="label:'毕业学校'" style="width: 100%" value="${SCHOOL }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="GRA_YEAR"
					data-options="label:'毕业年份'" style="width: 100%"
					value="${GRA_YEAR }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="PRO" data-options="label:'所学专业'"
					style="width: 100%" value="${PRO }" />
			</div>

			<div style="margin-bottom: 10px">
				<input class="easyui-combobox" name="IS_JOB" id="IS_JOB"
					style="width: 100%"
					data-options="
					url:'frame/loadCode.do?codeName=boolean',
					method:'post',
					valueField:'id',
					textField:'text',
					panelHeight:'auto', editable:false, required:true, label:'是否就业'"
					value="${IS_JOB }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" id="WANT_JOB_AREA_NAME"
					data-options="label:'意向工作地'" style="width: 100%"
					value="${WANT_JOB_AREA_NAME }">
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="WANT_JOB_NAME"
					data-options="label:'意向职位',prompt:'多个职位之间用逗号分隔'"
					style="width: 100%" value="${WANT_JOB_NAME }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-textbox" name="WANT_INCOME"
					data-options="label:'意向待遇',prompt:'元/月'" style="width: 100%"
					value="${WANT_INCOME }" />
			</div>
			<div style="margin-bottom: 10px">
				<input class="easyui-combobox" name="want_job.service_codes"
					id="service_codes" style="width: 100%" />
			</div>
		</form>
		<div style="text-align: center; margin-top: 30px">
			<a href="javascript:void(0)" onclick="$('#theForm').form('submit');"
				class="easyui-linkbutton" plain="true" outline="true"
				style="width: 100px; height: 35px"><span style="font-size: 16px">保存</span></a>
		</div>
		<footer style="padding: 2px 3px">
			<a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
				outline="true" onclick="showMobileHrJobNOjobMgeUI()"
				style="width: 70px">入离职登记</a> <a href="javascript:void(0)"
				class="easyui-linkbutton" plain="true" outline="true"
				onclick="unbind()" style="width: 60px">解除绑定</a>
		</footer>

	</div>

	<div id="dlg" class="easyui-dialog" title="选择"
		style="width: 250px; height: 350px; padding: 10px;"
		data-options="iconCls:'icon-save',closed:true,buttons: '#hjButtons'">
		<ul id="areaTree" class="easyui-tree">
		</ul>
		<div id="hjButtons" style="text-align: center;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cleanHjBtn" style="width: 80px">清除</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				onclick="$('#dlg').dialog('close')" style="width: 80px">关闭</a>
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
		function showMobileHrJobNOjobMgeUI() {
			window.location.href = baseHref
					+ "busi/hr/showMobileHrJobNOjobMgeUI.do";
		}
		function showMobileHrCenterUI() {
			window.location.href = baseHref + "busi/hr/showMobileHrCenterUI.do";
		}
		function unbind() {
			window.location.href = baseHref + "busi/hr/unbindHr.do";
		}
		$(function() {
			var areaTreeWant = new AreaTree('dlgWantArea', 'WANT_JOB_AREA',
					'WANT_JOB_AREA_NAME');
			var areaTree = new AreaTree('dlg', 'HJ_AREA', 'HJ_AREA_NAME');
			$('#areaTree').tree({
				url : 'busi/common/loadTree.do',
				method : 'post',
				queryParams : {
					'treeName' : 'busi_com_area_tree',
					'rootId' : '430900000000'
				},
				onClick : function(node) {
					areaTree.nodeClick(node);
				}
			});
			$('#HJ_AREA_NAME').textbox({
				buttonText : '选择',
				onClickButton : function() {
					areaTree.showAreaTree();
				},
				onClick : function() {
					areaTree.showAreaTree();
				},
				editable : false,
				required : true
			});
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
			$("#cleanHjBtn").bind('click', function() {
				areaTree.cleanChosed();
			});
			$("#cleanWantBtn").bind('click', function() {
				areaTreeWant.cleanChosed();
			});
			var needServicesJson = '${needServicesJson}';
			$("#service_codes").combobox({
				label : '就业服务需求',
				url : 'frame/loadCode.do?codeName=service_code',
				method : 'post',
				valueField : 'id',
				textField : 'text',
				multiple : true,
				panelHeight : '200',
				editable : false,
				onLoadSuccess : function() {
					if (needServicesJson != '') {
						var services = eval('(' + needServicesJson + ')');
						var val = $(this).combobox("getData");
						for (var i = 0; i < val.length; i++) {
							for (var j = 0; j < services.length; j++) {
								if (services[j].SERVICE_CODE == val[i].id) {
									$(this).combobox("select", val[i].id);
									break;
								}
							}
						}
					}
				}
			});
			$('#theForm')
					.form(
							{
								url : 'busi/hr/saveHrInfoSelfMo.do',
								success : function(data) {
									var rst = eval('(' + data + ')');
									if (rst.isSucc) {
										showOprTip("oprTip", "操作成功", "green");
										window.location.href = baseHref
												+ "busi/hr/showMobileHrCenterUI.do";
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
