/* ### GOAT DATA/PROMISES ### */

goat.data = {
    /**** jQuery loads ... ****/
    loadLessonContent: function (_url) {
    //TODO: switch to $http (angular) later
    //return $http({method:'GET', url: _url});
    
    return $.get(_url, {}, null, "html");
    },
    loadCookies: function() {
        return $.get(goatConstants.cookieService, {});
    },
    loadHints: function () {
        return $.get(goatConstants.hintService, {});
    },
    loadSource: function() {
        return $.get(goatConstants.sourceService, {});
    },
    loadSolution: function () {
        return $.get(goatConstants.solutionService, {})
    },
    loadPlan: function () {
        return $.get(goatConstants.lessonPlanService, {});
    },
    loadParams: function() {
        return $.get(goatConstants.paramsService,{});
    },
    /*** angular data grabs ***/
    loadMenuData: function() {
        //TODO use goatConstants var for url
        return $http({method: 'GET', url: goatConstants.menuService});
    }
};
