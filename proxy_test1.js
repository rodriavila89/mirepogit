var httpProxy = require("http-proxy"); 
httpProxy.createServer(9000, 'localhost').listen(8000);