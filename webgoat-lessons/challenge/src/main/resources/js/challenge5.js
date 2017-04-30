$(document).ready(function () {
    getVotings();
    login('Guest');
})

function login(user) {
    $("#name").text(user);
    $.get("votings/login?user=" + user, function (result, status) {

    });
}

function getVotings() {
    $.get("votings/", function (result, status) {

    })
}

