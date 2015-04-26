define(['jquery',
	'underscore',
	'backbone',
	'goatApp/controller/LessonController',
	'goatApp/controller/MenuController',
	'goatApp/view/LessonContentView',
	'goatApp/view/MenuView'
	], function ($,_,Backbone,LessonController,MenuController,LessonView,MenuView) {

		var lessonView = new LessonContentView();
		var menuView = new MenuView(); 
		var GoatAppRouter = Backbone.Router.extend({
			routes: {
				//#....
				'welcome':'welcomeRoute',
				'attack/:scr/:menu':'attackRoute' //	
			},
			lessoonController: lessoonController({
				lessonView:lessonView
			}),
			menuView: new MenuController({
				menuView:menuView
			});
		});

		var init = function() {
			goatRouter =  new GoatAppRouter();

			goatRouter.on('route:attackRoute', function(scr,menu) {
				this.lessonController.loadLesson(scr,menu);
				//update menu
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