$(document).ready(function () {
    getVotings()
})

function login(user) {
    $.get("votings/login?user=" + user, function (result, status) {

    })
}


function getVotings() {
    $.get("votings/", function (result, status) {

    })
}
