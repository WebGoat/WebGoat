define(['jquery',
	'underscore',
	'backbone',
	'goatApp/controller/LessonController'
	], function ($,_,Backbone,LessonController) {
	var GoatAppRouter = Backbone.Router.extend({
		routes: {
			//#....
			'welcome':'welcomeRoute',
			'attack/:scr/:menu':'attackRoute' //	
		},
		lessonController: new LessonController()
	});



	var init = function() {
		goatRouter =  new GoatAppRouter();

		goatRouter.on('route:attackRoute', function(scr,menu) {
			this.lessonController.loadLesson(scr,menu);
		});
		goatRouter.on('route:welcomeRoute', function() {
			alert('welcome route');
		});
		// init the history/router
		Backbone.history.start();
	}

	return {
		init:init
	};

});