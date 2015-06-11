define(['jquery',
	'underscore',
	'libs/backbone',
	'goatApp/model/LessonContentData',
	'goatApp/view/LessonContentView'
	], 
	function($,_,Backbone,LessonContentData,LessonContentView) {
		'use strict'
		
		
		var Controller = function(options) {
			this.lessonView = options.lessonView;
			this.lessonContent = new LessonContentData();

			_.extend(Controller.prototype,Backbone.Events);
			this.start = function() {
				this.listenTo(this.lessonContent,'contentLoaded',this.onContentLoaded);
				
			}

			//load View, which can pull data
			this.loadLesson = function(scr,menu) {
				this.lessonContent.loadData({
					'screen': encodeURIComponent(scr),
					'menu': encodeURIComponent(menu),
				});

				//this.registerListeners();
			};

			this.onContentLoaded = function() {
				//this.lessonView  = new LessonContentView({content:LessonContent.content});
				this.lessonView.model = this.lessonContent;
				this.lessonView.render();
				//load cookies/parameters view

				//load title view (initially hidden)

				//plan view (initially hidden)

				//solution view (initially hidden)

				//source (initially hidden)

				//load help controls view (contextul to what helps are available)
			}
			
		};
		return Controller;
});