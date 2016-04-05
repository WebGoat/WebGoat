define(['jquery',
	'underscore',
	'backbone'],
function($,_,Backbone) {
	return Backbone.View.extend({
		el:'#header #lesson-title-wrapper',
		
		render:function(title) {
			var lessonTitleEl = $('<h1>',{id:'lesson-title',text:title});
			this.$el.html(lessonTitleEl);
		}
	});
});