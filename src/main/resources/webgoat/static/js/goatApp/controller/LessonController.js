define(['jquery',
    'underscore',
    'backbone',
    'goatApp/model/LessonContentModel',
    'goatApp/view/LessonContentView',
    'goatApp/view/HintView',
    'goatApp/view/HelpControlsView',
    'goatApp/support/GoatUtils',
    'goatApp/view/UserAndInfoView',
    'goatApp/view/MenuButtonView',
    'goatApp/model/LessonInfoModel'
    ],
    function($,
        _,
        Backbone,
        LessonContentModel,
        LessonContentView,
        HintView,
        HelpControlsView,
        GoatUtils,
        UserAndInfoView,
        MenuButtonView,
        LessonInfoModel
    ) {
        'use strict'

        var Controller = function(options) {
            this.lessonContent = new LessonContentModel();
            this.lessonContentView = options.lessonContentView;
            this.titleView = options.titleView;

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
                this.helpControlsView = new HelpControlsView();
                this.listenTo(this.helpControlsView,'hints:show',this.showHintsView);
                this.listenTo(this.helpControlsView,'lesson:restart',this.restartLesson);
                this.helpControlsView.render();

                this.showHintsView();
                this.titleView.render(this.lessonInfoModel.get('lessonTitle'));
            };

            this.updateMenu = function() {
                this.trigger('menu:reload')
            };

            this.onContentLoaded = function(loadHelps) {
                this.lessonInfoModel = new LessonInfoModel({'lesson':loadHelps['urlRoot']});

                this.listenTo(this.lessonInfoModel,'info:loaded',this.onInfoLoaded);

                if (loadHelps) {
                    this.helpControlsView = null;
                    this.lessonContentView.model = this.lessonContent;
                    this.lessonContentView.render();
                    //TODO: consider moving hintView as child of lessonContentView ...
                    this.createLessonHintView();

                    $('.lesson-help').hide();
                }
                //this.trigger('menu:reload');
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
                var self=this;
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
                    url: 'service/restartlesson.mvc/' + encodeURIComponent(self.name),
                    method:'GET'
                }).done(function(lessonLink) {
                    self.loadLesson(self.name);
                    self.updateMenu();
                    self.callPaginationUpdate();
                    self.lessonContentView.resetLesson();
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
