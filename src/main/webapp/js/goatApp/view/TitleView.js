define(['jquery',
	'underscore',
	'backbone'],
function($,_,Backbone) {
	return Backbone.View.extend({
		el:'#lessonTitleWrapper',
		render:function(title) {
			this.$el.find('.lessonTitle').html(title);
		}
	});
});