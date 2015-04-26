//LessonContentView
define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonContentData'], 
function($,_,Backbone,LessonData) {
	var contentView = Backbone.View.extend({
		el:'#lessonContent',
		initialize: function(options) {
			//this.content = options.content;
			this.lessonData = {};
			this.listenTo(this.lessonData,'sync',this.render);
		},
		loadLesson: function(options) {
			this.lessonData = new LessonData(options.screen,options.menu);
			
		},
		render: function() {
			alert('render');
			this.$el.html(this.content);
		}
	});
});