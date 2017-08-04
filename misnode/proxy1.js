var portproxy = 8080;
var express  = require('express');
var app      = express();
var httpProxy = require('http-proxy');
var apiProxy = httpProxy.createProxyServer();
var serverContac = 'http://localhost:8088';

app.use(function (req, res, next) {
	console.log('server use', req.connection.remoteAddress + ' ' + req.url);
    next();
});
	
app.all("/forms/*", function(req, res) {
	console.log('redirect forms');
    apiProxy.web(req, res, {target: serverContac});
});

app.listen(portproxy);