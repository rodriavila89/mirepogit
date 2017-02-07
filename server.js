var restify = require('restify');
//var mysql = require('mysql');

/*
connection = mysql.createConnection({
	host: '172.16.11.71',
	user: 'contactos',
	password: '3sdagContactos',
	database: 'test'
});
*/

var ip_addr = '127.0.0.1';
var port = '8088';

var server = restify.createServer({
	name: "usuarios"
});


server.use(restify.queryParser());
server.use(restify.bodyParser());
server.listen(port, ip_addr, function(){
	console.log('%s activo en %s ', server.name, server.url);
});


/*
var PATH = '/usuarios';

server.get({path : PATH , version : '0.0.1'} , findAllUsers);


function findAllUsers(req, res, next){
	//connection.query('SELECT 1 + 1 AS solution', function(err, rows, fields){
		//if (err) throw err;

		//console.log('The solution is: ', rows[0].solution);
		console.log('The solution is: ');
	};



*/



function Home(req, res, next){
	res.send(200, 'HOLA PUTO');
	return next();
}


server.get({path: '/', version: '0.0.1'}, Home);








