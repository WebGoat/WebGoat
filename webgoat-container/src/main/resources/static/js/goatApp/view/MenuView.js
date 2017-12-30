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
			this.addSpinner();
			this.listenTo(this.collection,'menuData:loaded',this.render);
			// this.listenTo(this,'menu:click',this.accordionMenu);
			this.curLessonLinkId = '';
		},

		addSpinner: function() {
			//<i class="fa fa-spinner fa-spin"></i>
			this.$el.append($('<i>',{class:'fa fa-3x fa-spinner fa-spin'}));
		},

		removeSpinner: function() {
			this.$el.find('i.fa-spinner').remove();
		},

		// rendering top level menu
		render: function (){
			//for now, just brute force
			//TODO: refactor into sub-views/components
			this.removeSpinner();
			var items, catItems, stages;
			items = this.collection.models; // top level items
			var menuMarkup = '';
			var menuUl = $('<ul>',{class:'nano-content'});
			for(var i=0;i<items.length;i++) { //CATEGORY LEVEL
				var catId, category, catLink, catArrow, catLinkText, lessonName, stageName;
				catId = GoatUtils.makeId(items[i].get('name'));
				category = $('<li>',{class:'sub-menu ng-scope'});
				catLink = $('<a>',{'category':catId});
				catArrow = $('<i>',{class:'fa fa-angle-right pull-right'});
				catLinkText = $('<span>',{text:items[i].get('name')});

				catLink.append(catArrow);
				catLink.append(catLinkText);
				var self = this;
				catLink.click(_.bind(this.expandCategory,this,catId));
				category.append(catLink);
				// lesson level (first children level)
				//var lessons = new MenuItemView({items:items[i].get('children')}).render();
				var lessons=items[i].get('children');
				if (lessons) {
					var categoryLessonList = $('<ul>',{class:'slideDown lessonsAndStages',id:catId}); //keepOpen
					for (var j=0; j < lessons.length;j++) {
						var lessonItem = $('<li>',{class:'lesson'});
						var lessonName = polyglot.t(lessons[j].name);
						var lessonId = catId + '-' + GoatUtils.makeId(lessonName);
						if (this.curLessonLinkId === lessonId) {
							lessonItem.addClass('selected');
						}
						var lessonLink = $('<a>',{href:lessons[j].link,text:lessonName,id:lessonId});
						lessonLink.click(_.bind(this.onLessonClick,this,lessonId));
						lessonItem.append(lessonLink);
						//check for lab/stages
						categoryLessonList.append(lessonItem);
						if (lessons[j].complete) { 
							lessonItem.append($('<span>',{class:'glyphicon glyphicon-check lesson-complete'}));
						}
						var stages = lessons[j].children;
						for (k=0; k < stages.length; k++) {
							var stageItem = $('<li>',{class:'stage'});
							var stageName = stages[k].name;
							var stageId = lessonId +  '-stage' + k;
							if (this.curLessonLinkId === stageId) {
								stageItem.addClass('selected');
							}
							var stageLink = $('<a>',{href:stages[k].link,text:stageName,id:stageId});
							stageLink.click(_.bind(this.onLessonClick,this,stageId));
							stageItem.append(stageLink);
							categoryLessonList.append(stageItem);
							if (stages[k].complete) {
								stageItem.append($('<span>',{class:'glyphicon glyphicon-check lesson-complete'}));
							}
						}
					}
					category.append(categoryLessonList);
				}

				menuUl.append(category);
			}
			this.$el.html(menuUl);
			//if we need to keep a menu open
			if (this.openMenu) {
				$('#'+this.openMenu).show();
			}
		},

		updateMenu: function() {
			//for now ...
			this.collection.fetch();
		},

		onLessonClick: function (elementId) {
			$('#'+this.curLessonLinkId).removeClass('selected').parent().removeClass('selected');
			//update
			$('#'+elementId).addClass('selected').parent().addClass('selected');
			this.curLessonLinkId = elementId;
		},

		expandCategory: function (id) {
			if (id) {
			    //this.selectedCategory = id;
				this.accordionMenu(id);
			}
		},

		accordionMenu: function(id) {
	        if (this.openMenu !== id) {
	        	this.$el.find('#' + this.openMenu).slideUp(200);
	        	this.$el.find('#' + id).slideDown(300);
	        	this.openMenu = id;
	        } else { //it's open
	            this.$el.find('#' + id).slideUp(300).attr('isOpen', 0);
	            this.openMenu = null;
	            return;
	        }
		}
	});
});