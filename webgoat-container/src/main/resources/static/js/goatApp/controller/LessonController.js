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
    'goatApp/view/DeveloperControlsView',
    'goatApp/support/GoatUtils',
    'goatApp/view/UserAndInfoView',
    'goatApp/view/MenuButtonView',
    'goatApp/model/LessonInfoModel',
    'goatApp/view/TitleView',
    'goatApp/model/LessonProgressModel',
    'goatApp/view/LessonProgressView',
    'goatApp/view/LessonOverviewView',
    'goatApp/model/LessonOverviewModel'
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
        DeveloperControlsView,
        GoatUtils,
        UserAndInfoView,
        MenuButtonView,
        LessonInfoModel,
        TitleView,
        LessonProgressModel,
        LessonProgressView,
        LessonOverviewView,
        LessonOverviewModel
    ) {
        'use strict'

        var Controller = function(options) {
            this.lessonContent = new LessonContentModel();
            this.lessonProgressModel = new LessonProgressModel();
            this.lessonProgressView = new LessonProgressView(this.lessonProgressModel);
            this.lessonOverviewModel = new LessonOverviewModel();
            this.lessonOverview = new LessonOverviewView(this.lessonOverviewModel);
            this.lessonContentView = options.lessonContentView;
            this.developerControlsView = new DeveloperControlsView();


            _.extend(Controller.prototype,Backbone.Events);

            this.start = function() {
                this.listenTo(this.lessonContent,'content:loaded',this.onContentLoaded);
                this.userAndInfoView = new UserAndInfoView();
                this.menuButtonView = new MenuButtonView();
            };

            this.loadLesson = function(name,pageNum) {
                if (this.name === name) {
                    this.lessonContentView.navToPage(pageNum)
                    return;
                }

                this.titleView = new TitleView();
                this.helpsLoaded = {};
                if (typeof(name) === 'undefined' || name === null) {
                    //TODO: implement lesson not found or return to welcome page?
                }
                this.lessonContent.loadData({
                    'name':name
                });
                this.planView = {};
                this.solutionView = {};
                this.sourceView = {};
                this.lessonHintView = {};
                this.name = name;
            };

            this.onInfoLoaded = function() {
                this.helpControlsView = new HelpControlsView({
                    hasPlan:this.lessonInfoModel.get('hasPlan'),
                    hasSolution:this.lessonInfoModel.get('hasSolution'),
                    hasSource:this.lessonInfoModel.get('hasSource'),
                    hasHints:(this.lessonInfoModel.get('numberHints') > 0)
                    //hasAttack:this.lessonInfo.get('hasAttack') // TODO: add attack options
                });

                this.listenTo(this.helpControlsView,'hints:show',this.showHints);
                this.listenTo(this.helpControlsView,'lessonOverview:show',this.showLessonOverview)
                this.listenTo(this.helpControlsView,'attack:show',this.hideShowAttack);
                this.listenTo(this.helpControlsView,'solution:show',this.hideShowHelps);
                this.listenTo(this.helpControlsView,'source:show',this.hideShowHelps);
                this.listenTo(this.helpControlsView,'lesson:restart',this.restartLesson);
                this.listenTo(this.developerControlsView, 'dev:labels', this.restartLesson);
                this.listenTo(this.lessonContentView, 'lesson:complete', this.updateMenu)
                this.listenTo(this.lessonContentView, 'lesson:complete', this.updateLessonOverview)
                this.listenTo(this,'hints:show',this.onShowHints);

                this.helpControlsView.render();
                this.lessonOverviewModel.fetch();

                this.titleView.render(this.lessonInfoModel.get('lessonTitle'));
            };

            this.updateMenu = function() {
                this.trigger('menu:reload')
            };

            this.updateLessonOverview = function() {
                this.lessonOverviewModel.fetch();
            }

            this.onContentLoaded = function(loadHelps) {
                this.lessonInfoModel = new LessonInfoModel();
                this.listenTo(this.lessonInfoModel,'info:loaded',this.onInfoLoaded);

                if (loadHelps) {
                    this.helpControlsView = null;
                    this.lessonContentView.model = this.lessonContent;
                    this.lessonContentView.render();
                    
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
                //this.trigger('menu:reload');
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

            this.showHints = function() {
                this.lessonHintView.render();
                //this.lessonHintView.
            };

            this.showLessonOverview = function() {
                this.lessonOverview.render();
            };

            this.hideShowAttack = function (options) { // will likely expand this to encompass
                if (options.show) {
                    $('#attack-container').show();
                    $('#attack-container div.modal-header button.close, #about-modal div.modal-footer button').unbind('click').on('click', function() {
                        $('#attack-container').hide(200);
                    });
                    if (this.lessonInfoModel.get('numberHints') > 0) {

                        this.lessonContentView.$el.find('#show-hints-button').unbind().on('click',_.bind(this.showHints,this)).show();
                    }
                }
            };

            this.restartLesson = function() {
                var self=this;
                $.ajax({
                    url:'service/restartlesson.mvc',
                    method:'GET'
                }).done(function(lessonLink) {
                    self.loadLesson(self.name);
                });
            };

            this.testHandler = function(param) {
                console.log('test handler');
                this.lessonContentView.showTestParam(param);
            };

        };



        return Controller;
});
