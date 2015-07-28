var util = require('util'),
    colors = require('colors'),
    http = require('http'),
    fs = require('fs'),
    httpProxy = require('http-proxy');

//
// Http Server with proxyRequest Handler and Latency
//
httpProxy.createServer({
  target: 'https://localhost:9091',
  secure: false,
  ssl: {
    key: fs.readFileSync('key.pem', 'utf8'),
    cert: fs.readFileSync('cert.pem', 'utf8')
  }
}).listen(9090);


util.puts('https proxy '.blue + 'started '.green.bold + 'on port '.blue + '9090 '.yellow);