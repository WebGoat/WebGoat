define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/MenuCollection',
	'goatApp/view/MenuItemView'], 
	function($,_,Backbone,MenuCollection,MenuItemView) {

	return  Backbone.View.extend({
		el:'#menuContainer',
		//TODO: set template
		initialize: function() {
			this.collection = new MenuCollection();
			this.listenTo(this.collection,'menuData:loaded',this.render);
		},
		// rendering top level menu
		render: function (model){
			var items = this.collection.models; // top level items
			var menuMarkup = '';
			var menuUl = $('<ul>',{class:'nano-content'});
			for(var i=0;i<items.length;i++) {
				var category = $('<li>',{class:'sub-menu'});
				var catLink = $('<a>',{text:items[i].get('name')});
				category.append(catLink);
				// lesson level (first children level)
				var categoryLessonList = $('<ul>',{class:'slideDown lessonsAndStages'});
				var catItems = new MenuItemView({items:items[i].get('children')}).render();
				for (var j=0;j< catItems.length;j++) {
					categoryLessonList.append(catItems[j]);
				}
				category.append(categoryLessonList);
				menuUl.append(category);
			}
			this.$el.append(menuUl);

		}
	});
});