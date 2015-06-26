define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/HTMLContentModel'],
	function($,
		_,
		Backbone,
		HTMLContentModel) {
	return HTMLContentModel.extend({
		url:'service/source.mvc',
		checkNullModel: function () {
			//TODO: move this function into HTMLContentModel and make the string a property of this 'child' model
			if (this.get('content').indexOf("No source listing found") > -1) {
				this.set('content',null);
			}
		}
		
	});
});