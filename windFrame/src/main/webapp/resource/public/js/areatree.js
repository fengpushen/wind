function AreaTree(areaDlgId, areaCodeId, areaNameId) {

	this.areaDlgId = areaDlgId;
	this.areaCodeId = areaCodeId;
	this.areaNameId = areaNameId;

	if (typeof AreaTree._initialized == "undefined") {
		AreaTree.prototype.showAreaTree = function() {
			$('#' + this.areaDlgId).dialog('open');
		};
		AreaTree.prototype.closeAreaTree = function() {
			$('#' + this.areaDlgId).dialog('close');
		};
		AreaTree.prototype.cleanChosed = function() {
			$('#' + this.areaNameId).textbox("setValue", "");
			$('#' + this.areaCodeId).val('');
			$('#' + this.areaDlgId).dialog('close');
		};
		AreaTree.prototype.nodeClick = function(node) {
			$('#' + this.areaNameId).textbox("setValue", node.text);
			$('#' + this.areaCodeId).val(node.id);
			$('#' + this.areaDlgId).dialog('close');
		};
		AreaTree._initialized = true;
	}
}