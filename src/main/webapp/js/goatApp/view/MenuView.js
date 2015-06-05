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
		el:'#menu-container',
		//TODO: set template
		initialize: function() {
			this.collection = new MenuCollection();
			this.listenTo(this.collection,'menuData:loaded',this.render);
			this.listenTo(this,'menu:click',this.accordionMenu);
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
				var catId, category, catLink, catArrow, catLinkText;
				catId = GoatUtils.makeId(items[i].get('name'));
				category = $('<li>',{class:'sub-menu ng-scope'});
				catLink = $('<a>',{'category':catId});
				catArrow = $('<i>',{class:'fa fa-angle-right pull-right'});
				catLinkText = $('<span>',{text:items[i].get('name')});

				catLink.append(catArrow);
				catLink.append(catLinkText);
				//TODO: refactor this along with sub-views/components
				var self = this;
				catLink.click(_.bind(this.expandCategory,this,catId));
				//TODO: bind catLink to accordion and selection method
				category.append(catLink);
				// lesson level (first children level)
				//var lessons = new MenuItemView({items:items[i].get('children')}).render();
				var lessons=items[i].get('children');
				if (lessons) {
					var categoryLessonList = $('<ul>',{class:'slideDown lessonsAndStages',id:catId}); //keepOpen
					for (var j=0; j < lessons.length;j++) {
						var lessonItem = $('<li>');
						var lessonLink = $('<a>',{href:lessons[j].link,text:lessons[j].name,id:GoatUtils.makeId(lessons[j].name)});
						lessonItem.append(lessonLink);
						//check for lab/stages
						categoryLessonList.append(lessonItem);
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
			//if we need to keep a menu open
			if (this.openMenu) {
				this.accordionMenu(this.openMenu);
			}
		},
		expandCategory: function (id) {
			if (id) {
				this.accordionMenu(id);
			}
		},
		accordionMenu: function(id) {
	        if (this.openMenu !== id) {
	        	this.$el.find('#' + id).slideDown(300);
	        } else { //it's open
	            this.$el.find('#' + id).slideUp(300).attr('isOpen', 0);
	            return;
	        }
	        this.openMenu = id;
	        this.$el.find('.lessonsAndStages').not('ul#' + id).slideUp(300);
	        /* //legacy angular code that may be usefl 
	        if ($scope.expandMe) {
	            $('ul#' + id).slideDown(300).attr('isOpen', 1);
	        }
	        */
		}
	});
});