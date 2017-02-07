var restify = require('restify');
var mysql = require('mysql');
var connection = mysql.createConnection({
  host     : '172.16.11.72',
  user     : 'contactos',
  password : '3sdagContactos',
  database : 'contactos'
});


var port = 8081;
var server = restify.createServer({
  name: "ServidorContacto2"
});

server.use(restify.queryParser());
server.use(restify.bodyParser());
server.use(restify.CORS());
server.listen(port, function(){
	console.log('%s activo en %s ', server.name, server.url);
});


var PATH = 'forms';

/*ENDPOINTs*/
server.post({path : PATH + '/contactos', version: '0.0.1'}, contactos);



function contactos(req, res, next){
  var user = {};
  user.nombre = req.params.nombre;
  user.apellido = req.params.apellido;
  user.email = req.params.email;
  user.telefono = req.params.telefono;
  user.dirig = req.params.dirig;
  user.mensaje = req.params.mensaje;
  user.idform = req.params.idform;
  user.fecha = req.params.fecha;

  console.log('referer ',req.headers.referer);

  var sql = "CALL sp_contacto(?,?,?,?,?,?,?,?)";

  res.setHeader('Access-Control-Allow-Origin','*');
 
     connection.query(sql, [user.nombre, user.apellido, user.email, user.telefono,user.dirig, user.mensaje, user.idform, user.fecha], 
     function (error, success){
            if(error) console.log(error);
            console.log(success);
            res.send(200, success.insertId);
        }
    );
}









function suscripcion(req, res, next){
  var user = {};
  user.nombre = req.params.nombre;
  user.apellido = req.params.apellido;
  user.email = req.params.email;
  user.telefono = req.params.telefono;
  user.celular = req.params.celular;
  user.localidad = req.params.localidad;
  user.producto = req.params.producto;
  user.acepta = req.params.acepta;
  user.recibe = req.params.recibe;
  user.novedad = req.params.novedad;
  user.mensaje = req.params.mensaje;
  user.idform = req.params.idform;
  user.fecha = req.params.fecha;

  console.log('referer ',req.headers.referer);

  var sql = "CALL sp_suscripcion(?,?,?,?,?,?,?,?,?,?,?,?,?)";

  res.setHeader('Access-Control-Allow-Origin','*');
 
     connection.query(sql, [user.nombre, user.apellido, user.email, user.telefono,user.dirig, user.mensaje, user.idform, user.fecha], 
     function (error, success){
            if(error) console.log(error);
            console.log(success);
            res.send(200, success.insertId);
        }
    );
}





function Home(req, res, next){
	res.send(200, 'Hola Cronista');
	return next();
}

server.get({path: '/', version: '0.0.1'}, Home);

