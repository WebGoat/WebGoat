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
    displayButton: function(id,show) {
        if ($('#'+id)) {
            if (show) {
                $('#'+id).show();
            } else {
                $('#'+id).hide();
            }
        }
    },
    showLessonCookiesAndParams: function() {
        $.get(goatConstants.cookieService, {}, function(reply) {
            $("#lesson_cookies").html(reply);
        }, "html");
    },
    showLessonHints: function() {
        $('.lessonHelp').hide();
        $('#lesson_hint').html();
        $('#lesson_hint_row').show();
    },
    showLessonSource: function(source) {
        $('.lessonHelp').hide();
        //$('#lesson_source').html("<pre>"+goat.lesson.lessonInfo.source+"</pre>");
        $('#lesson_source_row').show();
        goat.utils.scrollToHelp();
    },
    showLessonSolution: function() {
        $('.lessonHelp').hide();
        $('#lesson_solution').html(goat.lesson.lessonInfo.solution);
        $('#lesson_solution_row').show();
        goat.utils.scrollToHelp();
    },
    showLessonPlan: function(plan) {
        $('.lessonHelp').hide();
        $("#lesson_plan").html(goat.lesson.lessonInfo.plan);
        $('#lesson_plan_row').show();
        goat.utils.scrollToHelp();
    },
    scrollToHelp:function() {
        $('#leftside-navigation').height($('#main-content').height()+15)
        var target = $('#lessonHelpsWrapper');
        goat.utils.scrollEasy(target);
    },
    scrollToTop: function() {
        $('.lessonHelp').hide();
        var target= $('#container');
        goat.utils.scrollEasy(target);
    },
    scrollEasy:function(target) {
        $('html,body').animate({
            scrollTop: target.offset().top
        }, 1000);
    },
    scrapeParams: function(url) {
        if (!url) {
            return;
        }
        var params = url.split('?')[1].split('&');
        var paramsArr = [];
        for (var i=0;i< params.length;i++) {
            var paramObj = {};
            paramObj.name = params[i].split('=')[0];
            paramObj.value = params[i].split('=')[1];
            paramsArr.push(paramObj);
        }
        return paramsArr;
    },
    highlightCurrentLessonMenu: function(id) {
        //TODO: move selectors in first two lines into goatConstants
        $('ul li.selected').removeClass(goatConstants.selectedMenuClass)
	$('ul li.selected a.selected').removeClass(goatConstants.selectedMenuClass)
	$('#'+id).addClass(goatConstants.selectedMenuClass);
	$('#'+id).parent().addClass(goatConstants.selectedMenuClass);
    },
    makeId: function (lessonName) {
        return lessonName.replace(/\s|\(|\)|\!|\:|\;|\@|\#|\$|\%|\^|\&|\*/g,'');//TODO move the replace routine into util function
    },
    ajaxifyAttackHREF: function () {
        // stub for dealing with CSRF lesson link issues and other similar issues
    }
};


$(window).resize(function() {
    //$('#leftside-navigation').css('height',$('div.panel-body').height());
    console.log($(window).height());
});