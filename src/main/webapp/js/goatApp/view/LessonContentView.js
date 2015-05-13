//LessonContentView
define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonContentData'], 
function($,_,Backbone,LessonData) {
	return Backbone.View.extend({
		el:'#lessonContentWrapper', //TODO << get this fixed up in DOM
		initialize: function(options) {
			options = options || {};
		},
		render: function() {
			//alert('render');
			this.$el.html(this.model.get('content'));
		}
	});

	
});