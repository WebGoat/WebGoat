/* ### GOAT DATA/PROMISES ### */

goat.data = {
    /**** jQuery loads ... ****/
    loadLessonContent: function ($http,_url) {
    //TODO: switch to $http (angular) later
        return $http({method:'GET', url: _url});
    //return $.get(_url, {}, null, "html");
    },
    loadCookies: function($http) {
        return $http({method: 'GET', url: goatConstants.cookieService});
        //return $.get(goatConstants.cookieService, {});
    },
    loadHints: function ($http) {
        return $http({method: 'GET', url: goatConstants.hintService});
        //return $.get(goatConstants.hintService, {});
    },
    loadSource: function($http) {
        return $http({method: 'GET', url: goatConstants.sourceService});
        //return $.get(goatConstants.sourceService, {});
    },
    loadSolution: function ($http) {
        return $http({method: 'GET', url: goatConstants.solutionService});
        //return $.get(goatConstants.solutionService, {});
    },
    loadPlan: function ($http) {
        return $http({method: 'GET', url: goatConstants.lessonPlanService});
        //return $.get(goatConstants.lessonPlanService, {});
    },
    loadParams: function($http) {
        return $http({method: 'GET', url: goatConstants.paramsService});
        //return $.get(goatConstants.paramsService,{});
    },
    loadMenu: function($http) {
        return $http({method: 'GET', url: goatConstants.lessonService});
    },
    loadLessonTitle: function ($http) {
        return $http({method: 'GET', url: goatConstants.lessonTitleService});
    },
    loadRestart: function ($http) {
        return $http({method: 'GET', url:goatConstants.restartLessonService})
    }
    
};
