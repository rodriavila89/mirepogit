
var fs = require('fs');
var path = require('path');

var pathBase = 'C:/Users/ravila/Documents/test_bench/';

var today = new Date();

var pathBench = today.getFullYear() + '_' + today.getMonth() + '_' + today.getDate() + '_' + today.getHours() + '_' + today.getMinutes() + '_' + today.getSeconds();

var pathCompleto = pathBase + pathBench;

console.log(pathCompleto);

fs.mkdirSync(pathCompleto);



