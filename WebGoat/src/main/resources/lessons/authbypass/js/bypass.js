// need custom js for this?

webgoat.customjs.onBypassResponse = function(data) {
    webgoat.customjs.jquery('#verify-account-form').hide();
    webgoat.customjs.jquery('#change-password-form').show();
}

var onViewProfile = function () {
    console.warn("on view profile activated")
    webgoat.customjs.jquery.ajax({
        method: "GET",
        url: "IDOR/profile",
        contentType: 'application/json; charset=UTF-8'
     }).then(webgoat.customjs.idorViewProfile);
}
