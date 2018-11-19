function ace_collect() {
    let code = "";
    console.log("Test");
    $(".ace_line").each(function(i, el) {
        var to_add = el.innerHTML;
        if(/\/\/.*/.test(to_add)) {
            to_add = to_add.replace(/\/\/.*/i, '');
        }
        code += to_add;
    });
    $.ajax({
        type: "POST",
        url: "/WebGoat/SqlInjection/attack10b",
        dataType: "text",
        data: {
            editor: code
        },
        success: function(data) {
            console.log("entry");
            let lesson_feedback = JSON.parse(data);
            $("#insertcode .attack-feedback").css("display", "block");
            $("#insertcode .attack-feedback").html(lesson_feedback.feedback);
        }
    });
}