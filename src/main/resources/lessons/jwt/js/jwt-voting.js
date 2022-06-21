$(document).ready(function () {
    loginVotes('Guest');
})

function loginVotes(user) {
    $("#name").text(user);
    $.ajax({
        url: 'JWT/votings/login?user=' + user,
        contentType: "application/json"
    }).always(function () {
        getVotings();
    })
}

var html = '<a href="#" class="list-group-item ACTIVE">' +
    '<div class="media col-md-3">' +
    '<figure> ' +
    '<img class="media-object img-rounded" src="images/IMAGE_SMALL" alt="placehold.it/350x250"/>' +
    '</figure>' +
    '</div> ' +
    '<div class="col-md-6">' +
    '<h4 class="list-group-item-heading">TITLE</h4>' +
    '<p class="list-group-item-text">INFORMATION</p>' +
    '</div>' +
    '<div class="col-md-3 text-center">' +
    '<h2 HIDDEN_VIEW_VOTES>NO_VOTES' +
    '<small HIDDEN_VIEW_VOTES> votes</small>' +
    '</h2>' +
    '<button type="button" id="TITLE" class="btn BUTTON btn-lg btn-block" onclick="vote(this.id)">Vote Now!</button>' +
    '<div style="visibility:HIDDEN_VIEW_RATING;" class="stars"> ' +
    '<span class="glyphicon glyphicon-star"></span>' +
    '<span class="glyphicon glyphicon-star"></span>' +
    '<span class="glyphicon glyphicon-star"></span>' +
    '<span class="glyphicon glyphicon-star-empty"></span>' +
    '</div>' +
    '<p HIDDEN_VIEW_RATING>Average AVERAGE<small> /</small>4</p>' +
    '</div>' +
    '<div class="clearfix"></div>' +
    '</a>';

function getVotings() {
    $("#votesList").empty();
    $.get("JWT/votings", function (result, status) {
        for (var i = 0; i < result.length; i++) {
            var voteTemplate = html.replace('IMAGE_SMALL', result[i].imageSmall);
            if (i === 0) {
                voteTemplate = voteTemplate.replace('ACTIVE', 'active');
                voteTemplate = voteTemplate.replace('BUTTON', 'btn-default');
            } else {
                voteTemplate = voteTemplate.replace('ACTIVE', '');
                voteTemplate = voteTemplate.replace('BUTTON', 'btn-primary');
            }
            voteTemplate = voteTemplate.replace(/TITLE/g, result[i].title);
            voteTemplate = voteTemplate.replace('INFORMATION', result[i].information || '');
            voteTemplate = voteTemplate.replace('NO_VOTES', result[i].numberOfVotes || '');
            voteTemplate = voteTemplate.replace('AVERAGE', result[i].average || '');

            var hidden = (result[i].numberOfVotes === undefined ? 'hidden' : '');
            voteTemplate = voteTemplate.replace(/HIDDEN_VIEW_VOTES/g, hidden);
            hidden = (result[i].average === undefined ? 'hidden' : '');
            voteTemplate = voteTemplate.replace(/HIDDEN_VIEW_RATING/g, hidden);

            $("#votesList").append(voteTemplate);
        }
    })
}

webgoat.customjs.jwtSigningCallback = function () {
    getVotings();
}

function vote(title) {
    var user = $("#name").text();
    if (user === 'Guest') {
        alert("As a guest you are not allowed to vote, please login first.")
    } else {
        $.ajax({
            type: 'POST',
            url: 'JWT/votings/' + title
        }).then(
            function () {
                getVotings();
            }
        )
    }
}

