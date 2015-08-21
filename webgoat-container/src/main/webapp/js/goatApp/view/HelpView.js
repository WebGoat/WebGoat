define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/SourceModel'],
function($,
	_,
	Backbone,
	SourceModel) {
	return Backbone.View.extend({
		el:'#lessonHelpWrapper .lessonHelp.lessonPlan', //Check this
		initialize: function() {
			this.model = new SourceModel();
			this.listenTo(this.model,'loaded',this.onModelLoaded);
			this.model.loadData();
		},
		render:function(title) {
			
		},
		onModelLoaded: function() {
			this.trigger(this.loadedMessage,this.helpElement);
		}
	});
});