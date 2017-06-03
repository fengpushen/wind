function AreaTree(areaDlgId, areaCodeId, areaNameId) {

	this.areaDlgId = areaDlgId;
	this.areaCodeId = areaCodeId;
	this.areaNameId = areaNameId;

	if (typeof AreaTree._initialized == "undefined") {
		AreaTree.prototype.showAreaTree = function() {
			try {
				$('#' + areaDlgId).dialog('open');
			} catch (e) {
				alert(e.message);
			}
		};
		AreaTree.prototype.closeAreaTree = function() {
			$('#' + areaDlgId).dialog('close');
		};
		AreaTree.prototype.cleanChosed = function() {
			$('#' + areaNameId).textbox("setValue", "");
			$('#' + areaCodeId).val('');
			$('#' + areaDlgId).dialog('close');
		};
		AreaTree.prototype.nodeClick = function(node) {
			alert(areaNameId);
			$('#' + areaNameId).textbox("setValue", node.text);
			$('#' + areaCodeId).val(node.id);
			$('#' + areaDlgId).dialog('close');
		};
		AreaTree._initialized = true;
	}
}