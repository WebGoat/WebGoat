/*
 * WebAuthn lesson: GoatBank flows use a security key simulated by WebGoat, the bonus assignment
 * uses the browser WebAuthn API with a real (roaming) authenticator.
 */

function webauthnToBuffer(base64url) {
    var base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
    var binary = atob(base64 + '='.repeat((4 - base64.length % 4) % 4));
    var bytes = new Uint8Array(binary.length);
    for (var i = 0; i < binary.length; i++) {
        bytes[i] = binary.charCodeAt(i);
    }
    return bytes.buffer;
}

function webauthnToBase64Url(buffer) {
    var bytes = new Uint8Array(buffer);
    var binary = '';
    for (var i = 0; i < bytes.length; i++) {
        binary += String.fromCharCode(bytes[i]);
    }
    return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
}

function webauthnJson(value) {
    return JSON.stringify(value, null, 2);
}

// native promises only: the global jQuery of the lesson pages does not adopt native promises
function webauthnRequest(url, options) {
    return fetch(url, Object.assign({credentials: 'same-origin'}, options)).then(function (response) {
        return response.json().catch(function () {
            return {};
        }).then(function (json) {
            if (!response.ok) {
                throw new Error(json.error || ('HTTP ' + response.status));
            }
            return json;
        });
    });
}

function webauthnPost(url, body) {
    return webauthnRequest(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body || {})
    });
}

function webauthnError(error) {
    if (!error || !error.message) {
        return 'Request failed';
    }
    // DOMExceptions from the WebAuthn API, e.g. NotAllowedError when the user cancels
    return (error.name && error.name !== 'Error') ? error.name + ': ' + error.message : error.message;
}

/* ---------- GoatBank registration (simulated security key) ---------- */

var webauthnRegistrationOptions = null;

webgoat.customjs.webauthnRegistrationBody = function () {
    return $('#webauthn-reg-body').val();
};

function webauthnBeginRegistration() {
    $('#webauthn-reg-body').val('');
    webauthnPost('WebAuthn/registration/options').then(function (options) {
        webauthnRegistrationOptions = options;
        $('#webauthn-reg-options').text(webauthnJson(options));
        $('#webauthn-reg-touch').prop('disabled', false);
    }).catch(function (error) {
        $('#webauthn-reg-options').text(webauthnError(error));
    });
}

function webauthnTouchRegistrationKey() {
    if (!webauthnRegistrationOptions) {
        return;
    }
    webauthnPost('WebAuthn/authenticator/create', webauthnRegistrationOptions).then(function (credential) {
        // GoatBank's front end sends the user handle back so the server knows the account
        $('#webauthn-reg-body').val(webauthnJson({
            userHandle: webauthnRegistrationOptions.user.id,
            credential: credential
        }));
        $('#webauthn-reg-touch').prop('disabled', true);
    }).catch(function (error) {
        $('#webauthn-reg-body').val(webauthnError(error));
    });
}

/* ---------- GoatBank sign in (simulated security key) ---------- */

webgoat.customjs.webauthnAuthenticationBody = function () {
    return $('#webauthn-login-body').val();
};

function webauthnStartLogin() {
    var username = $('#webauthn-login-username').val();
    $('#webauthn-login-body').val('');
    webauthnPost('WebAuthn/authentication/options', {username: username}).then(function (options) {
        $('#webauthn-login-options').text(webauthnJson(options));
        return webauthnPost('WebAuthn/authenticator/get', options).then(function (credential) {
            $('#webauthn-login-body').val(webauthnJson({username: username, credential: credential}));
        });
    }).catch(function (error) {
        $('#webauthn-login-body').val(webauthnError(error));
    });
}

function webauthnOpenTicket() {
    webauthnRequest('WebAuthn/support-ticket').then(function (captured) {
        $('#webauthn-ticket-content').text(
            'POST /WebGoat/WebAuthn/authentication/finish HTTP/1.1\n' +
            'Content-Type: application/json\n\n' + webauthnJson(captured));
    });
}

/* ---------- Bonus: real roaming authenticator ---------- */

