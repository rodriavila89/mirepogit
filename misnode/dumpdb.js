const password = "!EdiCI0n35.";
const ppk = ".\\cronista2016-102.ppk";

var exec = require('child_process').execSync;
var exec2 = require('child_process').execSync;
var fs = require('fs');
var smartZip = require("smart-zip");
var pathUnzip = "C:\\temp\\";

var cmd = "pscp -pw !EdiCI0n35. -P 60 -i cronista2016-102.ppk ediciones@52.206.92.221:/home/ediciones/dumps4datalytics.zip C:\\temp\\";

var dump1 = "mysql -h 172.16.11.72 -u contactos --password=3sdagContactos -D opencms2 < C:\\temp\\unzip\\home\\ediciones\\dumps4datalytics\\CMS_USERDATA.sql";

var dump2 = "mysql -h 172.16.11.72 -u contactos --password=3sdagContactos -D opencms2 < C:\\temp\\unzip\\home\\ediciones\\dumps4datalytics\\CMS_USERS.sql";

var dump3 = "mysql -h 172.16.11.72 -u contactos --password=3sdagContactos -D opencms2 < C:\\temp\\unzip\\home\\ediciones\\dumps4datalytics\\TFS_COMMENTS.sql";

console.log(cmd);

exec(cmd, function(error, stdout, stderr) {
	console.log(stderr);
	console.log(error);
	console.log(stdout);// command output is in stdout
	
	
	
});


smartZip.unzip(pathUnzip + "dumps4datalytics.zip", pathUnzip + "unzip", function (error) {
		if (error) {
			throw error;
			console.log('ERROR');
		}
		
		console.log('File unziped.');
		
		exec2(dump1, function(error, stdout, stderr) {
			console.log(stderr);
			console.log(error);
			console.log(stdout);// command output is in stdout
  
		});
		
		console.log('FIN Primer Dump');
		
		exec(dump2, function(error, stdout, stderr) {
		console.log(stderr);
		console.log(error);
		console.log(stdout);// command output is in stdout
		});
		
		console.log('FIN Segundo Dump.');
		
		exec(dump3, function(error, stdout, stderr) {
		console.log(stderr);
		console.log(error);
		console.log(stdout);// command output is in stdout
		});
		
		console.log('FIN Tercer Dump.');
		
		
});


	
	
	




/*



*/





