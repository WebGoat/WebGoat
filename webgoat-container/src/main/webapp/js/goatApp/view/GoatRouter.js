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
            'attack/:scr/:menu(/:stage)':'attackRoute' //
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

            goatRouter.on('route:attackRoute', function(scr,menu,stage) {
                this.lessonController.loadLesson(scr,menu,stage);
                this.menuController.updateMenu(scr,menu);
                //update menu
            });
            goatRouter.on('route:welcomeRoute', function() {
                alert('welcome route');
            });

            Backbone.history.start();
            this.listenTo(this.menuController, 'menu:reload',this.reloadMenu)
        },

        reloadMenu: function (curLesson) {
            this.menuController.updateMenu(curLesson);
        }


    });

    return GoatAppRouter;

});