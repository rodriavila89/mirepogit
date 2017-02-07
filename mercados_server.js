var 
	restify = require('restify');
	server = restify.createServer();
	sql = require('mssql'), 
	count = 0,

	config = {
	    driver: 'tedious',
	    user: 'dolarapp',
	    password: 'dolarappX09',
	    server: 'localhost', // You can use 'localhost\\instance' to connect to named instance 
	    database: 'mercadosweb',
	    options: {
	        encrypt: false // Use this if you're on Windows Azure 
	    }
	}
 
var connection = new sql.Connection(config, function(err) {
	if(err) {
		console.log('sql not connected',err);
		return false
	}
	console.log('sql connected');
});


var obtenerMoneda = function (req, res, next) {
	var request = new sql.Request(connection); // or: var request = connection.request(); 


	if (req.params.id) request.input('id', sql.Int, req.params.id);
	request.execute('ObtenerMonedas', function(err, recordset, returnValue) {
		if(err) {
			console.log(err);
			res.send(500, err);
		}
		res.send(recordset[0]);
		console.dir(recordset);
	});
  	next();
/*

	request.query('ObtenerMonedas', function(err, recordset) {
		if(err) {
			console.log(err);
			res.send(500, err);
		}
		res.send(recordset);
		console.dir(recordset);
	});
  next(); 
*/
}

var obtenerMonedaHistoria = function (req, res, next) {
	var request = new sql.Request(connection); // or: var request = connection.request(); 
	console.log('moneda id',req.params.id);
	request.input('id', sql.Int, req.params.id);
	request.input('FecDesde', sql.DateTime2, req.body.fecha);
	request.execute('obtenermonedaHistoria', function(err, recordset, returnValue) {
		if(err) {
			console.log(err);
			res.send(500, err);
		}
		res.send(recordset[0]);
		console.dir(recordset);
	});
  	next();
}

server.use(restify.authorizationParser());
server.use(restify.bodyParser());

server.use( function (req, res, next) {
    var users;
    count++;

    // if (/* some condition determining whether the resource requires authentication */) {
    //    return next();
    // }

	//TODO: Abstract this dict into sql table.
    users = {
        foo: {
            id: 1,
            password: 'cronista'
        }
    };

    if (req.username == 'anonymous' || !users[req.username] || req.authorization.basic.password !== users[req.username].password) {
        next(new restify.NotAuthorizedError());
    } 

    next();
});

/*ENDPOINTs*/
server.post('/mercados/moneda', obtenerMoneda);
server.post('/mercados/moneda/:id', obtenerMoneda);
server.post('/mercados/moneda/:id/historia', obtenerMonedaHistoria);

/* indicadores descripcion */

var ObtenerIndicadoresDescripcion = function (req, res, next) {
    var request = new sql.Request(connection); // or: var request = connection.request(); 
    request.execute('ObtenerIndicadoresDefinicion', function (err, recordset, returnValue) {
        if (err) {
            console.log(err);
            res.send(500, err);
        } else {
            res.send(recordset[0]);
            console.dir(recordset);
        }
    });
    next();
}


var ObtenerAccionesLideres = function (req, res, next) {
    var request = new sql.Request(connection); 
    request.execute('ObtenerAccionesLideres', function (err, recordset, returnValue) {
        if (err) {
            console.log(err);
            res.send(500, err);
        } else {
            res.send(recordset[0]);
            console.dir(recordset);
        }
    });
    next();
}


server.get('/mercados/indicadores', ObtenerIndicadoresDescripcion);
server.get('/mercados/accioneslideres', ObtenerAccionesLideres);

/* server */

server.listen(8088, function() {
  console.log('%s listening at %s', server.name, server.url);
});
