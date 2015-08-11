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
			if (this.get('content').indexOf("Could not find the source file or") > -1) {
				this.set('content',null);
			}
		}
		
	});
});