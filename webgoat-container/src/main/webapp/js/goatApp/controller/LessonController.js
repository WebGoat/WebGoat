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
	'goatApp/view/TitleView'
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
		TitleView
	) {
		'use strict'
		
		
		var Controller = function(options) {
			this.lessonContent = new LessonContentModel();
			this.lessonView = options.lessonView;

			_.extend(Controller.prototype,Backbone.Events);

			this.start = function() {
				this.listenTo(this.lessonContent,'content:loaded',this.onContentLoaded);
				this.userAndInfoView = new UserAndInfoView();
				this.menuButtonView = new MenuButtonView();
			};

			this.loadLesson = function(scr,menu,stage) {
				this.titleView = new TitleView();
				this.helpsLoaded = {};
				this.lessonContent.loadData({
					'screen': scr,
					'menu': menu,
					'stage': stage
				});
				this.planView = {};
				this.solutionView = {};
				this.sourceView = {};
				this.lessonHintView = {};
				this.screen = scr;
				this.menu = menu;
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
				this.listenTo(this.lessonInfoModel,'info:loaded',this.onInfoLoaded); //TODO onInfoLoaded function to handle title view and helpview

				if (loadHelps) {
					this.helpControlsView = null;
					this.lessonView.model = this.lessonContent;
					this.lessonView.render();
					//load title view (initially hidden) << //TODO: currently handled via menu click but need to be able to handle via routed request
					this.planView = new PlanView();
					this.solutionView = new SolutionView();
					this.sourceView = new SourceView();
					this.lessonHintView = new HintView();
					this.cookieView = new CookieView();
					// parameter model & view
					//TODO: instantiate model with values (not sure why was not working before)
					var paramModel = new ParamModel({});
					paramModel.set('screenParam',this.lessonContent.get('screenParam'));
					paramModel.set('menuParam',this.lessonContent.get('menuParam'));
					paramModel.set('stageParam',this.lessonContent.get('stageParam'));
					this.paramView = new ParamView({model:paramModel});

					$('.lesson-help').hide();
					}
				this.trigger('menu:reload');
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
				$.ajax({
					url:'service/restartlesson.mvc',
					method:'GET'
				}).then(function() {
					self.loadLesson(self.screen,self.menu);
				});
			};

		};
		return Controller;
});