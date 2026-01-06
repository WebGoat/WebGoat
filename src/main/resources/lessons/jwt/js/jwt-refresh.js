$(document).ready(function () {
    login('Jerry');
});

/**
 * In a real deployment, this should be loaded from a secure configuration
 * or provided by the backend, never hard-coded in client-side JavaScript.
 *
 * For TEST/DRY-RUN mode, we simulate a configurable source:
 * - Prefer a non-secret value from an attribute on the <body> element.
 * - Fallback to an empty string rather than embedding a secret in code.
 */
function getConfiguredPassword() {
    var $body = $('body');
    var configured = $body.attr('data-demo-password');
    if (typeof configured === 'string') {
        return configured;
    }
    // FINAL FALLBACK: return an empty string instead of a hard-coded secret.
    return '';
}

function login(user) {
    $.ajax({
        type: 'POST',
        url: 'JWT/refresh/login',
        contentType: "application/json",
        data: JSON.stringify({
            user: user,
            // Removed hard-coded password literal; now sourced from
            // a configurable, non-secret value (or empty string).
            password: getConfiguredPassword()
        })
    }).success(
        function (response) {
            localStorage.setItem('access_token', response['access_token']);
            localStorage.setItem('refresh_token', response['refresh_token']);
        }
    );
}

//Dev comment: Pass token as header as we had an issue with tokens ending up in the access_log
webgoat.customjs.addBearerToken = function () {
    var headers_to_set = {};
    headers_to_set['Authorization'] = 'Bearer ' + localStorage.getItem('access_token');
    return headers_to_set;
};

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
    );
}