webgoat.customjs.webauthnSecurityKeyBody = function () {
    return $('#webauthn-sk-assertion').val();
};

function webauthnSupported() {
    if (!window.isSecureContext) {
        return 'WebAuthn needs a secure context. Open WebGoat via https:// or http://localhost '
            + '(current origin: ' + window.location.origin + ').';
    }
    if (!window.PublicKeyCredential || !navigator.credentials) {
        return 'This browser does not support WebAuthn.';
    }
    return null;
}

function webauthnCredentialToJson(credential) {
    var response = credential.response;
    var json = {
        id: credential.id,
        rawId: webauthnToBase64Url(credential.rawId),
        type: credential.type,
        authenticatorAttachment: credential.authenticatorAttachment || null,
        clientExtensionResults: typeof credential.getClientExtensionResults === 'function'
            ? credential.getClientExtensionResults() : {},
        response: {clientDataJSON: webauthnToBase64Url(response.clientDataJSON)}
    };
    if (response.attestationObject) {
        json.response.attestationObject = webauthnToBase64Url(response.attestationObject);
        json.response.transports = response.getTransports ? response.getTransports() : [];
    } else {
        json.response.authenticatorData = webauthnToBase64Url(response.authenticatorData);
        json.response.signature = webauthnToBase64Url(response.signature);
        json.response.userHandle = response.userHandle ? webauthnToBase64Url(response.userHandle) : null;
    }
    return json;
}

function webauthnDescriptors(descriptors) {
    return (descriptors || []).map(function (d) {
        return {type: d.type, id: webauthnToBuffer(d.id)};
    });
}

function webauthnRegisterSecurityKey() {
    var output = $('#webauthn-sk-registration');
    output.text('Waiting for your security key, touch it when it blinks...');
    webauthnPost('WebAuthn/securitykey/registration/options').then(function (options) {
        var publicKey = Object.assign({}, options, {
            challenge: webauthnToBuffer(options.challenge),
            user: Object.assign({}, options.user, {id: webauthnToBuffer(options.user.id)}),
            excludeCredentials: webauthnDescriptors(options.excludeCredentials)
        });
        return navigator.credentials.create({publicKey: publicKey});
    }).then(function (credential) {
        return webauthnPost('WebAuthn/securitykey/registration/finish', webauthnCredentialToJson(credential));
    }).then(function (result) {
        output.text(webauthnJson(result));
    }).catch(function (error) {
        output.text('Registration failed: ' + webauthnError(error));
    });
}

function webauthnSignInWithSecurityKey() {
    var output = $('#webauthn-sk-registration');
    webauthnPost('WebAuthn/securitykey/authentication/options').then(function (options) {
        var publicKey = Object.assign({}, options, {
            challenge: webauthnToBuffer(options.challenge),
            allowCredentials: webauthnDescriptors(options.allowCredentials)
        });
        return navigator.credentials.get({publicKey: publicKey});
    }).then(function (credential) {
        $('#webauthn-sk-assertion').val(JSON.stringify(webauthnCredentialToJson(credential)));
        // let WebGoat submit the assignment form so progress and feedback are handled
        document.getElementById('webauthn-sk-form').requestSubmit();
    }).catch(function (error) {
        output.text('Sign in failed: ' + webauthnError(error));
    });
}

$(document).ready(function () {
    $('#webauthn-reg-begin').off('click').on('click', webauthnBeginRegistration);
    $('#webauthn-reg-touch').off('click').on('click', webauthnTouchRegistrationKey);
    $('#webauthn-login-start').off('click').on('click', webauthnStartLogin);
    $('#webauthn-ticket-open').off('click').on('click', webauthnOpenTicket);
    $('#webauthn-sk-register').off('click').on('click', webauthnRegisterSecurityKey);
    $('#webauthn-sk-login').off('click').on('click', webauthnSignInWithSecurityKey);

    var unsupported = webauthnSupported();
    if (unsupported) {
        $('#webauthn-sk-unsupported').text(unsupported).show();
        $('#webauthn-sk-register, #webauthn-sk-login').prop('disabled', true);
    }
});
