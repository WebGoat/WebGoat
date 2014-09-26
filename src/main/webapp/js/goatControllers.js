//main goat application file
//TODO: reorg

/* ### GOAT CONTROLLERS ### */

/* menu controller */
var goatMenu = function($scope, $http, $modal, $log, $templateCache) {
    $scope.cookies = [];
    $scope.params = [];
    $scope.renderMenu = function() {
        goat.data.loadMenu($http).then(//$http({method: 'GET', url: goatConstants.lessonService})
                function(menuData) {
                    var menuItems = goat.utils.addMenuClasses(goatConstants.menuPrefix.concat(menuData.data));
                    //top-tier 'categories'
                    for (var i = 0; i < menuItems.length; i++) {
                        menuItems[i].id = goat.utils.makeId(menuItems[i].name);//TODO move the replace routine into util function
                        menuItems[i].displayClass = ($scope.openMenu === menuItems[i].id) ? goatConstants.keepOpenClass : '';
                        if (menuItems[i].children) {
                            for (var j = 0; j < menuItems[i].children.length; j++) {
                                menuItems[i].children[j].id = goat.utils.makeId(menuItems[i].children[j].name);
                                //handle selected Menu state
                                if (menuItems[i].children[j].id === $scope.curMenuItemSelected) {
                                    menuItems[i].children[j].selectedClass = goatConstants.selectedMenuClass;
                                    menuItems[i].selectedClass = goatConstants.selectedMenuClass;
                                }
                                //handle complete state
                                if (menuItems[i].children[j].complete) {
                                    menuItems[i].children[j].completeClass = goatConstants.lessonCompleteClass;
                                } else {
                                    menuItems[i].children[j].completeClass = '';
                                }
                                if (menuItems[i].children[j].children) {
                                    for (var k = 0; k < menuItems[i].children[j].children.length; k++) {
                                        //TODO make utility function for name >> id
                                        menuItems[i].children[j].children[k].id = goat.utils.makeId(menuItems[i].children[j].children[k].name);
                                        //menuItems[i].children[j].children[k].id = menuItems[i].children[j].children[k].name.replace(/\s|\(|\)/g,'');
                                        //handle selected Menu state
                                        if (menuItems[i].children[j].children[k].id === $scope.curMenuItemSelected) {
                                            menuItems[i].children[j].children[k].selectedClass = goatConstants.selectedMenuClass;
                                            menuItems[i].children[j].selectedClass = goatConstants.selectedMenuClass;
                                        }
                                        //handle complete state
                                        if (menuItems[i].children[j].children[k].complete) {
                                            menuItems[i].children[j].children[k].completeClass = goatConstants.lessonCompleteClass;
                                        } else {
                                            menuItems[i].children[j].children[k].completeClass = ''
                                        }
                                    }
                                }
                            }
                        }
                    }
                    $scope.menuTopics = menuItems;
                    //
                    if ($scope.openMenu) {
                        $('ul' + $scope.openMenu).show();
                    }

                },
                function(error) {
                    // TODO - handle this some way other than an alert
                    console.error("Error rendering menu: " + error);
                }
        );
    };

    $scope.renderLesson = function(id, url, showControls) {//TODO convert to single object parameter
        $scope.hintIndex = 0;
        var curScope = $scope;
        $('.lessonHelp').hide();
        // clean up menus, mark selected
        $scope.curMenuItemSelected = id;
        goat.utils.highlightCurrentLessonMenu(id);
        curScope.parameters = goat.utils.scrapeParams(url);
        // lesson content
        goat.data.loadLessonContent($http, url).then(
                function(reply) {
                    goat.data.loadLessonTitle($http).then(
                            function(reply) {
                                $("#lessonTitle").text(reply.data);
                            }
                    );
                    //TODO encode html or get angular js portion working
                    $("#lesson_content").html(reply.data);
                    //hook forms and links (safe to call twice)
                    // links are hooked with each lesson now (see Java class Screen.getContent())
                    goat.utils.makeFormsAjax();// inject form?
                    goat.utils.ajaxifyAttackHref();
                    $('#leftside-navigation').height($('#main-content').height() + 15)//TODO: get ride of fixed value (15)here
                    //notifies goatLesson Controller of the less change
                    $scope.$emit('lessonUpdate', {params: curScope.parameters, 'showControls': showControls});
                }
        )
        $scope.renderMenu();
    };
    $scope.accordionMenu = function(id) {
        if ($('ul#' + id).attr('isOpen') == 0) {
            $scope.expandMe = true;
        } else {
            $('ul#' + id).slideUp(300).attr('isOpen', 0);
            return;
        }
        $scope.openMenu = id;
        $('.lessonsAndStages').not('ul#' + id).slideUp(300).attr('isOpen', 0);
        if ($scope.expandMe) {
            $('ul#' + id).slideDown(300).attr('isOpen', 1);
        }
    }
    $scope.renderMenu();
    // runs on first loadcan be augmented later to '
    // resume' for a given user ... currently kluged to start at fixed lesson
    var url = 'attack?Screen=32&menu=5';
    angular.element($('#leftside-navigation')).scope().renderLesson(null, url);
}

