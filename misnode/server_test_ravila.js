
var restify = require('restify');
var mysql = require('mysql');
var nodemailer = require('nodemailer');


var transporter = nodemailer.createTransport({
	host:'172.16.11.75',
	port:587,
	secure:false, 
	tls:{rejectUnauthorized: false}
});


var db_config = {
	host     : '172.16.11.72',
	user     : 'contactos',
	password : '3sdagContactos',
	database : 'contactos_dev'
};


var connection;

function handleDisconnect() {
  connection = mysql.createConnection(db_config); 

  connection.connect(function(err) {
    if(err) {                               
      console.log('error when connecting to db:', err);
      setTimeout(handleDisconnect, 2000); 
    }                                     
  });                                     
                                          
  connection.on('error', function(err) {
    console.log('db error tmstmp '+Date().toString(), err);
    if(err.code === 'PROTOCOL_CONNECTION_LOST') {
      handleDisconnect();                         
    } else {                                      
      throw err;                                  
    }
  });
}

handleDisconnect();


var port = 8089;
var server = restify.createServer({
	name: "ServerLanding1"
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
server.post({path : PATH + '/suscripcion', version: '0.0.1'}, suscripcion);
server.post({path : PATH + '/landing', version: '0.0.1'}, landing);
server.get({path : PATH + '/jsontest', version: '0.0.1'}, jsontest);



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


  var sql = "CALL sp_contacto(?,?,?,?,?,?,?,?)";

  res.setHeader('Access-Control-Allow-Origin','*');
 
     connection.query(sql, [user.nombre, user.apellido, user.email, user.telefono,user.dirig, user.mensaje, user.idform, user.fecha], 
     function (error, success){
            if(error) console.log(error);
            console.log('tmstmp '+Date().toString(),success);
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

  console.log(user.producto);
  

  var sql = "CALL sp_suscripcion(?,?,?,?,?,?,?,?,?,?,?,?,?)";

  res.setHeader('Access-Control-Allow-Origin','*');
 
     connection.query(sql, [user.nombre, user.apellido, user.email, 
	 user.telefono, user.celular, user.localidad, user.producto, user.acepta, 
	 user.recibe, user.novedad, user.mensaje, user.idform, user.fecha], 
     function (error, success){
            if(error) console.log(error);
            console.log(success);
            res.send(200, success.insertId);
        }
    );
}

function landing(req, res, next){
  var user = {};
  user.nombre = req.params.nombre;
  user.apellido = req.params.apellido;
  user.email = req.params.email;
  user.telefono = req.params.telefono;
  user.celular = "";
  user.idform = req.params.idform;
  user.fecha = "1991-01-01";
  user.fecnac = req.params.fecnac;
  user.sexo = req.params.sexo;
  user.utmsource = req.params.utmsource != null ? req.params.utmsource : "";
  user.utmmedium = req.params.utmmedium != null ? req.params.utmmedium : "";
  user.utmcampaign = req.params.utmcampaign != null ? req.params.utmcampaign : "";

  console.log(user.producto);
  
  var sql = "CALL sp_landing(?,?,?,?,?,?,?,?,?,?,?,?)";

  res.setHeader('Access-Control-Allow-Origin','*');
 
     connection.query(sql, [user.nombre, user.apellido, user.email, 
	 user.telefono, user.celular, user.idform, user.fecha, user.fecnac, user.sexo, user.utmsource, user.utmmedium, user.utmcampaign], 
     function (error, success){
            if(error) console.log(error);
            console.log(success);
            res.send(200, success.insertId);
        }
    );
}


function jsontest(req, res, next){
	var test = {hola:"hola"};
	
	res.send(200, JSON.stringify(test));
	return next();
	
}


function Home(req, res, next){
	res.send(200, 'Hola Cronista');
	return next();
}

server.get({path: '/', version: '0.0.1'}, Home);

