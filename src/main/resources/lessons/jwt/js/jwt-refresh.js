$(document).ready(function () {
    login('Jerry');
});

function login(user) {
    // NOTE:
    // For security reasons, no password or secret is hard-coded here.
    // The backend lesson should handle authentication logic securely.
    // This client simply triggers the flow with a non-sensitive placeholder.

    $.ajax({
        type: 'POST',
        url: 'JWT/refresh/login',
        contentType: "application/json",
        // Use a non-secret placeholder value instead of a real password/secret.
        // Any real credential handling must be done on the server side using secure storage.
        data: JSON.stringify({ user: user, password: "PLACEHOLDER_PASSWORD" })
    }).success(
        function (response) {
            if (!response) return;
            if (response['access_token']) {
                localStorage.setItem('access_token', response['access_token']);
            }
            if (response['refresh_token']) {
                localStorage.setItem('refresh_token', response['refresh_token']);
            }
        }
    );
}

// Dev comment: Pass token as header as we had an issue with tokens ending up in the access_log
webgoat.customjs.addBearerToken = function () {
    var headers_to_set = {};
    var accessToken = localStorage.getItem('access_token');
    if (accessToken) {
        headers_to_set['Authorization'] = 'Bearer ' + accessToken;
    }
    return headers_to_set;
};

// Dev comment: Temporarily disabled from page we need to work out the refresh token flow but for now we can go live with the checkout page
function newToken() {
    var refreshToken = localStorage.getItem('refresh_token');
    if (!refreshToken) {
        return;
    }

    $.ajax({
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('access_token')
        },
        type: 'POST',
        url: 'JWT/refresh/newToken',
        data: JSON.stringify({ refreshToken: refreshToken }),
        contentType: "application/json"
    }).success(
        function (response) {
            // Expect the API to return the new tokens in a response object
            if (!response) return;
            if (response.access_token) {
                localStorage.setItem('access_token', response.access_token);
            }
            if (response.refresh_token) {
                localStorage.setItem('refresh_token', response.refresh_token);
            }
        }
    );
}
