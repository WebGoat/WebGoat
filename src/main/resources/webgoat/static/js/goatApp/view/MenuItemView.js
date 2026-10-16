define(['jquery',
	'underscore',
	'backbone',
	'goatApp/support/GoatUtils',
	'goatApp/view/MenuItemView'],
	function(
		$,
		_,
		Backbone,
		GoatUtils,
		MenuItemView) {

	return Backbone.View.extend({
		initialize: function(options) {
			options = options || {};
			this.items = options.items;
		},
		render: function() {
			var viewItems = [];
			for (var i=0;i<this.items.length;i++) {
				var listItem = $('<li>',{text:this.items[i].name});
				//viewItems
				viewItems.push(listItem);
			}
			return viewItems;
		}

	});

});
