define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonSourceModel'],
function($,
	_,
	Backbone,
	LessonSourceModel) {
	return Backbone.View.extend({
		el:'#lessonHelpWrapper .lessonHelp.lessonPlan', //Check this
		initialize: function() {
			this.model = new LessonSourceModel();
			this.listenTo(this.model,'loaded',this.onModelLoaded);
			this.model.loadData();
		},
		render:function(title) {
			
		},
		onModelLoaded: function() {
			// ???
		}
	});
});