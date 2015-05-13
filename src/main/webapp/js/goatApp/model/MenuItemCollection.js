define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/MenuItemModel'], 
	function($,_,Backbone,MenuItemModel) {

	return Backbone.Collection.extend({
		model: MenuItemModel,
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
});