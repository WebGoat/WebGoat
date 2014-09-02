//goatConstants

var goatConstants = {
	CATEGORYCLASS:'fa-angle-right pull-right',
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
	paramService: 'service/parms.mvc', //this is a stub .. need to discuss this
	// literals
	notFound: 'Could not find'
};


