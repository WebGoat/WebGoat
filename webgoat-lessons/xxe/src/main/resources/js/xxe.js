webgoat.customjs.register = function () {
    var xml = '<?xml version="1.0"?>' +
        '<user>' +
        '  <username>' + 'test' + '</username>' +
        '  <password>' + 'test' + '</password>' +
        '</user>';
    return xml;
}
webgoat.customjs.registerJson = function () {
   var json;
    json = '{' +
        '   "user":' + '"test"' +
        '  "password":' + '"test"' +
        '}';
    return json;
}
