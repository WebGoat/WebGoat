define(['jquery',
    'libs/jquery-vuln',
    'jqueryui',
    'underscore',
    'backbone',
    'goatApp/controller/LessonController',
    'goatApp/controller/MenuController',
    'goatApp/view/LessonContentView',
    'goatApp/view/MenuView',
    'goatApp/view/DeveloperControlsView',
    'goatApp/view/TitleView'
], function ($,
             $vuln,
             jqueryui,
             _,
             Backbone,
             LessonController,
             MenuController,
             LessonContentView,
             MenuView,
             DeveloperControlsView,
             TitleView) {

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

        lessonController: null,
        menuController : null,
        titleView: null,

        setUpCustomJS: function () {
            webgoat.customjs.jquery = $; //passing jquery into custom js scope ... still klunky, but works for now
            webgoat.customjs.jqueryVuln = $vuln;

            // shim to support xss lesson
            webgoat.customjs.phoneHome = function (e) {
                console.log('phoneHome invoked');
                webgoat.customjs.jquery.ajax({
                    method: "POST",
                    url: "/WebGoat/CrossSiteScripting/phone-home-xss",
                    data: {param1: 42, param2: 24},
                    headers: {
                        "webgoat-requested-by": "dom-xss-vuln"
                    },
                    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                    success: function (data) {
                        //devs leave stuff like this in all the time
                        console.log('phone home said '  + JSON.stringify(data));
                    }
                });
            }

        },

        initialize: function () {
            this.menuController = new MenuController({menuView: new MenuView()});
            this.titleView = new TitleView();
            this.lessonController = new LessonController({lessonContentView: new LessonContentView(), titleView: this.titleView}),
            this.lessonController.start();
            webgoat = {};
            webgoat.customjs = {};

            this.setUpCustomJS();
            Backbone.history.start();
            this.listenTo(this.lessonController, 'menu:reload', this.reloadMenu)
        },

        lessonRoute: function(name) {
            render();
            this.lessonController.loadLesson(name, 0);
            this.menuController.updateMenu(name);
        },

        lessonPageRoute: function (name, pageNum) {
            render();
            pageNum = (_.isNumber(parseInt(pageNum))) ? parseInt(pageNum) : 0;
            this.lessonController.loadLesson(name, pageNum);
            this.menuController.updateMenu(name);
        },

        testRoute: function (param) {
            this.lessonController.testHandler(param);
            //this.menuController.updateMenu(name);
        },

        welcomeRoute: function () {
            render();
            this.lessonController.loadWelcome();
        },

        reloadMenu: function (curLesson) {
            this.menuController.updateMenu();
        },

        reportCard : function () {
            var self = this;
            require(['goatApp/view/ReportCardView'], function (ReportCardView) {
                self.titleView.render('Report card');
                render(new ReportCardView());
            });
        },
    });

    return GoatAppRouter;

});
