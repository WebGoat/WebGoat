define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonHintModel'], 
	function($,_,Backbone,LessonHintModel) {

	return Backbone.Collection.extend({
		model: LessonHintModel,
		url:'service/hint.mvc',
		initialize: function () {
			var self = this;
			this.fetch().then(function (data) {
				this.models = data;
				self.onDataLoaded();
			});
		},
		onDataLoaded:function() {
			this.trigger('hints:loaded');//copied over as boiler-plate ... use this event trigger?
		},
		checkNullModel:function() {
			//
		}
	});
});