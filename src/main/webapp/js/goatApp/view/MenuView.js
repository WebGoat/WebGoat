define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/MenuCollection',
	'goatApp/view/MenuItemView',
	'goatApp/support/GoatUtils'], 
	function(
		$,
		_,
		Backbone,
		MenuCollection,
		MenuItemView,
		GoatUtils) {
	return  Backbone.View.extend({
		el:'#menuContainer',
		//TODO: set template
		initialize: function() {
			this.collection = new MenuCollection();
			this.listenTo(this.collection,'menuData:loaded',this.render);
		},
		// rendering top level menu
		render: function (model){
			//for now, just brute force
			//TODO: refactor into sub-views/components
			var items, catItems, stages;
			items = this.collection.models; // top level items
			var menuMarkup = '';
			var menuUl = $('<ul>',{class:'nano-content'});
			for(var i=0;i<items.length;i++) { //CATEGORY LEVEL
				var category = $('<li>',{class:'sub-menu ng-scope'});
				var catLink = $('<a>');
				var catArrow = $('<i>',{class:'fa fa-angle-right pull-right'});
				var catLinkText = $('<span>',{text:items[i].get('name')});
				catLink.append(catArrow);
				catLink.append(catLinkText);
				//TODO: bind catLink to accordion and selection method
				category.append(catLink);
				// lesson level (first children level)
				//var lessons = new MenuItemView({items:items[i].get('children')}).render();
				var lessons=items[i].get('children');
				if (lessons) {
					var categoryLessonList = $('<ul>',{class:'slideDown lessonsAndStages'}); //keepOpen
					for (var j=0; j < lessons.length;j++) {
						var lessonItem = $('<li>');
						var lessonLink = $('<a>',{href:lessons[j].link,text:lessons[j].name,id:GoatUtils.makeId(lessons[j].name)});
						lessonItem.append(lessonLink);
						//check for lab/stages
						categoryLessonList.append(lessonLink);
						var stages = lessons[j].children;
						for (k=0; k < stages.length; k++) {
							var stageSpan = $('<span>');
							var stageLink = $('<a>',{href:stages[k].link,text:stages[k].name,id:GoatUtils.makeId(stages[k].name)});
							stageSpan.append(stageLink);
							categoryLessonList.append(stageSpan);
						}
					}
					category.append(categoryLessonList);
				}
				
				menuUl.append(category);
			}
			this.$el.append(menuUl);

		}
	});
});