define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/MenuModel'], 
	function($,_,Backbone,MenuModel) {

	return Backbone.Collection.extend({
		model: MenuModel,
		url:'service/lessonmenu.mvc',
		initialize: function () {
			var self = this;
			this.fetch().then(function (data) {
				this.models = data;
				self.onDataLoaded();
			});
		},
		onDataLoaded:function() {
			this.trigger('menuData:loaded');
		}
	});
})