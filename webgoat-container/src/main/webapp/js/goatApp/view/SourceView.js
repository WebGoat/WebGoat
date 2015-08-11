define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonSourceModel',
	'goatApp/view/HelpView'],
function($,
	_,
	Backbone,
	LessonSourceModel,
	HelpView) {
	return HelpView.extend({
		helpElement:{'helpElement':'source','value':true},
		loadedMessage:'source:loaded',
		el:'#lessonHelpWrapper .lessonHelp.lessonPlan', //Check this
		initialize: function() {
			this.model = new LessonSourceModel();
			this.listenTo(this.model,'loaded',this.onModelLoaded);
			this.model.loadData();
		}
	});
});