define(['jquery','underscore','backbone','goatApp/view/MenuView'], 
	function($,_,Backbone,MenuView) {
		 Controller = function(options){
			options = options || {};
			this.menuView = options.menuView;
		 	this.initMenu = function() {
		 		console.debug('initing menu');
		 	}

		 	this.updateMenu = function() {
		 		
		 	}

		 };

		 return Controller;
});