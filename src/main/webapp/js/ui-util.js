function makeFormsAjax() {
    //console.log("Hooking any lesson forms to make them ajax");
    $("form").ajaxForm(options);
}

function extractLessonTitle(el) {
    var title = $('h1',el).text();
    return title;
}

$(window).resize(function() {
    //$('#leftside-navigation').css('height',$('div.panel-body').height());
    console.log($(window).height());
});

