var restify = require('restify');
var mysql = require('mysql');
var connection = mysql.createConnection({
  multipleStatements: true,
  host     : '172.16.11.72',
  user     : 'contactos',
  password : '3sdagContactos',
  database : 'contactos'
});

var connection2 = mysql.createConnection({
  multipleStatements: true,
  host     : 'localhost',
  user     : 'root',
  password : 'eltrono22',
  database : 'contactos'
});



var ip_addr = '127.0.0.1';
var port = 8088;

var server = restify.createServer({
	name: "usuarios"
});

//curl -i -X POST -H "Content-Type: application/json" -d '{"Nombre":"DARIO" , "Apellido":"BENEDETTO" , "Email":"jaja@jaja.com.ar"}' http://127.0.0.1:1234/visitantes

//curl -i -X GET -H "Content-Type: application/json" http://127.0.0.1:1234/visitantes

//curl -i -X POST -H "Content-Type: application/json" -d '{"mes":"11","dia":"02"}' http://127.0.0.1:1234/visitantes/callpost

// curl -i -X POST -H "Content-Type: application/json" -d '{"nombre":"NATALIA" , "apellido":"CARRIZO" , "email":"naticarrizo90@gmail.com"}' http://127.0.0.1:1234/visitantes
//10.40.3.1


//call sp_contacto("LUCAAS22","carrizo","juajuasqw32134@gmail.com", "11293847546", "Comercial", "Quiero contactarme ahora para...", "2", "2016-12-18");

server.use(restify.queryParser());
server.use(restify.bodyParser());
server.use(restify.CORS());
server.listen(port, ip_addr, function(){
	console.log('%s activo en %s ', server.name, server.url);
});


var PATH = 'visitantes';


/*ENDPOINTS*/
server.get({path : PATH + '/call', version: '0.0.1'}, call1);
server.get({path : PATH , version: '0.0.1'}, findAllUsers);
server.post({path : PATH , version: '0.0.1'}, postNewUser);
server.post({path : PATH + '/callpost', version: '0.0.1'}, callPost);
server.post({path : PATH + '/callpost2', version: '0.0.1'}, callPost2);


function postNewUser(req, res, next){
	var user = {};
	user.nombre = req.params.nombre;
	user.apellido = req.params.apellido;
	user.email = req.params.email;

	res.setHeader('Access-Control-Allow-Origin','*');

	 //connection.query('INSERT INTO visitante (nombre, apellido, email) VALUES (\'' +user.nombre+'\', \''+user.apellido+'\', \''+user.email+'\')', 
	 	connection.query('INSERT INTO visitante SET ?', user,
	 	function (error, success){
            if(error) throw error;
            console.log(success);
            res.send(200, success.insertId);
        }
    );
}



function findAllUsers(req, res, next){  
    //"SET @p1=3133; SET @p2=6266; SET @p3=9939; call prueba2(@p1, @p2, @p3);"
    connection.query("select count_visit('miguel', 'miguelonaguero1989@gmail.com');", function (error, success){
      if(error) throw error;
      console.log(success);
      res.send(200, success);
      return next();
  });
}


function call1(req, res, next){  
    connection2.query('call sp1("01","02")', function (error, results){
      if(error) throw error;
      //console.log(results);
      res.send(200, results);
      return next();
  });
}




function callPost(req, res, next){
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

  var idform = 4;

  var sql = "CALL sp_contacto(?,?,?,?,?,?,?,?)";

  res.setHeader('Access-Control-Allow-Origin','*');
 
     connection.query(sql, [user.nombre, user.apellido, user.email, user.telefono,user.dirig, user.mensaje, idform, user.fecha], 
     function (error, success){
            if(error) console.log(error);
            console.log(success);
            res.send(200, success.insertId);
        }
    );
}



function callPost2(req, res, next){
  var user = {};
  user.var1 = req.params.var1;
  user.var2 = req.params.var2;
  user.var3 = req.params.var3;

  var formu = "2";

  var sql = "call prueba1(?,?,?);";

  var sql2 = 'SET @p1=' + user.var1 + '; SET @p2=' + user.var2 + '; SET @p3=' + user.var3 + '; call prueba1(@p1, @p2, @p3);' ;

  //var sql2 = 'SET @p1=' + connection.escape(user.var1) + '; SET @p2=' + connection.escape(user.var2) + '; SET @p3=' + connection.escape(user.var3) + '; call prueba2(@p1, @p2, @p3);' ;

  //var sql2 = 'SET @p1=333; SET @p2=666; SET @p3=999; call prueba2(@p1, @p2, @p3);' ;

  res.setHeader('Access-Control-Allow-Origin','*');
 
     connection.query(mysql.format(sql), [user.var1, user.var2, user.var3],
     function (error, success){
            if(error) throw error;
            console.log(success);
            //res.send(200, success.insertId);
            return next();
        }
    );
}





function Home(req, res, next){
	res.send(200, 'HOLA CRONISTA');
	return next();
}

server.get({path: '/', version: '0.0.1'}, Home);





//curl -i -X POST -H "Content-Type: application/json" -d '{"nombre":"marcelo" , "apellido":"corvalan" , "email":"macercorva89@gmail.com", "telefono":"123213", "dirig":"Redaccion", "mensaje":"TEST1", "idform":"2", "fecha":"2016-12-12"}' http://172.16.11.72:8088/forms/contactos






