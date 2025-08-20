function follow(user) {
    $.ajax({
        type: 'POST',
        url: 'JWT/kid/follow/' + user
    }).then(function (result) {
        $("#toast").append(result);
    })
}
