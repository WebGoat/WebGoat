define(['jquery',
    'underscore',
    'backbone',
    'goatApp/controller/LessonController',
    'goatApp/controller/MenuController',
    'goatApp/view/LessonContentView',
    'goatApp/view/MenuView',
    'goatApp/view/DeveloperControlsView'
    ], function ($,
    _,
    Backbone,
    LessonController,
    MenuController,
    LessonContentView,
    MenuView,
    DeveloperControlsView) {
    
    var lessonContentView = new LessonContentView();
    var menuView = new MenuView();
    var developerControlsView = new DeveloperControlsView();

    var GoatAppRouter = Backbone.Router.extend({
        routes: {
            'welcome':'welcomeRoute',
            'lesson/:name':'lessonRoute',
            'lesson/:name/:pageNum':'lessonPageRoute',
            'test/:param':'testRoute'
        },


        lessonController: new LessonController({
            lessonContentView: lessonContentView
        }),

        menuController: new MenuController({
            menuView: menuView
        }),

        init:function() {
            goatRouter =  new GoatAppRouter();
            this.lessonController.start();
            // this.menuController.initMenu();
            webgoat = {};
            webgoat.customjs = {};
            webgoat.customjs.jquery = $; //passing jquery into custom js scope ... still klunky, but works for now

            goatRouter.on('route:lessonRoute', function(name) {
                this.lessonController.loadLesson(name,0);
                //TODO - update menu code from below
                this.menuController.updateMenu(name);
            });

            goatRouter.on('route:lessonPageRoute', function(name,pageNum) {
                pageNum = (_.isNumber(parseInt(pageNum))) ? parseInt(pageNum) : 0;
                this.lessonController.loadLesson(name,pageNum);
                //TODO - update menu code from below
                this.menuController.updateMenu(name);
            });

            goatRouter.on('route:welcomeRoute', function() {
                this.lessonController.loadWelcome();
            });

            goatRouter.on('route:welcomeRoute', function() {
                this.lessonController.loadWelcome();
            });

            goatRouter.on('route:testRoute', function(param) {
                this.lessonController.testHandler(param);
            });

            goatRouter.on("route", function(route, params) {});

            Backbone.history.start();
            this.listenTo(this.lessonController, 'menu:reload',this.reloadMenu)
        },

        reloadMenu: function (curLesson) {
            this.menuController.updateMenu();
        }


    });

    return GoatAppRouter;

});
