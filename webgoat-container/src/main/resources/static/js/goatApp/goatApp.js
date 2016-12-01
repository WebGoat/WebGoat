define(['jquery','underscore','backbone','goatApp/view/GoatRouter', 'goatApp/support/goatAsyncErrorHandler'],
	function($,_,Backbone,Router, asyncErrorHandler){
		'use strict'
		//var goatRouter = new Router();
		return {
			initApp: function() {
				asyncErrorHandler.init();
				//TODO: add query/ability to load from where they left off 
				var goatRouter = new Router();
				goatRouter.init();
			}
		};
});