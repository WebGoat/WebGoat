define(['jquery','underscore','backbone','goatApp/view/GoatRouter'],
	function($,_,Backbone,Router){
		'use strict'
		//var goatRouter = new Router();
		return {
			initApp: function() {
				//TODO: add query/ability to load from where they left off 
				var goatRouter = new Router();
				goatRouter.init();
			}
		};
});