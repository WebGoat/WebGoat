define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonHintModel'], 
	
	function($,
	_,
	Backbone,
	LessonHintModel) {
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
				this.trigger('loaded');
			},

			checkNullModel:function() {
				if (this.models[0].indexOf('There are no hints defined.') > -1) {
					this.reset([]);
					//return this.models;
				}
			}
		});
});