define(['jquery',
	'underscore',
	'backbone'],
	function($,_,Backbone) {

	return Backbone.Model.extend({

	});

});

	// accordionMenu = function(id) {
 //        if ($('ul#' + id).attr('isOpen') == 0) {
 //            $scope.expandMe = true;
 //        } else {
 //            $('ul#' + id).slideUp(300).attr('isOpen', 0);u
 //            return;
 //        }
 //        $scope.openMenu = id;
 //        $('.lessonsAndStages').not('ul#' + id).slideUp(300).attr('isOpen', 0);
 //        if ($scope.expandMe) {
 //            $('ul#' + id).slideDown(300).attr('isOpen', 1);
 //        }
 //    }


		//urlRoot:'service/lessonmenu.mvc',
	// 	defaults: {
	// 		items:null,
	// 		selectedItem:null
	// 	},
	// 	initialize: function () {
	// 		var self = this;
	// 		this.fetch().then(function(menuItems){
	// 			menuItems = goatUtils.enhanceMenuData(menuItems,this.selectedItem);
	// 			this.setDataItems(menuItems);
	// 		});
	// 	},
	
	// 	update: function() {
	// 		var self = this;
	// 		this.fetch().then(function(menuItems) {
	// 			menuItems = goatUtils.enhanceMenuData(menuItems,this.selectedItem);
	// 			self.setDataItems(menuItems);
	// 		});
	// 	},

	// 	setDataItems: function (data) {
	// 		this.items = data;
	// 	}
	// });
