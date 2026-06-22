//goatConstants

var goatConstants = {
	getClasses: function() {
		return {
			categoryClass:'fa-angle-right pull-right',
			lessonCompleteClass:'glyphicon glyphicon-check lessonComplete',
			selectedMenuClass:'selected',
			keepOpenClass:'keepOpen'
		};
	},
	getServices: function() {
		return {
			lessonService: 'service/lessonmenu.mvc',
			cookieService: 'service/cookie.mvc', //cookies_widget.mvc
			hintService: 'service/hint.mvc',
			sourceService: 'service/source.mvc',
			solutionService: 'service/solution.mvc',
			lessonPlanService: 'service/lessonplan.mvc',
			menuService: 'service/lessonmenu.mvc',
			restartLessonService: 'service/restartlesson.mvc'
		}
	},
	getMessages: function() {
		return {
			notFound: 'Could not find',
			noHints: 'There are no hints defined.',
			noSourcePulled: 'No source was retrieved for this lesson'
		}
	},
	getDOMContainers:function() {
		return {
			lessonMenu: '#menu-container'
		}
	}
};
