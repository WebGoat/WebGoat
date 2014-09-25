//goatConstants

var goatConstants = {
	CATEGORYCLASS:'fa-angle-right pull-right',
	lessonCompleteClass:'glyphicon glyphicon-check lessonComplete',
	selectedMenuClass:'selected',
	keepOpenClass:'keepOpen',
	menuPrefix : [
		{
		name:'LESSONS',
		type:'STATIC',
		complete:false,
		link:'',
		children:null,
		class:'fa-bars static'
	}],
	//services
	lessonService: 'service/lessonmenu.mvc',
	cookieService: 'service/cookie.mvc', //cookies_widget.mvc
	hintService:'service/hint.mvc',
	sourceService:'service/source.mvc',
	solutionService:'service/solution.mvc',
	lessonPlanService:'service/lessonplan.mvc',
	menuService: 'service/lessonmenu.mvc',
	lessonTitleService: 'service/lessontitle.mvc',
	restartLessonService: 'service/restartlesson.mvc',
	
	// literal messages
	notFound: 'Could not find',
	noHints: 'There are no hints defined.',
	noSourcePulled: 'No source was retrieved for this lesson'
	
};


