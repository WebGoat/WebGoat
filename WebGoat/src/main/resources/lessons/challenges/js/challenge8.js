$(document).ready(function () {
    loadVotes();
    average();
})

function loadVotes() {
    $.get("challenge/8/votes/", function (votes) {
            var totalVotes = 0;
            for (var i = 1; i <= 5; i++) {
                totalVotes = totalVotes + votes[i];
            }
            console.log(totalVotes);
            for (var i = 1; i <= 5; i++) {
                var percent = votes[i] * 100 / totalVotes;
                console.log(percent);
                var progressBar = $('#progressBar' + i);
                progressBar.width(Math.round(percent) * 2 + '%');
                $("#nrOfVotes" + i).html(votes[i]);

            }
        }
    );
}

function average() {
    $.get("challenge/8/votes/average", function (average) {
            for (var i = 1; i <= 5; i++) {
                var number = average["average"];
                $("#star" + i).removeClass('btn-warning');
                $("#star" + i).removeClass('btn-default');
                $("#star" + i).removeClass('btn-grey');

                if (i <= number) {
                    $("#star" + i).addClass('btn-warning');
                } else {
                    $("#star" + i).addClass('btn-grey');
                }
            }
        }
    );
}


function doVote(stars) {
    $("#voteResultMsg").hide();
    $.get("challenge/8/vote/" + stars, function (result) {
        if (result["error"]) {
            $("#voteResultMsg").addClass('alert-danger alert-dismissable');
        } else {
            $("#voteResultMsg").addClass('alert-success alert-dismissable');
        }
        $("#voteResultMsg").html(result["message"]);
        $("#voteResultMsg").show();
    })
    loadVotes();
    average();
}
