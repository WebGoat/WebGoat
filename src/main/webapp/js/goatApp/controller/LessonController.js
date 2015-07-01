define(['jquery',
	'underscore',
	'libs/backbone',
	'goatApp/model/LessonContentData',
	'goatApp/view/LessonContentView',
	'goatApp/view/PlanView',
	'goatApp/view/SourceView',
	'goatApp/view/SolutionView',
	'goatApp/view/LessonHintView'
	], 
	function($,
		_,
		Backbone,
		LessonContentData,
		LessonContentView,
		PlanView,
		SourceView,
		SolutionView,
		LessonHintView
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
			}

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

				//load title view (initially hidden) << currently handled via menu click but need to be able to handle via routed request
				//plan view (initially hidden)
				this.planView = new PlanView();
				//solution view (initially hidden)
				this.solutionView = new SolutionView();
				//source (initially hidden)
				this.sourceView = new SourceView();
				//load help controls view (contextul to what helps are available)
				this.lessonHintView = new LessonHintView();
				this.listenTo(this.lessonHintView,'hints:loaded',this.areHelpsReady);
			},
			this.areHelpsReady = function (curHelp) {
				this.helpsLoaded[curHelp.helpElement] = curHelp.value;
				// check if all are ready
			}
			
		};
		return Controller;
});