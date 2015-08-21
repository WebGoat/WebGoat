define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/SolutionModel'],
	//TODO: create a base 'HelpView class'
function($,_,Backbone,SolutionModel) {
	return Backbone.View.extend({
		el:'#lessonHelpWrapper .lessonHelp.lessonSolution', //Check this
		initialize: function() {
			this.model = new SolutionModel();
			this.listenTo(this.model,'loaded',this.onModelLoaded);
			this.model.loadData();
			//TODO: handle error cases
		},
		render:function(title) {
			
		},
		onModelLoaded: function() {
			this.trigger('solution:loaded',{'helpElement':'solution','value':true});
		}
	});
});