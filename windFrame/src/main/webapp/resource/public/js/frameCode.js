var frameCode = function() {

	var codesMap = {};
	function getCodes(codeName) {
		var codes = codesMap[codeName];
		if (codes == null) {
			$.ajax({
				type : "post",
				url : "frame/loadCode.do",
				data : "codeName=" + codeName,
				cache : false,
				async : false,
				success : function(result) {
					codes = {};
					if (result != null && result != '') {
						var json = $.parseJSON(result);
						for (var i = 0, len = json.length; i < len; i++) {
							codes[json[i].id] = json[i].text;
						}
					}
					codesMap[codeName] = codes;
				}
			});
		}
		return codes;
	}

	return {
		getCodeValue : function(codeName, codeKey) {
			var codes = getCodes(codeName);
			var value = codes[codeKey];
			if (value == null) {
				value = codeKey;
			}
			return value;
		}
	};
};

var theFrameCode = frameCode();