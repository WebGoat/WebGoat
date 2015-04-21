define(['jquery',
	'underscore',
	'libs/backbone',
	'goatApp/model/LessonData'
	], 
	function($,_,Backbone,LessonData) {
		'use strict'
		//private vars

		var controller = function() {
			this.loadLesson = function(scr,menu) {
				var curLessonData = new LessonData({
					'screen': encodeURIComponent(scr),
					'menu': encodeURIComponent(menu),
				});
			}
		};

		return controller;

	//var curScreen,curMenu;

	//return { 
	//	'screen':curScreen
		// loadLesson called from the router to load the given lesson
		/*loadLesson: function (src,curMenu) {
			var curLesson = new LessonData({
				'screen': encodeURIComponent(scr),
				'menu': encodeURIComponent(curMenu),
			});

			//set listeners

		},
		restartLesson: function () {

		}
		//getters & setters*/
	//};
});