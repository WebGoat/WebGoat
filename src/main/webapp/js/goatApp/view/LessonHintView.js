define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonHintCollection'],
function($,
	_,
	Backbone,
	LessonHintCollection) {
	return Backbone.View.extend({
		el:'#lessonHelpWrapper .lessonHelp.lessonHint',
		initialize: function() {
			this.collection = new LessonHintCollection();
			this.listenTo(this.collection,'hints:loaded',this.onModelLoaded);
		},
		render:function(title) {
			
		},
		onModelLoaded: function() {
			this.trigger('hints:loaded',{'helpElement':'hints','value':true})
		}
	});
});