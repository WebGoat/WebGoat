// define(['jquery',
// 	'underscore',
// 	'backbone',
// 	'goatApp/model/MenuItemModel',
// 	'goatApp/model/MenuItemCollection'],
// 	function($,_,Backbone,MenuItemModel,MenuItemCollection) {

// 	return Backbone.View.extend({
// 		initialize: function(options) {
// 			options = options || {};
// 			//if children, generate Stage views
// 			this.collection = new MenuItemCollection();
// 			this.collection.set(options.collection);
// 		},
// 		render: function() {
// 			//example
// 			/*
// 			"name": "Using an Access Control Matrix",
// 			"type": "LESSON",
// 			"children": [ ],
// 			"complete": false,
// 			"link": "#attack/18/200",
// 			"showSource": true,
// 			"showHints": true
// 			*/
// 			var link = $('<a>',{});
// 			var listItem = $('<li>',{class:'sub-menu',text:this.model.get('name')});

// 			listItem.append(link);
// 				//this.model.get('name') + this.model.get('children').length + '</li>';
// 			return listItem;
// 		}


// 	});

// });