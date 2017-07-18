// need custom js for this?

webgoat.customjs.onBypassResponse = function(e) {
    console.warn("showPasswordChange fired - "+ data)
}

var onViewProfile = function () {
    console.warn("on view profile activated")
    webgoat.customjs.jquery.ajax({
        method: "GET",
        url: "/WebGoat/IDOR/profile",
        contentType: 'application/json; charset=UTF-8'
     }).then(webgoat.customjs.idorViewProfile);
}
