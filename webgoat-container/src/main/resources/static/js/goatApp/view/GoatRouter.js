define(['jquery',
    'underscore',
    'backbone',
    'goatApp/controller/LessonController',
    'goatApp/controller/MenuController',
    'goatApp/view/LessonContentView',
    'goatApp/view/MenuView',
    'goatApp/view/DeveloperControlsView',
    'goatApp/view/TitleView'
], function ($,
             _,
             Backbone,
             LessonController,
             MenuController,
             LessonContentView,
             MenuView,
             DeveloperControlsView,
             TitleView) {

    var lessonContentView = new LessonContentView();
    var menuView = new MenuView();
    var developerControlsView = new DeveloperControlsView();
    var titleView = new TitleView();

    function getContentElement() {
        return $('#main-content');
    };

    function render(view) {
        $('div.pages').hide();
        //TODO this works for now because we only have one page we should rewrite this a bit
        if (view != null) {
            $('#report-card-page').show();
        } else {
            $('#lesson-title').show();
            $('#lesson-page').show();
        }
    };

    var GoatAppRouter = Backbone.Router.extend({
        routes: {
            'welcome': 'welcomeRoute',
            'lesson/:name': 'lessonRoute',
            'lesson/:name/:pageNum': 'lessonPageRoute',
            'test/:param': 'testRoute',
            'reportCard': 'reportCard'
        },

        lessonController: new LessonController({
            lessonContentView: lessonContentView,
            titleView: titleView
        }),

        menuController: new MenuController({
            menuView: menuView
        }),

        setUpCustomJS: function () {
            webgoat.customjs.jquery = $; //passing jquery into custom js scope ... still klunky, but works for now

            // temporary shim to support dom-xss lesson
            webgoat.customjs.phoneHome = function (e) {
                console.log('phoneHome invoked');
                console.log(arguments.callee);
                //
                webgoat.customjs.jquery.ajax({
                    method: "POST",
                    url: "/WebGoat/CrossSiteScripting/dom-xss",
                    data: {param1: 42, param2: 24},
                    headers: {
                        "webgoat-requested-by": "dom-xss-vuln"
                    },
                    contentType: 'application/x-www-form-urlencoded; charset=UTF-8'
                });
            }
        },

        init: function () {
            goatRouter = new GoatAppRouter();
            this.lessonController.start();
            // this.menuController.initMenu();
            webgoat = {};
            webgoat.customjs = {};

            this.setUpCustomJS();

            goatRouter.on('route:lessonRoute', function (name) {
                render();
                this.lessonController.loadLesson(name, 0);
                //TODO - update menu code from below
                this.menuController.updateMenu(name);
            });

            goatRouter.on('route:lessonPageRoute', function (name, pageNum) {
                render();
                pageNum = (_.isNumber(parseInt(pageNum))) ? parseInt(pageNum) : 0;
                this.lessonController.loadLesson(name, pageNum);
                //TODO - update menu code from below
                this.menuController.updateMenu(name);
            });

            goatRouter.on('route:welcomeRoute', function () {
                render();
                this.lessonController.loadWelcome();
            });

            goatRouter.on('route:testRoute', function (param) {
                render();
                this.lessonController.testHandler(param);
            });

            goatRouter.on("route", function (route, params) {
            });

            Backbone.history.start();
            this.listenTo(this.lessonController, 'menu:reload', this.reloadMenu)
        },

        reloadMenu: function (curLesson) {
            this.menuController.updateMenu();
        },

        reportCard : function () {
            require(['goatApp/view/ReportCardView'], function (ReportCardView) {
                titleView.render('Report card');
                render(new ReportCardView());
            });
        },
    });

    return GoatAppRouter;

});
