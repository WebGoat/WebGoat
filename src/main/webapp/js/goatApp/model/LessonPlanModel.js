define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/HTMLContentModel'],
	function($,
		_,
		Backbone,
		HTMLContentModel) {
	//TODO: make a base class to extend for items with 'traditional data' (e.g. LessonContentData, this ... others?)
	return HTMLContentModel.extend({
		url:'service/lessonplan.mvc',
		checkNullModel: function() {
			if (this.get('content').indexOf('Plan is not available for this lesson.') > -1) {
				this.set('content',null);
			}
		}
		
	});
});