// need custom js for this?

webgoat.customjs.idorViewProfile = function(data) {
    webgoat.customjs.jquery('#idor-profile').html(
        'name:' + data.name + '<br/>'+
        'color:' + data.color + '<br/>'+
        'size:' + data.size + '<br/>'
    );
}

var onViewProfile = function () {
    console.warn("on view profile activated")
    webgoat.customjs.jquery.ajax({
        method: "GET",
        url: "/WebGoat/IDOR/profile",
        contentType: 'application/json; charset=UTF-8'
     }).then(webgoat.customjs.idorViewProfile);
}
