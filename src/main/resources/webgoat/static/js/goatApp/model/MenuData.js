//var goatApp = goatApp || {};

define(['jquery','underscore','backbone'], function($,_,Backbone) {

var menuData = Backbone.Model.extend({
		urlRoot:'service/lessonmenu.mvc',
		defaults: {
			items:null,
			selectedItem:null
		},
		initialize: function () {
			var self = this;
			this.fetch().then(function(menuItems){
				menuItems = goatUtils.enhanceMenuData(menuItems,this.selectedItem);
				this.setDataItems(menuItems);
			});
		},

		update: function() {
			var self = this;
			this.fetch().then(function(menuItems) {
				menuItems = goatUtils.enhanceMenuData(menuItems,this.selectedItem);
				self.setDataItems(menuItems);
			});
		},

		setDataItems: function (data) {
			this.items = data;
		}
	});

});
