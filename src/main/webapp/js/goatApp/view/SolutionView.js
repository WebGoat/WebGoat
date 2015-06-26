define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonSolutionModel'],
function($,_,Backbone,LessonSolutionModel) {
	return Backbone.View.extend({
		el:'#lessonHelpWrapper .lessonHelp.lessonSolution', //Check this
		initialize: function() {
			this.model = new LessonSolutionModel();
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