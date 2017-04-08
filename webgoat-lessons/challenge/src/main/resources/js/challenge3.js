$(document).ready(function () {
    $("#postComment").on("blur", function () {
        var comment = $("#commentInput").val();
        $.post("challenge3", function (result, status) {
            var json;
            json = '{' +
                '   "comment":' + '"' + comment + '"'
                '}';
        })
    })

    $.get("challenge3", function (result, status) {
        alert("Hello");
    })
})