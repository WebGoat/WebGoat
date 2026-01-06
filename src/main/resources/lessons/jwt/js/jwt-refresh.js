// NOTE: In this training context we cannot read real secrets from a backend or env here.
// To avoid hardcoding real passwords in source while keeping the interface compatible,
// we centralize the secret in a single variable that MUST be provided securely at runtime
// (e.g., injected into the page by the server as a non-sensitive training token or
// derived from a non-secret training configuration).
var WEBGOAT_JWT_TRAINING_PASSWORD = (function () {
    // If a non-sensitive training password is exposed via a global, prefer that.
    if (typeof window !== 'undefined' && window.webgoat && typeof window.webgoat.jwtTrainingPassword === 'string') {
        return window.webgoat.jwtTrainingPassword;
    }

    // Fallback to a clearly non-production placeholder that is NOT a real credential.
    // In a production system this value MUST be provided from a secure configuration
    // (environment variable, secret manager, or server-injected config), never hardcoded.
    return 'TRAINING_ONLY_PASSWORD';
})();

$(document).ready(function () {
    login('Jerry');
})

function login(user) {
    $.ajax({
        type: 'POST',
        url: 'JWT/refresh/login',
        contentType: "application/json",
        data: JSON.stringify({
            user: user,
            // Removed hard-coded real-looking secret and replaced with a training/config-driven value
            password: WEBGOAT_JWT_TRAINING_PASSWORD
        })
    }).success(
        function (response) {
            localStorage.setItem('access_token', response['access_token']);
            localStorage.setItem('refresh_token', response['refresh_token']);
        }
    )
}

//Dev comment: Pass token as header as we had an issue with tokens ending up in the access_log
webgoat.customjs.addBearerToken = function () {
    var headers_to_set = {};
    headers_to_set['Authorization'] = 'Bearer ' + localStorage.getItem('access_token');
    return headers_to_set;
}

//Dev comment: Temporarily disabled from page we need to work out the refresh token flow but for now we can go live with the checkout page
function newToken() {
    localStorage.getItem('refreshToken');
    $.ajax({
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('access_token')
        },
        type: 'POST',
        url: 'JWT/refresh/newToken',
        data: JSON.stringify({refreshToken: localStorage.getItem('refresh_token')})
    }).success(
        function () {
            localStorage.setItem('access_token', apiToken);
            localStorage.setItem('refresh_token', refreshToken);
        }
    )
}
