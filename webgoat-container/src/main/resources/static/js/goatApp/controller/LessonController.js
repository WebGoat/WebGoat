define(['jquery',
    'underscore',
    'libs/backbone',
    'goatApp/model/LessonContentModel',
    'goatApp/view/LessonContentView',
//    'goatApp/view/PlanView',
//    'goatApp/view/SourceView',
//    'goatApp/view/SolutionView',
    'goatApp/view/HintView',
    'goatApp/view/HelpControlsView',
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
    'goatApp/view/LessonOverviewView'
    ],
    function($,
        _,
        Backbone,
        LessonContentModel,
        LessonContentView,
        HintView,
        HelpControlsView,
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
        LessonOverviewView
    ) {
        'use strict'

        var Controller = function(options) {
            this.lessonContent = new LessonContentModel();
            this.lessonProgressModel = new LessonProgressModel();
            this.lessonProgressView = new LessonProgressView(this.lessonProgressModel);
            this.lessonContentView = options.lessonContentView;
            this.titleView = options.titleView;
            this.developerControlsView = new DeveloperControlsView();

            _.extend(Controller.prototype,Backbone.Events);

            this.start = function() {
                this.listenTo(this.lessonContent,'content:loaded',this.onContentLoaded);
                this.userAndInfoView = new UserAndInfoView();
                this.menuButtonView = new MenuButtonView();
                this.listenTo(this.lessonContentView, 'assignment:complete', this.updateMenu);
                this.listenTo(this.lessonContentView, 'endpoints:filtered', this.filterPageHints);
            };

            this.filterPageHints = function(endpoints) {
                //filter hints for page by
                this.lessonHintView.filterHints(endpoints);
            }

            this.onHideHintsButton = function() {
                this.helpControlsView.hideHintsButton();
            }

            this.onShowHintsButton = function() {
                this.helpControlsView.showHintsButton();
            }

            this.loadLesson = function(name,pageNum) {

                if (this.name === name) {
                    this.listenToOnce(this.lessonHintView, 'hints:showButton', this.onShowHintsButton);
                    this.listenTo(this.lessonHintView, 'hints:hideButton', this.onHideHintsButton);
                    this.lessonContentView.navToPage(pageNum);
                    this.lessonHintView.hideHints();
                    this.lessonHintView.showFirstHint();
                    //this.lessonHintView.selectHints();
                    this.titleView.render(this.lessonInfoModel.get('lessonTitle'));
                    return;
                }

                if (pageNum && !this.name) {
                    //placeholder
                }

                this.helpsLoaded = {};
                if (typeof(name) === 'undefined' || name === null) {
                    //TODO: implement lesson not found or return to welcome page?
                }
                this.lessonContent.loadData({'name':name});
                this.name = name;
            };

            this.onInfoLoaded = function() {
                this.helpControlsView = new HelpControlsView({
                    hasPlan:this.lessonInfoModel.get('hasPlan'),
                    hasSolution:this.lessonInfoModel.get('hasSolution'),
                    hasSource:this.lessonInfoModel.get('hasSource')
                });

                this.listenTo(this.helpControlsView,'hints:show',this.showHintsView);

                this.listenTo(this.helpControlsView,'lesson:restart',this.restartLesson);
                this.listenTo(this.developerControlsView, 'dev:labels', this.restartLesson);

                this.helpControlsView.render();
                this.showHintsView();
                this.titleView.render(this.lessonInfoModel.get('lessonTitle'));
            };

            this.updateMenu = function() {
                this.trigger('menu:reload')
            };

            this.onContentLoaded = function(loadHelps) {
                this.lessonInfoModel = new LessonInfoModel();
                this.listenTo(this.lessonInfoModel,'info:loaded',this.onInfoLoaded);

                if (loadHelps) {
                    this.helpControlsView = null;
                    this.lessonContentView.model = this.lessonContent;
                    this.lessonContentView.render();
                    //TODO: consider moving hintView as child of lessonContentView ...
                    this.createLessonHintView();

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

            this.createLessonHintView = function () {
                if (this.lessonHintView) {
                    this.lessonHintView.stopListening();
                    this.lessonHintView = null;
                }
                this.lessonHintView = new HintView();
            }

            this.addCurHelpState = function (curHelp) {
                this.helpsLoaded[curHelp.helpElement] = curHelp.value;
            };

            this.showHintsView = function() {
                if (!this.lessonHintView) {
                    this.createLessonHintView();
                }
                //
                this.lessonHintView.render();
                if (this.lessonHintView.getHintsCount() > 0) {
                    this.helpControlsView.showHintsButton();
                } else {
                    this.helpControlsView.hideHintsButton();
                }
            };

            this.restartLesson = function() {
                var self=this;
                $.ajax({
                    url:'service/restartlesson.mvc',
                    method:'GET'
                }).done(function(lessonLink) {
                    self.loadLesson(self.name);
                    self.updateMenu();
                    self.callPaginationUpdate();
                });
            };

            this.testHandler = function(param) {
                console.log('test handler');
                this.lessonContentView.showTestParam(param);
            };

            this.callPaginationUpdate = function () {
                this.lessonContentView.updatePagination();
            }

        };




        return Controller;
});
