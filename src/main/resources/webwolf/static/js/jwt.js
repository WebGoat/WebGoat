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
let timeout = null;

tokeninput.addEventListener('keyup', function (e) {
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        call(false);
    }, 1000);
});
headerinput.addEventListener('keyup', function (e) {
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        call(true);
    }, 1000);
});
secretKeyinput.addEventListener('keyup', function (e) {
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        call(true);
    }, 1000);
});
payloadinput.addEventListener('keyup', function (e) {
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        call(true);
    }, 1000);
});
});

function call(encode) {
        var url = encode ? 'jwt/encode' : 'jwt/decode';
        var formData = encode ? $('#encodeForm').getFormData() : $('#decodeForm').getFormData();
        formData["secretKey"] = $('#secretKey').val();
        console.log(formData);

        $.ajax({
            type: 'POST',
            url: url,
            data: formData,
            success: function (data) {
                update(data)
            },
            contentType: "application/x-www-form-urlencoded",
            dataType: 'json'
        });
}

function update(token) {
    $('#token').val(token.encoded);
    $('#payload').val(token.payload);
    $('#header').val(token.header);
    $('#token').css('background-color', token.validToken ? '#FFFFFF' : 'lightcoral');
    $('#header').css('background-color', token.validHeader ? '#FFFFFF' : 'lightcoral');
    $('#payload').css('background-color', token.validPayload ? '#FFFFFF' : 'lightcoral');
    $('#signatureValid').html(token.signatureValid ? "Signature valid" : "Signature invalid");
}
