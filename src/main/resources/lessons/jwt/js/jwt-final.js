function follow(user) {
    $.ajax({
        type: 'POST',
        url: 'JWT/final/follow/' + user
    }).then(function (result) {
        $("#toast").append(result);
    })
}

