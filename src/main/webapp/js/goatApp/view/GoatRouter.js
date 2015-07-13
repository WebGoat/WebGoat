define(['jquery',
	'underscore',
	'backbone',
	'goatApp/controller/LessonController',
	'goatApp/controller/MenuController',
	'goatApp/view/LessonContentView',
	'goatApp/view/MenuView',
	'goatApp/view/TitleView'
	], function ($,_,Backbone,LessonController,MenuController,LessonContentView,MenuView,TitleView) {

		var lessonView = new LessonContentView();
		var menuView = new MenuView(); 
		var titleView = new TitleView();

		var GoatAppRouter = Backbone.Router.extend({
			routes: {
				//#....
				'welcome':'welcomeRoute',
				'attack/:scr/:menu':'attackRoute' //	
		 	},
			lessonController: new LessonController({
				lessonView:lessonView
			}),
			menuController: new MenuController({
				menuView:menuView,
				titleView:titleView
			}),

			init:function() {
				goatRouter =  new GoatAppRouter();
				this.lessonController.start();
				this.menuController.initMenu();

				goatRouter.on('route:attackRoute', function(scr,menu) {
					console.log('attack route');
					this.lessonController.loadLesson(scr,menu);
					this.menuController.updateMenu(scr,menu);
					//update menu
				});
				goatRouter.on('route:welcomeRoute', function() {
					alert('welcome route');
				});
				
				Backbone.history.start();
			}
		});

		return GoatAppRouter;

});