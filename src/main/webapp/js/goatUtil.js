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
    debugFormSubmission: false,
    // pre-submit callback 
    showRequest: function(formData, jqForm, options) {
        if (goat.utils.debugFormSubmission) {
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
    // post-submit callback 
    showResponse: function(responseText, statusText, xhr, $form) {
        // for normal html responses, the first argument to the success callback 
        // is the XMLHttpRequest object's responseText property 

        // if the ajaxForm method was passed an Options Object with the dataType 
        // property set to 'xml' then the first argument to the success callback 
        // is the XMLHttpRequest object's responseXML property 

        // if the ajaxForm method was passed an Options Object with the dataType 
        // property set to 'json' then the first argument to the success callback 
        // is the json data object returned by the server 
        if (goat.utils.debugFormSubmission) {
            alert('status: ' + statusText + '\n\nresponseText: \n' + responseText +
                    '\n\nThe output div should have already been updated with the responseText.');
        }
        // update lesson cookies and params
        // make any embedded forms ajaxy
        goat.utils.showLessonCookiesAndParams();
        // forms and links are now hooked with each standard lesson render (see Java class Screen.getContent())
        // but these are safe to call twice
        goat.utils.makeFormsAjax();
        goat.utils.ajaxifyAttackHref(); //TODO find some way to hook scope for current menu. Likely needs larger refactor which is already started/stashed
        //refresh menu
        angular.element($('#leftside-navigation')).scope().renderMenu();
    },
    makeFormsAjax: function() {
        // make all forms ajax forms
        var options = {
            target: '#lesson_content', // target element(s) to be updated with server response                     
            beforeSubmit: goat.utils.showRequest, // pre-submit callback, comment out after debugging 
            success: goat.utils.showResponse  // post-submit callback, comment out after debugging 

                    // other available options: 
                    //url:       url         // override for form's 'action' attribute 
                    //type:      type        // 'get' or 'post', override for form's 'method' attribute 
                    //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
                    //clearForm: true        // clear all form fields after successful submit 
                    //resetForm: true        // reset the form after successful submit 

                    // $.ajax options can be used here too, for example: 
                    //timeout:   3000 
        };
        //console.log("Hooking any lesson forms to make them ajax");
        $("form").ajaxForm(options);
    },
    displayButton: function(id, show) {
        if ($('#' + id)) {
            if (show) {
                $('#' + id).show();
            } else {
                $('#' + id).hide();
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
    scrollToHelp: function() {
        $('#leftside-navigation').height($('#main-content').height() + 15)
        var target = $('#lessonHelpsWrapper');
        goat.utils.scrollEasy(target);
    },
    scrollToTop: function() {
        $('.lessonHelp').hide();
        var target = $('#container');
        goat.utils.scrollEasy(target);
    },
    scrollEasy: function(target) {
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
        for (var i = 0; i < params.length; i++) {
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
        $('#' + id).addClass(goatConstants.selectedMenuClass);
        $('#' + id).parent().addClass(goatConstants.selectedMenuClass);
    },
    makeId: function(lessonName) {
        return lessonName.replace(/\s|\(|\)|\!|\:|\;|\@|\#|\$|\%|\^|\&|\*/g, '');//TODO move the replace routine into util function
    },
    ajaxifyAttackHref: function() {
        /* Jason I commented this implementation out
         * I think we should show the attack link on the lessons that need it by modifying the lesson
         * itself or we could add a new button up top for "show lesson link"      
         $.each($('a[href^="attack?"]'),
         function(i,el) {
         var url = $(el).attr('href');
         $(el).attr('href','#');
         $(el).attr('link',url);
         //TODO pull currentMenuId
         $(el).click(
         function() {
         var _url = $(el).attr('link');
         $.get(_url, {success:showResponse});
         }
         )
         }
         );
         */
        // alternate implementation
        // unbind any bound events so we are safe to be called twice
        $('#lesson_content a').unbind('click');
        $('#lesson_content a').bind('click', function(event) {
            event.preventDefault();
            $.get(this.href, {}, function(response) {
                $('#lesson_content').html(response);
            });
        });
    }
};


$(window).resize(function() {
    //$('#leftside-navigation').css('height',$('div.panel-body').height());
    console.log($(window).height());
});