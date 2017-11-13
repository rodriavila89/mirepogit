
var request = require('request');
var cheerio = require('cheerio');
var fs = require('fs');
var path = require('path');

var pathBase = 'C:/Users/ravila/Documents/test_bench/';


//paths
var today = new Date();

//url = 'http://f1.cronista.com/';
url = 'http://www.lanacion.com.ar/';




var nose = scrapHome(url);

console.log(nose);




/* (async function() {
    
    try {

        const ver1 = await scrapHome(url);

        console.log(ver1);

    } catch (e) {
        console.log('Error: ${e}')

    }

})() */

















function createPath(html){

    var pathBench = today.getFullYear() + '_' + ('0' + (today.getMonth()+1)).slice(-2) + '_' + today.getDate() + '_' + today.getHours() + '_' + today.getMinutes() + '_' + today.getSeconds();
    
    var pathCompleto = pathBase + pathBench;

    var pathCompletoHome = pathCompleto + '/home';
    var pathCompletoNota = pathCompleto + '/nota';

    fs.mkdirSync(pathCompleto);
    fs.mkdirSync(pathCompletoHome);
    fs.mkdirSync(pathCompletoNota);

    fs.writeFile(pathCompletoHome +  "\\" + 'test2.html', html, function(err){
        console.log('WriteFile OK');
    })

    return pathCompletoHome;

}



function scrapHome(url){

    var uno = new Array();

    request(url, function (error, response, html) {
        
        //console.log(response.statusCode);
        //console.log(html);
        
        if (!error && response.statusCode == 200) {
    
            var respuesta = createPath(html);
    
            console.log('OK: ' + respuesta);    
    
    
            var $ = cheerio.load(html);
    
            var count1 = 0;

            
            //CRONISTA
/*             $('div.entry-data').each(function(){
                var data2 = $(this);
                var algo = $(this).find('a').attr('href');
                uno.push(algo);
                count1++;
            }); */

            $('a.f-linkNota').each(function(){
                var algo = $(this).attr('href');
                uno.push(algo);
                count1++;
            });
    
            console.log('CANT: ' + count1);
            //console.log(uno);
    
    
        }
        
    });

    //var jaja = ['123', 'weqe'];
    return uno;


}

