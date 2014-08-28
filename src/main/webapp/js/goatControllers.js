//main goat application file
//TODO: reorg

/* ### GOAT CONTROLLERS ### */

/** Menu Controller
 *  prepares and updates menu topic items for the view
 */
goat.controller('goatLesson', function($scope, $http, $modal, $log, $templateCache) {
    //TODO: implement via separate promise and use config for menu (goat.data.loadMenuData())
    $http({method: 'GET', url: goatConstants.lessonService}).then(
            function(menuData) {
                var menuItems = goat.utils.addMenuClasses(goatConstants.menuPrefix.concat(menuData.data));
                $scope.menuTopics = menuItems;
            },
            function(error) {
                // TODO - handle this some way other than an alert
                console.error("Error rendering menu: " + error);
            }
    );
    $scope.renderLesson = function(url) {
        console.log(url + ' was passed in');
        // use jquery to render lesson content to div
        goat.data.loadLessonContent(url).then(
                function(reply) {
                    $("#lesson_content").html(reply);
                    // hook forms
                    goat.utils.makeFormsAjax();
                    $('#lessonTitle').text(goat.utils.extractLessonTitle($(reply)));
                    // adjust menu to lessonContent size if necssary
                    if ($('div.panel-body').height() > 400) {
                        $('#leftside-navigation').height($(window).height());
                    }
                    // hook into our pseudo service calls
                    // @TODO make these real services during phase 2
                    // show cookies and params
                    goat.utils.showLessonCookiesAndParams();
                    // show hints
                    goat.utils.showLessonHint();
                    // show plan
                    goat.utils.showLessonPlan();
                    // show solution
                    goat.utils.showLessonSolution();
                    // show source
                    goat.utils.showLessonSource();                    
                }
        );
    };
}).animation('.slideDown', function() {
    var NgHideClassName = 'ng-hide';
    return {
        beforeAddClass: function(element, className, done) {
            if (className === NgHideClassName) {
                $(element).slideUp(done);
            }
        },
        removeClass: function(element, className, done) {
            if (className === NgHideClassName) {
                $(element).hide().slideDown(done);
            }
        }
    };
});


/* Controllers for modal instances */
var showSourceController = function($scope, $modalInstance, lessonSource) {

    $scope.lessonSource = lessonSource;

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
};

var showSolutionController = function($scope, $modalInstance, lessonSolutionUrl) {

    $scope.lessonSolutionUrl = lessonSolutionUrl;

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
};




