define(['jquery',
	'underscore',
	'backbone',
	'goatApp/view/MenuView'
	], 
	function($,
		_,
		Backbone,
		MenuView) {
		 Controller = function(options){
		 	_.extend(Controller.prototype,Backbone.Events);
			options = options || {};
			this.menuView = options.menuView;
			this.titleView = options.titleView;

		 	// this.initMenu = function() {
		 	// 	this.listenTo(this.menuView,'lesson:click',this.renderTitle);
		 	// }

		 	this.updateMenu = function(){
		 		this.menuView.updateMenu();
		 	},

		 	//TODO: move title rendering into lessonContent/View pipeline once data can support it
		 	this.renderTitle = function(title) {
		 		this.titleView.render(title);
		 	}


		 };

		 return Controller;
});