goat.utils = {
    //TODO add recursion to handle arr[i].children objects
    // ... in case lower-level's need classes as well ... don't right now
    addMenuClasses: function(arr) {
        for (var i = 0; i < arr.length; i++) {
            var menuItem = arr[i];
            //console.log(menuItem);
            if (menuItem.type && menuItem.type === 'CATEGORY') {
                menuItem.class = 'fa-angle-right pull-right';
            }
        }
        return arr;
    },
    makeFormsAjax: function() {
        //console.log("Hooking any lesson forms to make them ajax");
        $("form").ajaxForm(options);
    },
    /**goatApp.extractLessonTitle
     *pulls lesson title from html fragment returned (looks for it in h1 element)
     *@param - html rendered to object passed in
    */
    extractLessonTitle:function (el) {
        var title = $('h1',el).text();
        return title;
    },
};

// ### GLOBAL FUNCTIONS ## //


$(window).resize(function() {
    //$('#leftside-navigation').css('height',$('div.panel-body').height());
    console.log($(window).height());
});