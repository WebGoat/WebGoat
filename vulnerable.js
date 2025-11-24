const http = require("http");
const url = require("url");

http.createServer(function (req, res) {
    const q = url.parse(req.url, true).query;

    // ❌ Reflected XSS
    res.end("<h1>Hello " + q.name + "</h1>");
}).listen(3000);
