/* ### GOAT DATA/PROMISES ### */

goat.data = {
    loadLessonContent: function (_url) {
    //TODO: switch to $http (angular) later
    //return $http({method:'GET', url: _url});
    return $.get(_url, {}, null, "html");
    },
    loadMenuData: function() {
        //TODO use goatConstants var for url
        return $http({method: 'GET', url: 'service/lessonmenu.mvc'});
    }
};
