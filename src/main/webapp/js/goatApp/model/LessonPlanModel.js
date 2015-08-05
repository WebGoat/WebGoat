define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/HTMLContentModel'],
	function($,
		_,
		Backbone,
		HTMLContentModel) {
	return HTMLContentModel.extend({
		url:'service/lessonplan.mvc',
		checkNullModel: function() {
			if (this.get('content').indexOf('Could not find lesson plan for') > -1) {
				this.set('content',null);
			}
		}
	});
});