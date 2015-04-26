define(['jquery',
	'underscore',
	'libs/backbone',
	'goatApp/model/LessonContentData',
	'goatApp/view/LessonContentView'
	], 
	function($,_,Backbone,LessonContent) {
		'use strict'
		
		
		var Controller = function(options) {
			this.lessonView = options.lessonView;
			this.lessonContent = new LessonContentData();

			_.extend(this,Backbone.Events);
			this.start = function() {
				this.listenTo(this.lessonContent,'contentLoaded',this.onContentLoaded);
			}

			//load View, which can pull data
			this.loadLesson = function(scr,menu) {
				this.lessonContent.loadContent({
					'screen': encodeURIComponent(scr),
					'menu': encodeURIComponent(menu),
				});

				//this.registerListeners();
			};

			this.onContentLoaded = function() {
				//this.lessonView  = new LessonContentView({content:LessonContent.content});
				//this.lessonView.render();
				console.debug('loading other stuff');
			}
			
		};
		return Controller;
});