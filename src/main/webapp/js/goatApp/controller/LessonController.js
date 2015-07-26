define(['jquery',
	'underscore',
	'libs/backbone',
	'goatApp/model/LessonContentData',
	'goatApp/view/LessonContentView',
	'goatApp/view/PlanView',
	'goatApp/view/SourceView',
	'goatApp/view/SolutionView',
	'goatApp/view/LessonHintView',
	'goatApp/view/HelpControlsView',
	'goatApp/support/GoatUtils'
	], 
	function($,
		_,
		Backbone,
		LessonContentData,
		LessonContentView,
		PlanView,
		SourceView,
		SolutionView,
		LessonHintView,
		HelpControlsView,
		GoatUtils
	) {
		'use strict'
		
		
		var Controller = function(options) {
			this.lessonContent = new LessonContentData();
			this.lessonView = options.lessonView;

			/*this.planView = new PlanView();
			this.solutionView = new SolutionView();
			this.sourceView = new SourceView();
			*/

			_.extend(Controller.prototype,Backbone.Events);
			this.start = function() {
				this.listenTo(this.lessonContent,'contentLoaded',this.onContentLoaded);
			};

			//load View, which can pull data
			this.loadLesson = function(scr,menu) {
				this.helpsLoaded = {};
				this.lessonContent.loadData({
					'screen': encodeURIComponent(scr),
					'menu': encodeURIComponent(menu),
				});
				this.planView = {};
				this.solutionView = {};
				this.sourceView = {};
				this.lessonHintView = {};
				//
				
			};

			this.onContentLoaded = function() {
				//this.lessonView  = new LessonContentView({content:LessonContent.content});
				this.lessonView.model = this.lessonContent;
				this.lessonView.render();

				//load cookies/parameters view

				//load title view (initially hidden) << //TODO: currently handled via menu click but need to be able to handle via routed request
				//plan view (initially hidden)
				this.planView = new PlanView();
				this.listenTo(this.planView,'plan:loaded',this.areHelpsReady);
				//solution view (initially hidden)
				this.solutionView = new SolutionView();
				this.listenTo(this.solutionView,'solution:loaded',this.areHelpsReady);
				//source (initially hidden)
				this.sourceView = new SourceView();
				this.listenTo(this.sourceView,'source:loaded',this.areHelpsReady);
				//load help controls view (contextul to what helps are available)
				this.lessonHintView = new LessonHintView();
				this.listenTo(this.lessonHintView,'hints:loaded',this.areHelpsReady);
			};

			this.areHelpsReady = function (curHelp) {
				this.addCurHelpState(curHelp);
				// check if all are ready
				if (this.helpsLoaded['hints'] && this.helpsLoaded['plan'] && this.helpsLoaded['solution'] && this.helpsLoaded['source']) {
					//
					this.helpControlsView = new HelpControlsView({
						hasPlan:(this.planView.model.get('content') !== null),
						hasSolution:(this.solutionView.model.get('content') !== null),
						hasSource:(this.sourceView.model.get('content') !== null),
						hasHints:(this.lessonHintView.collection.length > 0),
					});
					this.helpControlsView.render();
					//
					this.listenTo(this.helpControlsView,'plan:show',this.hideShowHelps);
					this.listenTo(this.helpControlsView,'solution:show',this.hideShowHelps);	
					this.listenTo(this.helpControlsView,'hints:show',this.showHints)
					this.listenTo(this.helpControlsView,'source:show',this.hideShowHelps);
					this.listenTo(this.helpControlsView,'lesson:restart',this.restartLesson);
				}
			};

			this.addCurHelpState = function (curHelp) {
				this.helpsLoaded[curHelp.helpElement] = curHelp.value;
			};

			this.hideShowHelps = function(showHelp) {
				var showId = '#lesson-' + showHelp + '-row';
				$('.lesson-help').not(showId).hide();
				switch(showHelp) {
					case 'plan':
						$(showId).html(this.planView.model.get('content')).show();
						break;
					case 'solution':
						$(showId).html(this.solutionView.model.get('content')).show();
						break;
					case 'source':
						$(showId).html(this.sourceView.model.get('content')).show();
						break;
				}
				GoatUtils.scrollToHelp()
			};;

			this.showHints = function() {
				console.log('show Hints');
			};

			this.restartLesson = function() {
				console.log('restart lesson');
			};

		};
		return Controller;
});