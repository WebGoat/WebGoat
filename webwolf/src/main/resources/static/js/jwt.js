$(document).ready(() => {
    $('#encodedToken').on('input', () => {
        var token = $('#encodedToken').val();
        var secretKey = $('#secretKey').val();

        $.ajax({
            type: 'POST',
            url: '/WebWolf/jwt/decode',
            data: JSON.stringify({encoded: token, secretKey: secretKey}),
            success: function (data) {
                $('#tokenHeader').val(data.header);
                $('#tokenPayload').val(data.payload);
                updateSignature(data);
            },
            contentType: "application/json",
            dataType: 'json'
        });
    });
});

function encode() {
    return () => {
        var header = $('#tokenHeader').val();
        var payload = $('#tokenPayload').val();
        var secretKey = $('#secretKey').val();
        var token = {header: header, payload: payload, secretKey: secretKey};

        if (!parseJson(header)) {
            $('#encodedToken').val("");
            $('#tokenHeader').css('background-color', 'lightcoral');
        } else if (!parseJson(payload)) {
            $('#encodedToken').val("");
            $('#tokenPayload').css('background-color', 'lightcoral');
        } else {
            $.ajax({
                type: 'POST',
                url: '/WebWolf/jwt/encode',
                data: JSON.stringify(token),
                success: function (data) {
                    $('#encodedToken').val(data.encoded);
                    $('#tokenPayload').css('background-color', '#FFFFFF');
                    $('#encodedToken').css('background-color', '#FFFFFF');
                    $('#tokenHeader').css('background-color', '#FFFFFF');
                    updateSignature(data);
                },
                contentType: "application/json",
                dataType: 'json'
            });
        }
    };
}

$(document).ready(() => {
    $('#tokenPayload').on('input', encode());
    $('#tokenHeader').on('input', encode());
    $('#secretKey').on('input', encode());
});

function parseJson(text) {
    try {
        if (text) {
            JSON.parse(text);
        }
    } catch (e) {
        return false;
    }
    return true;
}

function updateSignature(data) {
    if (data.signatureValid) {
        $('#signatureValid').html("Signature valid");
    } else {
        $('#signatureValid').html("Signature invalid");
    }
}
