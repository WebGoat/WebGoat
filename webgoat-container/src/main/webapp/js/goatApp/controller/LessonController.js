define(['jquery',
    'underscore',
    'libs/backbone',
    'goatApp/model/LessonContentModel',
    'goatApp/view/LessonContentView',
    'goatApp/view/PlanView',
    'goatApp/view/SourceView',
    'goatApp/view/SolutionView',
    'goatApp/view/HintView',
    'goatApp/view/HelpControlsView',
    'goatApp/view/CookieView',
    'goatApp/view/ParamView',
    'goatApp/model/ParamModel',
    'goatApp/support/GoatUtils',
    'goatApp/view/UserAndInfoView',
    'goatApp/view/MenuButtonView',
    'goatApp/model/LessonInfoModel',
    'goatApp/view/TitleView',
    'goatApp/model/LessonProgressModel',
    'goatApp/view/LessonProgressView'
    ], 
    function($,
        _,
        Backbone,
        LessonContentModel,
        LessonContentView,
        PlanView,
        SourceView,
        SolutionView,
        HintView,
        HelpControlsView,
        CookieView,
        ParamView,
        ParamModel,
        GoatUtils,
        UserAndInfoView,
        MenuButtonView,
        LessonInfoModel,
        TitleView,
        LessonProgressModel,
        LessonProgressView

    ) {
        'use strict'
        
        
        var Controller = function(options) {
            this.lessonContent = new LessonContentModel();
            this.lessonProgressModel = new LessonProgressModel();
            this.lessonProgressView = new LessonProgressView(this.lessonProgressModel);
            this.lessonView = options.lessonView;

            _.extend(Controller.prototype,Backbone.Events);

            this.start = function() {
                this.listenTo(this.lessonContent,'content:loaded',this.onContentLoaded);
                this.userAndInfoView = new UserAndInfoView();
                this.menuButtonView = new MenuButtonView();
            };

            this.loadLesson = function(scr,menu,stage,num) {
                this.titleView = new TitleView();
                this.helpsLoaded = {};
                if (typeof(scr) == "undefined") {
                    scr = null;
                }
                if (typeof(menu) == "undefined") {
                    menu = null;
                }
                if (typeof(stage) == "undefined") {
                    stage = null;
                }
                if (typeof(num) == "undefined") {
                    num = null;
                }
                this.lessonContent.loadData({
                    'scr': scr,
                    'menu': menu,
                    'stage': stage,
                    'num': num,
                });
                this.planView = {};
                this.solutionView = {};
                this.sourceView = {};
                this.lessonHintView = {};
                this.scr = scr;
                this.menu = menu;
                this.stage = stage;
                this.num = num;
                console.log("Lesson loading initiated")
            };

            this.onInfoLoaded = function() {
                this.helpControlsView = new HelpControlsView({
                    hasPlan:this.lessonInfoModel.get('hasPlan'),
                    hasSolution:this.lessonInfoModel.get('hasSolution'),
                    hasSource:this.lessonInfoModel.get('hasSource'),
                    hasHints:(this.lessonInfoModel.get('numberHints') > 0),
                });

                this.listenTo(this.helpControlsView,'plan:show',this.hideShowHelps);
                this.listenTo(this.helpControlsView,'solution:show',this.hideShowHelps);    
                this.listenTo(this.helpControlsView,'hints:show',this.onShowHints)
                this.listenTo(this.helpControlsView,'source:show',this.hideShowHelps);
                this.listenTo(this.helpControlsView,'lesson:restart',this.restartLesson);

                this.helpControlsView.render();

                this.titleView.render(this.lessonInfoModel.get('lessonTitle'));
            };

            this.onContentLoaded = function(loadHelps) {
                this.lessonInfoModel = new LessonInfoModel();
                this.listenTo(this.lessonInfoModel,'info:loaded',this.onInfoLoaded);

                if (loadHelps) {
                    this.helpControlsView = null;
                    this.lessonView.model = this.lessonContent;
                    this.lessonView.render();
                    
                    this.planView = new PlanView();
                    this.solutionView = new SolutionView();
                    this.sourceView = new SourceView();
                    this.lessonHintView = new HintView();
                    this.cookieView = new CookieView();

                    //TODO: instantiate model with values (not sure why was not working before)
                    var paramModel = new ParamModel({});
                    paramModel.set('scrParam',this.lessonContent.get('scrParam'));
                    paramModel.set('menuParam',this.lessonContent.get('menuParam'));
                    paramModel.set('stageParam',this.lessonContent.get('stageParam'));
                    paramModel.set('numParam',this.lessonContent.get('numParam'));
                    this.paramView = new ParamView({model:paramModel});

                    $('.lesson-help').hide();
                }
                this.trigger('menu:reload');
                this.lessonProgressModel.completed();
            };

            this.addCurHelpState = function (curHelp) {
                this.helpsLoaded[curHelp.helpElement] = curHelp.value;
            };

            this.hideShowHelps = function(showHelp) {
                var showId = '#lesson-' + showHelp + '-row';
                var contentId = '#lesson-' + showHelp + '-content';
                $('.lesson-help').not(showId).hide();
                if (!showId) { 
                    return;
                }

                if ($(showId).is(':visible')) {
                    $(showId).hide();
                    return;
                } else {
                    //TODO: move individual .html operations into individual help views
                    switch(showHelp) {
                        case 'plan':
                            $(contentId).html(this.planView.model.get('content'));
                            break;
                        case 'solution':
                            $(showId).html(this.solutionView.model.get('content'));
                            break;
                        case 'source':
                            $(contentId).html('<pre>' + this.sourceView.model.get('content') + '</pre>');
                            break;
                    }
                    $(showId).show();
                    GoatUtils.scrollToHelp()
                }
            };

            this.onShowHints = function() {
                this.lessonHintView.render();
            };

            this.restartLesson = function() {
                var self=this;
                var fragment = "attack/" + self.scr + "/" + self.menu;
                console.log("Navigating to " + fragment);
                // Avoiding the trigger event - handle - navigate loop by
                // loading the lesson explicitly (after executing the restart
                // servlet).
                goatRouter.navigate(fragment);
                // Resetting the user's lesson state (assuming a single browser
                // and session per user).
                $.ajax({
                    url:'service/restartlesson.mvc',
                    method:'GET'
                }).done(function() {
                    //Log shows warning, see https://bugzilla.mozilla.org/show_bug.cgi?id=884693

                    // Explicitly loading the lesson instead of triggering an
                    // event in goatRouter.navigate().
                    self.loadLesson(self.scr,self.menu);
                });
            };

        };
        return Controller;
});
