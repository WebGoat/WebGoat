var menuData = Backbone.Model.extend({
		urlRoot:'/webgoat/service/lessonmenu.mvc',
		defaults: {
			items:null,
			selectedItem:null
		},
		initialize: function () {
			var self = this;
			this.fetch().then(function(menuItems){
				menuItems = goatUtils.enhanceMenuData(menuItems,this.selectedItem);
				self.items = menuItems;
			});
		},
	
		update: function() {
			var self = this;
			this.fetch().then(function(data) {
				self.items = data;
				self.render(0);
			});
		}
	});