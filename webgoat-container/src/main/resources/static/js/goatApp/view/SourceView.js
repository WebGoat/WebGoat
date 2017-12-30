define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/SourceModel',
	'goatApp/view/HelpView'],
function($,
	_,
	Backbone,
	SourceModel,
	HelpView) {
	return HelpView.extend({
		helpElement:{'helpElement':'source','value':true},
		loadedMessage:'source:loaded',
		el:'#lessonHelpWrapper .lessonHelp.lessonPlan', //Check this
		initialize: function() {
			this.model = new SourceModel();
			this.listenTo(this.model,'loaded',this.onModelLoaded);
			this.model.loadData();
		}
	});
});