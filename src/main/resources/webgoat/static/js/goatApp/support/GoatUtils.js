define(['jquery',
    'underscore',
    'backbone',
    'libs/jquery.form'
    ],
    function($,
        _,
        Backbone,
        JQueryForm) {
            var goatUtils = {
                makeId: function(lessonName) {
                    //var id =
                    return  lessonName.replace(/\s|\(|\)|\!|\:|\;|\@|\#|\$|\%|\^|\&|\*/g, '');
                },

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
//                debugFormSubmission: false,
                // pre-submit callback
                showRequest: function(formData, jqForm, options) {
                    if (GoatUtils.debugFormSubmission) {
                        // formData is an array; here we use $.param to convert it to a string to display it
                        // but the form plugin does this for you automatically when it submits the data
                        var queryString = $.param(formData);

                        // jqForm is a jQuery object encapsulating the form element.  To access the
                        // DOM element for the form do this:
                        // var formElement = jqForm[0];

                        alert('About to submit: \n\n' + queryString);
                    }
                    // here we could return false to prevent the form from being submitted;
                    // returning anything other than false will allow the form submit to continue
                    return true;
                },

                displayButton: function(id, show) {
                    if ($('#' + id)) {
                        if (show) {
                            $('#' + id).show();
                        } else {

                        }
                    }
                },

                showLessonCookiesAndParams: function() {
                    $.get(goatConstants.cookieService, {}, function(reply) {
                        $("#lesson_cookies").html(reply);
                    }, "html");
                },

                scrollToHelp: function() {
                    $('#leftside-navigation').height($('#main-content').height() + 15)
                    var target = $('#lesson-helps-wrapper');
                    this.scrollEasy(target);
                },

                scrollToTop: function() {
                    $('.lessonHelp').hide();
                    var target = $('#container');
                    this.scrollEasy(target);
                },

                scrollEasy: function(target) {
                    $('html,body').animate({
                        scrollTop: target.offset().top
                    }, 1000);
                },

                highlightCurrentLessonMenu: function(id) {
                    //TODO: move selectors in first two lines into goatConstants
                    $('ul li.selected').removeClass(goatConstants.selectedMenuClass)
                    $('ul li.selected a.selected').removeClass(goatConstants.selectedMenuClass)
                    $('#' + id).addClass(goatConstants.selectedMenuClass);
                    $('#' + id).parent().addClass(goatConstants.selectedMenuClass);
                },

        };

        return goatUtils;
});
