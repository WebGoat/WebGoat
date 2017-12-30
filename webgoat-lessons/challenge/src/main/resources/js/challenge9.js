$(document).ready(function() {
    $('#login').click(function(e) {
        e.preventDefault();
        $('div#form-login').toggle('500');
    });
    $('#forgot').click(function(e) {
        e.preventDefault();
        $('div#form-login').toggle('500');
    });
});