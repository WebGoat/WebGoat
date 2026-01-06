$(document).ready(function () {
    login('Jerry');
})

/**
 * Retrieve the JWT login password from a secure runtime source instead of hard-coding
 * it in the client-side code. This function falls back to a clearly non-production
 * placeholder if no runtime value is available.
 *
 * NOTE: In a real deployment, this value should NOT be a real secret on the client.
 * It is modeled here to remove a hard-coded credential from source and to make
 * testing possible without embedding sensitive data.
 */
function getJwtLoginPassword() {
    // Prefer a runtime-provided configuration (e.g., injected into the page at build/deploy time)
    if (window.webgoatConfig && typeof window.webgoatConfig.jwtLoginPassword === 'string') {
        return window.webgoatConfig.jwtLoginPassword;
    }

    // Safe, non-sensitive default used only for demonstration/testing.
    // This is intentionally not a real secret and is clearly identifiable as a placeholder.
    return 'CHANGE_ME_JWT_PASSWORD';
}

function login(user) {
    $.ajax({
        type: 'POST',
        url: 'JWT/refresh/login',
        contentType: "application/json",
        data: JSON.stringify({
            user: user,
            // Use indirect retrieval instead of a hard-coded literal.
            password: getJwtLoginPassword()
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
