$(document).ready(function () {
    $("#postComment").unbind();
    $("#postComment").on("click", function () {
        var commentInput = $("#commentInput").val();
        $.ajax({
            type: 'POST',
            url: 'xxe/simple',
            data: JSON.stringify({text: commentInput}),
            contentType: "application/json",
            dataType: 'json'
        }).then(
            function () {
                getComments();
                $("#commentInput").val('');
            }
        )
    })
    getComments();
})

var html = '<li class="comment">' +
    '<div class="pull-left">' +
    '<img class="avatar" src="images/avatar1.png" alt="avatar"/>' +
    '</div>' +
    '<div class="comment-body">' +
    '<div class="comment-heading">' +
    '<h4 class="user">USER</h4>' +
    '<h5 class="time">DATETIME</h5>' +
    '</div>' +
    '<p>COMMENT</p>' +
    '</div>' +
    '</li>';

function getComments() {
    $.get("xxe/simple", function (result, status) {
        $("#comments_list").empty();
        for (var i = 0; i < result.length; i++) {
            var comment = html.replace('USER', result[i].user);
            comment = comment.replace('DATETIME', result[i].dateTime);
            comment = comment.replace('COMMENT', result[i].text);
            $("#comments_list").append(comment);
        }

    });
}