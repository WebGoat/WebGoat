define(['jquery','underscore','backbone','goatApp/model/goatMenu'], function($,_,Backbone,MenuData) {

	return  Backbone.View.extend({
		el:'#menuContainer',
		//TODO: set template

		render: function (model){
			//TODO: implement own HTML Encoder
			this.$el.html(buildMenu(items));
		},
		buildMenu: function(items) {

			var menuData = new MenuData();

			var i, j, k, $wholeMenu, $menuCat, itemClass, $lessonItem, lessons, stages, $stageItem;
			var _renderMenu = function (items) {
				$wholeMenu = $('<ul>');
				for (var i=0;i<items.length;i++){
					// should be at category level ...
					itemClass = (items[i].class || '');
					if (items[i].type && items.type === 'CATEGORY') {
						itemClass += 'fa-angle-right pull-right';
					}
					var $menuCat = $('<li>',{text:items[i].name,class:itemClass});
					$wholeMenu.append($menuCat);
					var lessonList = $('<ul>',{class:'slideDown lessonsAndStages' + items[0].displayClass,id:items[0].id}) //
					// first tier lessons
					var lessons = items[i].children;
					for (j=0;j<lessons.length;j++) {
						itemClass = (lessons[j].class || '');
						$lessonItem = $('<li>',{text:lessons[j].name,id:lessons[j].id});//add click
						lessonList.append($lessonItem);
						//stages (children of lesson)
						stages = lessons[j].children;
						for (k=0;k<stages.length;k++) {
							$stageItem = $('<li>',{text:stages[k].name,id:stages[k].id});
							lessonList.append($stageItem);
						}
					}
					$menuCat.append(lessonList);
				}
				return $wholeMenu;
				//$wholeMenu.append($menuCat);
				$(goatConstants.getDOMContainers().lessonMenu).html('').append($wholeMenu);
			};
			
		}
	});
});