define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/HintModel'], 
	
	function($,
	_,
	Backbone,
	HintModel) {
		return Backbone.Collection.extend({
			model: HintModel,
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