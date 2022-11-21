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
    $('#payload').on('input', call(true));
    $('#header').on('input', call(true));
    $('#secretKey').on('input', call(true));
    $('#token').on('input', call(false));
});

function call(encode) {
    return () => {
        var url = encode ? '/WebWolf/jwt/encode' : '/WebWolf/jwt/decode';
        var formData = encode ? $('#encodeForm').getFormData() : $('#decodeForm').getFormData();
        formData["secretKey"] = $('#secretKey').val();

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
