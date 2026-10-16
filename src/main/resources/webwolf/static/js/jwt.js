(function ($) {
    $.fn.getFormData = function () {
        var data = {};
        var dataArray = $(this).serializeArray();
        for (var i = 0; i < dataArray.length; i++) {
            data[dataArray[i].name] = dataArray[i].value;
        }
        return data;
    }
})(jQuery);


$(document).ready(() => {
let tokeninput = document.getElementById('token');
let headerinput = document.getElementById('header');
let secretKeyinput = document.getElementById('secretKey');
let payloadinput = document.getElementById('payload');
let jwksinput = document.getElementById('jwks');
let secretSection = document.getElementById('secretKeySection');
let jwksSection = document.getElementById('jwksSection');
let timeout = null;

const debounce = (callback) => {
    clearTimeout(timeout);
    timeout = setTimeout(callback, 400);
};

const registerReactiveListener = (element, callback) => {
    if (!element) {
        return;
    }
    ['input', 'keyup', 'change', 'paste'].forEach((eventName) => {
        element.addEventListener(eventName, () => debounce(callback));
    });
};

registerReactiveListener(tokeninput, () => call(false));
registerReactiveListener(headerinput, () => call(true));
registerReactiveListener(secretKeyinput, () => call(true));
registerReactiveListener(jwksinput, () => call(false));
registerReactiveListener(payloadinput, () => call(true));

document.querySelectorAll('input[name="verificationMode"]').forEach((element) => {
    element.addEventListener('change', function () {
        toggleVerificationMode(this.value);
        debounce(() => call(false));
    });
});

function toggleVerificationMode(mode) {
    if (mode === 'jwks') {
        secretSection.classList.add('d-none');
        jwksSection.classList.remove('d-none');
    } else {
        jwksSection.classList.add('d-none');
        secretSection.classList.remove('d-none');
    }
}

toggleVerificationMode(document.querySelector('input[name="verificationMode"]:checked').value);
});

function call(encode) {
        var url = encode ? 'jwt/encode' : 'jwt/decode';
        var formData = encode ? $('#encodeForm').getFormData() : $('#decodeForm').getFormData();
        var mode = $("input[name='verificationMode']:checked").val();
        formData["verificationMode"] = mode;
        if (mode === 'jwks') {
            formData["secretKey"] = '';
            formData["jwks"] = $('#jwks').val();
        } else {
            formData["secretKey"] = $('#secretKey').val();
            formData["jwks"] = '';
        }
        showLoading();

        $.ajax({
            type: 'POST',
            url: url,
            data: formData,
            success: function (data) {
                update(data)
            },
            contentType: "application/x-www-form-urlencoded",
            dataType: 'json'
        }).always(() => hideLoading());
}

function update(token) {
    $('#token').val(token.encoded);
    $('#payload').val(token.payload);
    $('#header').val(token.header);
    $('#token').css('background-color', token.validToken ? '#FFFFFF' : 'lightcoral');
    $('#header').css('background-color', token.validHeader ? '#FFFFFF' : 'lightcoral');
    $('#payload').css('background-color', token.validPayload ? '#FFFFFF' : 'lightcoral');
    if (token.signatureValid) {
        $('#signatureValid').html("Signature valid");
    } else if (!$('#secretKey').val() && !$('#jwks').val()) {
        $('#signatureValid').html("Signature not validated");
    } else {
        $('#signatureValid').html("Signature invalid");
    }
}

function showLoading() {
    if (signatureSpinner) {
        signatureSpinner.classList.remove('d-none');
    }
}

function hideLoading() {
    if (signatureSpinner) {
        signatureSpinner.classList.add('d-none');
    }
}
