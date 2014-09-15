// goat.lesson name space
goat.lesson = {
    CurLesson: function(_lessonUrl) {
        return {
            hints:[],
            hintIndex:0,
            solution:null,
            plan:null,
            cookiesAndParams:[],
            params:[],
            source:null,
            lessonUrl:(_lessonUrl || null),
            clearInfo: function() {
                this.hints = null;
                this.solution = null;
                this.plan = null;
                this.cookies = null;
                this.source = null;
                this.params = null;
            },
            loadInfo: function() {
                this.getHints();
                this.getPlan();
                this.getSolution();
                this.getCookies();
                this.getSource();
                this.getParams();
            },
            getHints:function() {
                var scope = this;
                goat.data.loadHints().then(
                    function(resp) {
                        scope.hints = resp.data;
                        if (scope.hints.length > 0 && scope.hints[0].hint.indexOf(goatConstants.noHints) === -1) {
                            goat.utils.displayButton('showHintsBtn',true);
                        } else {
                            goat.utils.displayButton('showHintsBtn',false);
                        }
                        return scope;
                    },
                    function(err){
                        goat.utils.displayButton('showHintsBtn',false);
                        //TODO handle errors
                    }
                );
            },
            getSolution:function() {
                var scope = this;
                goat.data.loadSolution().then(
                    function(resp) {
                        scope.solution = resp.data;
                        goat.utils.displayButton('showSolutionBtn',true);
                        $('#showSolutionBtn').unbind().click(goat.utils.showLessonSolution);
                        return scope;
                    },
                    function(err){
                        scope.solution = null;
                        goat.utils.displayButton('showSolutionBtn',false);
                        //TODO handle errors
                    }
                );                
            },
            getPlan: function() {
                var scope = this;
                goat.data.loadPlan().then(
                    function(resp) {
                        scope.plan = resp.data;
                        goat.utils.displayButton('showPlanBtn',true);
                        $('#showPlanBtn').unbind().click(goat.utils.showLessonPlan);
                        return scope;
                    },
                    function(err){
                        goat.utils.displayButton('showPlanBtn',false);
                        //TODO handle errors
                    }
                );
            },
            getSource: function() {
                var scope = this;
                goat.data.loadSource().then(
                    function(resp) {
                        scope.source = resp.data;
                        goat.utils.displayButton('showSourceBtn',true);
                        $('#showSourceBtn').unbind().click(goat.utils.showLessonSource);
                        return scope;
                    },
                    function(err){
                        goat.utils.displayButton('showSourceBtn',false);
                        //TODO handle errors
                    }
                );
            },
            getCookies: function() {
                var scope = this;
                goat.data.loadCookies().then(
                    function(resp) {
                        scope.cookies = resp.data;
                        return scope;
                    },
                    function(err){
                        //TODO handle errors
                    }
                );
            },
            getParams: function() {
                this.params = goat.utils.scrapeParams(this.lessonUrl);
            }
        }
    }
};