/* lesson controller */
var goatLesson = function($scope, $http, $log) {

    $('#hintsView').hide();
    // adjust menu to lessonContent size if necssary
    //cookies

    $scope.$on('lessonUpdate', function(params) {
        $scope.parameters = arguments[1].params;
        $scope.showHints = (arguments[1].showControls && arguments[1].showControls.showHints);
        $scope.showSource = (arguments[1].showControls && arguments[1].showControls.showSource);
        curScope = $scope; //TODO .. update below, this curScope is probably not needed
        goat.data.loadCookies($http).then(
                function(resp) {
                    curScope.cookies = resp.data;
                }
        );
        //hints
        curScope.hintIndex = 0;
        if ($scope.showHints) {
            goat.data.loadHints($http).then(
                    function(resp) {
                        curScope.hints = resp.data;
                        if (curScope.hints.length > 0 && curScope.hints[0].hint.indexOf(goatConstants.noHints) === -1) {
                            goat.utils.displayButton('showHintsBtn', true);
                        } else {
                            goat.utils.displayButton('showHintsBtn', false);
                        }
                    }
            );
        } else {
            $scope.hints = null;
            goat.utils.displayButton('showHintsBtn', false);
        }
        //source
        if ($scope.showSource) {
            goat.data.loadSource($http).then(
                    function(resp) {
                        curScope.source = resp.data;
                    }
            );
        } else {
            $scope.source = goatConstants.noSourcePulled;
        }

        //plan
        goat.data.loadPlan($http).then(
                function(resp) {
                    curScope.plan = resp.data;
                }
        );
        //solution
        goat.data.loadSolution($http).then(
                function(resp) {
                    curScope.solution = resp.data;
                }
        );
    });

    //goat.utils.scrollToTop();


    $scope.showLessonSource = function() {
        $('.lessonHelp').hide();
        $('#lesson_source_row').show();
        goat.utils.scrollToHelp();
    }

    $scope.showLessonPlan = function() {
        $('.lessonHelp').hide();
        $("#lesson_plan").html($scope.plan);
        $('#lesson_plan_row').show();
        goat.utils.scrollToHelp();
    }

    $scope.showLessonSolution = function() {
        $('.lessonHelp').hide();
        $("#lesson_solution").html($scope.solution);
        $('#lesson_solution_row').show();
        goat.utils.scrollToHelp();
    }

    $scope.manageHintButtons = function() {
        if ($scope.hintIndex === $scope.hints.length - 1) {
            $('#showNextHintBtn').css('visibility', 'hidden');
        } else if ($scope.hintIndex < $scope.hints.length - 1) {
            $('#showNextHintBtn').css('visibility', 'visible');
        }
        //
        if ($scope.hintIndex === 0) {
            $('#showPrevHintBtn').css('visibility', 'hidden');
        } else if ($scope.hintIndex > 0) {
            $('#showPrevHintBtn').css('visibility', 'visible');
        }
    };

    $scope.viewHints = function() {
        if (!$scope.hints) {
            return;
        }

        $('.lessonHelp').hide();
        $('#lesson_hint_row').show();
        //goat.utils.scrollToHelp();
        //TODO
        $scope.curHint = $scope.hints[$scope.hintIndex].hint;
        //$scope.curHint = $sce.trustAsHtml($scope.hints[$scope.hintIndex].hint);
        //TODO get html binding workin in the UI ... in the meantime ...
        //$scope.renderCurHint();
        $scope.manageHintButtons();
    };

    $scope.viewNextHint = function() {
        $scope.hintIndex++;
        $scope.curHint = $scope.hints[$scope.hintIndex].hint;
        $scope.renderCurHint();
        $scope.manageHintButtons();
    };

    $scope.viewPrevHint = function() {
        $scope.hintIndex--;
        $scope.curHint = $scope.hints[$scope.hintIndex].hint;
        $scope.renderCurHint();
        $scope.manageHintButtons();
    };

    $scope.renderCurHint = function() {
        $('#curHintContainer').html($scope.curHint);
    }

    $scope.hideHints = function() {

    };

    $scope.restartLesson = function() {
        goat.data.loadRestart($http).then(
                function(resp) {
                    angular.element($('#leftside-navigation')).scope().renderLesson(null, resp.data, {showSource: $scope.showSource, showHints: $scope.showHints});
                }
        )
    }

    $scope.showAbout = function() {
        $('#aboutModal').modal({
            //remote: 'about.mvc'
        });
    };
}


