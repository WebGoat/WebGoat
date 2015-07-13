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
			if (this.get('content').indexOf('Plan is not available for this lesson.') > -1) {
				this.set('content',null);
			}
		}
		
	});
});