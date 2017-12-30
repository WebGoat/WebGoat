define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonPlanModel'],
function($,
	_,
	Backbone,
	LessonPlanModel) {
	return Backbone.View.extend({
		el:'#lessonHelpWrapper .lessonHelp.lessonPlan', //Check this
		initialize: function() {
			this.model = new LessonPlanModel();
			this.listenTo(this.model,'loaded',this.onModelLoaded);
			this.model.loadData();
		},
		
		render:function(title) {
			
		},

		onModelLoaded: function() {
			this.trigger('plan:loaded',{'helpElement':'plan','value':true});
		}
	});
});