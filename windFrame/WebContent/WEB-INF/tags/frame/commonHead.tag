<%@ tag pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<link rel="stylesheet" type="text/css"
	href="resource/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="resource/jquery-easyui-1.5.1/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="resource/jquery-easyui-1.5.1/demo/demo.css">
<script type="text/javascript"
	src="resource/jquery-easyui-1.5.1/jquery.min.js"></script>
<script type="text/javascript"
	src="resource/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="resource/jquery-easyui-1.5.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="resource/public/js/areatree.js"></script>
<script type="text/javascript">
	var baseHref = "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/";
	var regexEnum = {
		intege : "^-?[1-9]\\d*$", // 整数
		intege1 : "^[1-9]\\d*$", // 正整数
		intege2 : "^-[1-9]\\d*$", // 负整数
		num : "^([+-]?)\\d*\\.?\\d+$", // 数字
		num1 : "^(0|[1-9]\\d*)$", // 正数（正整数 + 0）
		num2 : "^-[1-9]\\d*|0$", // 负数（负整数 + 0）
		decmal : "^([+-]?)\\d*\\.\\d+$", // 浮点数
		decmal1 : "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$", // 正浮点数
		decmal2 : "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$",// 负浮点数
		decmal3 : "^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$",// 浮点数
		decmal4 : "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$",// 非负浮点数（正浮点数 + 0）
		decmal5 : "^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$",// 非正浮点数（负浮点数 +// 0）
		decmal6 : "^([1-9]\\d*|0)(\\.\\d{1,2})?$", //非负浮点数，精确到小数点后两位
		email : "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", // 邮件
		color : "^[a-fA-F0-9]{6}$", // 颜色
		url : "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$", // url
		chinese : "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$", // 仅中文
		ascii : "^[\\x00-\\xFF]+$", // 仅ACSII字符
		zipcode : "^\\d{6}$", // 邮编
		mobile : "^(13|15|14|18)[0-9]{9}$", // 手机
		ip4 : "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$", // ip地址
		notempty : "^\\S+$", // 非空
		picture : "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$", // 图片
		rar : "(.*)\\.(rar|zip|7zip|tgz)$", // 压缩文件
		date : "^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$", // 日期
		qq : "^[1-9]*[1-9][0-9]*$", // QQ号码
		tel : "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$", // 电话号码的函数(包括验证国内区号,国际区号,分机号)
		username : "^\\w+$", // 用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
		letter : "^[A-Za-z]+$", // 字母
		letter_u : "^[A-Z]+$", // 大写字母
		letter_l : "^[a-z]+$", // 小写字母
		idcard : "^[1-9](\\d{17}|\\d{16}(\\d|X|x))$", // 身份证
		dianxinmobile : "^153[0-9]{8}$|189[0-9]{8}$"
	};
	var aCity = {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	};
	function isValidIdcard(sId) {
		var iSum = 0;
		if (!/^\d{17}(\d|x)$/i.test(sId))
			return false; // "你输入的身份证长度或格式错误"
		sId = sId.replace(/x$/i, "a");
		if (aCity[parseInt(sId.substr(0, 2))] == null)
			return false; // "你的身份证地区非法"
		var sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2))
				+ "-" + Number(sId.substr(12, 2));
		var d = new Date(sBirthday.replace(/-/g, "/"));
		if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d
				.getDate()))
			return false; // "身份证上的出生日期非法"

		for (var i = 17; i >= 0; i--)
			iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
		if (iSum % 11 != 1)
			return false; // "你输入的身份证号非法"
		return true;// aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女")
	}
	function isPositiveInteger(str) {
		var pattern = new RegExp(regexEnum.num1);
		return pattern.test(str);
	}
	$.extend($.fn.validatebox.defaults.rules, {
		validIdcard : {
			validator : function(value, param) {
				return isValidIdcard(value);
			},
			message : '请输入正确的身份证号码'
		}
	});
	$.extend($.fn.validatebox.defaults.rules, {
		/*必须和某个字段相等*/
		equalTo : {
			validator : function(value, param) {
				return $("#" + param[0]).val() == value;
			},
			message : '字段不匹配'
		}
	});
	(function($) {
		$.fn.serializeJson = function() {
			var serializeObj = {};
			var array = this.serializeArray();
			var str = this.serialize();
			$(array).each(
					function() {
						if (serializeObj[this.name]) {
							if ($.isArray(serializeObj[this.name])) {
								serializeObj[this.name].push(this.value);
							} else {
								serializeObj[this.name] = [
										serializeObj[this.name], this.value ];
							}
						} else {
							serializeObj[this.name] = this.value;
						}
					});
			return serializeObj;
		};
	})(jQuery);
	function showOprTip(id, msg, color) {
		$("#" + id).html(msg);
		$("#" + id).css('color', color);
		$("#" + id).panel('open');
	}
	function closeOrpTip(id) {
		$("#" + id).html('');
		$("#" + id).panel('close');
	}
	var mapCommon = {};
</script>