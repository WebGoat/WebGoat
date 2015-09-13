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
	'goatApp/view/MenuButtonView'
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
		MenuButtonView
	) {
		'use strict'
		
		
		var Controller = function(options) {
			this.lessonContent = new LessonContentModel();
			this.lessonView = options.lessonView;

			_.extend(Controller.prototype,Backbone.Events);

			this.start = function() {
				this.listenTo(this.lessonContent,'contentLoaded',this.onContentLoaded);
				//'static' elements of page/app
				this.userAndInfoView = new UserAndInfoView();
				this.menuButtonView = new MenuButtonView();
			};
			//load View, which can pull data
			this.loadLesson = function(scr,menu,stage) {
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
				this.screen = scr; //needed anymore?
				this.menu = menu;
				//
				
			};

			this.onContentLoaded = function() {
				this.helpControlsView = null;
				this.lessonView.model = this.lessonContent;
				this.lessonView.render();
				//load title view (initially hidden) << //TODO: currently handled via menu click but need to be able to handle via routed request
				//plan view (initially hidden)
				this.planView = new PlanView();
				this.listenToOnce(this.planView,'plan:loaded',this.areHelpsReady);
				//solution view (initially hidden)
				this.solutionView = new SolutionView();
				this.listenToOnce(this.solutionView,'solution:loaded',this.areHelpsReady);
				//source (initially hidden)
				this.sourceView = new SourceView();
				this.listenToOnce(this.sourceView,'source:loaded',this.areHelpsReady);
				//load help controls view (contextul to what helps are available)
				this.lessonHintView = new HintView();
				this.listenToOnce(this.lessonHintView,'hints:loaded',this.areHelpsReady);
				//
				this.cookieView = new CookieView();
				// parameter model & view
				//TODO: instantiate model with values (not sure why was not working before)
				var paramModel = new ParamModel({
				});
				paramModel.set('screenParam',this.lessonContent.get('screenParam'));
				paramModel.set('menuParam',this.lessonContent.get('menuParam'));
				paramModel.set('stageParam',this.lessonContent.get('stageParam'));
				this.paramView = new ParamView({model:paramModel});

				$('.lesson-help').hide();
				this.trigger('menu:reload');
			};

			this.areHelpsReady = function (curHelp) {
				//TODO: significantly refactor (remove) this once LessonInfoService can be used to support lazy loading
				this.addCurHelpState(curHelp);
				// check if all are ready
				if (this.helpsLoaded['hints'] && this.helpsLoaded['plan'] && this.helpsLoaded['solution'] && this.helpsLoaded['source'] && !this.helpControlsView) {
					
					this.helpControlsView = new HelpControlsView({
						hasPlan:(this.planView.model.get('content') !== null),
						hasSolution:(this.solutionView.model.get('content') !== null),
						hasSource:(this.sourceView.model.get('content') !== null),
						hasHints:(this.lessonHintView.collection.length > 0),
					});
					this.helpControlsView.render();
					
					this.listenTo(this.helpControlsView,'plan:show',this.hideShowHelps);
					this.listenTo(this.helpControlsView,'solution:show',this.hideShowHelps);	
					this.listenTo(this.helpControlsView,'hints:show',this.onShowHints)
					this.listenTo(this.helpControlsView,'source:show',this.hideShowHelps);
					this.listenTo(this.helpControlsView,'lesson:restart',this.restartLesson);
				}
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