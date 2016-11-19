define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/HTMLContentModel'],
	function($,
		_,
		Backbone,
		HTMLContentModel) {
	return HTMLContentModel.extend({
		url:'service/solution.mvc',
		checkNullModel: function() {
			if (this.get('content').indexOf('Could not find the solution file or solution file does not exist') === 0) {
				this.set('content',null);
			}
		}
		
	});
});