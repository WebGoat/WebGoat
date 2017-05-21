$(document).ready(function () {
    $("#postCommentSimple").unbind();
    $("#postCommentSimple").on("click", function () {
        var commentInput = $("#commentInputSimple").val();
        var xml = '<?xml version="1.0"?>' +
            '<comment>' +
            '  <text>' + commentInput + '</text>' +
            '</comment>';
        $.ajax({
            type: 'POST',
            url: 'xxe/simple',
            data: xml,
            contentType: "application/xml",
            dataType: 'xml',
            complete: function (data) {
                $("#commentInputSimple").val('');
                getComments('#commentsListSimple')
            }
        })
    });
    getComments('#commentsListSimple');
});

$(document).ready(function () {
    $("#postCommentBlind").unbind();
    $("#postCommentBlind").on("click", function () {
        var commentInput = $("#commentInputBlind").val();
        var xml = '<?xml version="1.0"?>' +
            '<comment>' +
            '  <text>' + commentInput + '</text>' +
            '</comment>';
        $.ajax({
            type: 'POST',
            url: 'xxe/blind',
            data: xml,
            contentType: "application/xml",
            dataType: 'xml',
            complete: function (data) {
                $("#commentInputBlind").val('');
                getComments('#commentsListBlind')
            }
        })
    });
    getComments('#commentsListBlind');
});

$(document).ready(function () {
    $("#postCommentContentType").unbind();
    $("#postCommentContentType").on("click", function () {
        var commentInput = $("#commentInputContentType").val();
        $.ajax({
            type: 'POST',
            url: 'xxe/content-type',
            data: JSON.stringify({text: commentInput}),
            contentType: "application/json",
            dataType: 'xml',
            complete: function (data) {
                $("#commentInputContentType").val('');
                getComments('#commentsListContentType')
            }
        })
    });
    getComments('#commentsListContentType');
});

$(document).ready(function () {
    getComments();
});

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

function getComments(field) {
    $.get("xxe/comments", function (result, status) {
        $(field).empty();
        for (var i = 0; i < result.length; i++) {
            var comment = html.replace('USER', result[i].user);
            comment = comment.replace('DATETIME', result[i].dateTime);
            comment = comment.replace('COMMENT', result[i].text);
            $(field).append(comment);
        }

    });
}
