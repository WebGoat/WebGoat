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
			if (this.get('content').indexOf('Solution  is not available. Contact') === 0) {
				this.set('content',null);
			}
		}
		
	});
});