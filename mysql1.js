var mysql      = require('mysql');
var connection = mysql.createConnection({
  host     : '172.16.11.72',
  user     : 'contactos',
  password : '3sdagContactos',
  database : 'contactos'
});


/*
connection.connect();

connection.query('SELECT 1 + 1 AS solution', function(err, rows, fields) {
  if (err) throw err;

  console.log('The solution is: ', rows[0].solution);
});

connection.end();
*/

connection.connect(function(err) {
  if (err) {
    console.error('error connecting: ' + err.stack);
    return;
  }

  console.log('connected as id ' + connection.threadId);
});