$(document).ready(function() {
    $('#olvidado').click(function(e) {
        e.preventDefault();
        $('div#form-olvidado').toggle('500');
    });
    $('#acceso').click(function(e) {
        e.preventDefault();
        $('div#form-olvidado').toggle('500');
    });
});

function showPasswordReset() {
    console.log("clicking")
    $('#password-reset').show();
    $('#password-login').hide();
}

function showPassword() {
    console.log("clicking")
    $('#password-login').show();
    $('#password-reset').hide();
}