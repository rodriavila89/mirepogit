
var cheerio = require('cheerio');
const fetch = require('node-fetch');
var fs = require('fs');

//my logger
var errorLog = require('./logger').errorlog;
var successlog = require('./logger').successlog;

//schedule task
var schedule = require("node-schedule");
var rule = new schedule.RecurrenceRule();
rule.minute = 5;


var startTime = new Date(Date.now() + 5000);

var endTime = new Date(startTime.getTime() + 50000000);



//const
var misConstants = [
    [
        {
            index: 'ECC',
            path: 'C:/Users/ravila/Documents/test_bench/ECC/', 
            url: 'http://f1.cronista.com/',
            prop: 'div.entry-data'
        }
    ],
    [
        {
            index: 'LN',
            path: 'C:/Users/ravila/Documents/test_bench/LN/', 
            url: 'http://www.lanacion.com.ar/',
            prop: 'a.f-linkNota'
        }
    ],
    [
        {
            index: 'AMBITO',
            path: 'C:/Users/ravila/Documents/test_bench/AMBITO/', 
            url: 'http://www.ambito.com/',
            prop: 'a.f-linkNota'
        }
    ],
    [
        {
            index: 'INFOBAE',
            path: 'C:/Users/ravila/Documents/test_bench/INFOBAE/', 
            url: 'https://www.infobae.com/',
            prop: 'a.f-linkNota'
        }
    ]
];



//main()
//schedule.scheduleJob({start: startTime, end: endTime, rule: '*/10 * * * *'}, function(){

    
    (async function() {

        var today = new Date();
        var pathTimeStamp = today.getFullYear() + '_' + ('0' + (today.getMonth()+1)).slice(-2) + '_' + today.getDate() + '_' + today.getHours() + '_' + today.getMinutes() + '_' + today.getSeconds();

        console.log(misConstants.length);
        
        for (var x = 0; x < misConstants.length; x++) {

            const path = await createPath(misConstants[x][0].path, pathTimeStamp, x);

            switch (misConstants[x][0].index) {

                case 'INFOBAE':
                    misConstants[x][0]['urlNotas'] = await scrapHomeGlobal(misConstants[x][0].url, misConstants[x][0].pathHome, misConstants[x][0].index);
                    console.log('TODAS LAS URLS');
                    console.log(misConstants[x][0].urlNotas);
                    var cantidad = await scrapNotaGlobal(misConstants[x][0].urlNotas,  misConstants[x][0].pathNota, misConstants[x][0].index);
                    console.log(cantidad);
                    successlog.info(`INFOBAE SCRAP HOME Y NOTA: OK`);
                break;

                case 'ECC': 
                    misConstants[x][0]['urlNotas'] = await scrapHomeGlobal(misConstants[x][0].url, misConstants[x][0].pathHome, misConstants[x][0].index);
                    console.log('TODAS LAS URLS');
                    console.log(misConstants[x][0].urlNotas);
                    var cantidad = await scrapNotaGlobal(misConstants[x][0].urlNotas,  misConstants[x][0].pathNota, misConstants[x][0].index);
                    console.log(cantidad);
                    successlog.info(`ECC SCRAP HOME Y NOTA: OK`);
                    break;

                case 'LN':
                    misConstants[x][0]['urlNotas'] = await scrapHomeGlobal(misConstants[x][0].url, misConstants[x][0].pathHome, misConstants[x][0].index);                
                    console.log('TODAS LAS URLS');
                    console.log(misConstants[x][0].urlNotas);
                    var cantidad = await scrapNotaGlobal(misConstants[x][0].urlNotas,  misConstants[x][0].pathNota, misConstants[x][0].index);
                    console.log(cantidad);
                    successlog.info(`LN SCRAP HOME Y NOTA: OK`);
                    break;
                
                case 'AMBITO':
                    misConstants[x][0]['urlNotas'] = await scrapHomeGlobal(misConstants[x][0].url, misConstants[x][0].pathHome, misConstants[x][0].index);
                    console.log('TODAS LAS URLS');
                    console.log(misConstants[x][0].urlNotas);
                    var cantidad = await scrapNotaGlobal(misConstants[x][0].urlNotas,  misConstants[x][0].pathNota, misConstants[x][0].index);
                    console.log(cantidad);
                    successlog.info(`AMBITO SCRAP HOME Y NOTA: OK`);
                    break;
                
                default:
                    break;
            }
            
        }

    })()


//});









function createPath(pathSitio, pathBase, indice){
    
    var misPaths2 = new Object();

    var pathCompleto = pathSitio + pathBase;

    var pathCompletoHome = pathCompleto + '/home';
    var pathCompletoNota = pathCompleto + '/nota';

    fs.mkdirSync(pathCompleto);
    fs.mkdirSync(pathCompletoHome);
    fs.mkdirSync(pathCompletoNota);

    misPaths2['home'+misConstants[indice][0].index] = pathCompletoHome;
    misPaths2['nota'+misConstants[indice][0].index] = pathCompletoNota;

    misConstants[indice][0]['pathHome'] = pathCompletoHome;
    misConstants[indice][0]['pathNota'] = pathCompletoNota;

    console.log(pathCompletoHome);
    console.log(pathCompletoNota);

    return misPaths2;
}




function writeHtml(path, fileName, template){    
    var rsp;
    fs.writeFile(path +  "\\" + fileName, template, function(err){
        rsp = 'Write OK!';
    });
    return rsp;
}




async function scrapNotaGlobal(misNotas, pathNotas, indice){
    
    const cantidad = misNotas.length;

    for (var i = 0; i < misNotas.length; i++) {
        var element = misNotas[i];        

        var forSearch = "http://";
        var forSearch2 = "https://";

        var checkUrl = '';

        //check si no es nota de sitio o (redirect)
        switch (indice) {
            case 'ECC':
                checkUrl = ( element.indexOf( forSearch ) != -1 || element.indexOf( forSearch2 ) != -1 ) ? element : 'http://f1.cronista.com' + element;
                break;
            case 'LN':
                checkUrl = ( element.indexOf( forSearch ) != -1 || element.indexOf( forSearch2 ) != -1 ) ? element : 'http://www.lanacion.com.ar' + element;
                break;
            case 'AMBITO':
                if(typeof(element) != 'undefined'){
                    if ( element.indexOf( forSearch ) == -1 && element.indexOf( forSearch2 ) == -1 ){
                        checkUrl = 'http://www.ambito.com' + element;
                    }
                }
                break;
            case 'INFOBAE':
                checkUrl = ( element.indexOf( forSearch ) != -1 || element.indexOf( forSearch2 ) != -1 ) ? element : 'https://www.infobae.com' + element;
                break;
            default:
                break;
        }
        

        if(checkUrl!==''){
            console.log(checkUrl);
            try{

                const response = await fetch(checkUrl);
                const htmlString = await response.text();
                const respuesta = writeHtml(pathNotas, 'nota_' + i + '.html', htmlString);
            
            } catch(e){
                //QUE HACER????
                //console.log(e);
                errorLog.error(`Error Message : ${e}`);
            }
        }        
    }

    return cantidad;

}






async function scrapHomeGlobal(url, pathHome, indice){
    
    var uno = new Array();

    const searchUrl = url;
    const response = await fetch(searchUrl);
    const htmlString = await response.text();

    if(response.status !== 200){
        throw Error('NOT FOUND!');
    }

    const respuesta = writeHtml(pathHome, 'home.html', htmlString)
    
    const $ = cheerio.load(htmlString);

    //extrae notas de home
    switch (indice) {
        case 'ECC':

            $('div.entry-data').each(function(){
                var algo = $(this).find('a').attr('href');
                uno.push(algo);
            });
            
            break;

        case 'LN':

            $('a.f-linkNota').each(function(){
                var algo = $(this).attr('href');
                uno.push(algo);
            });

            break;
        
        case 'AMBITO':
            $('article.portada-article-titulo').each(function(){
                var algo = $(this).find('a').attr('href');
                uno.push(algo);
            })
        
            $('article.mod-esp-bajada > header').each(function(){
                var algo = $(this).find('a').attr('href');
                uno.push(algo);
            })        
            
            //AUTOS Y PLACERES 2 NOTAS
            $('div.modulo-9 > h5').each(function(){
                var algo = $(this).find('a').attr('href');
                uno.push(algo);
            })
        
            //AUTOS Y PLACERES -> 4 notas
            $('div.modulo-10 > div.col-xs-12 > h5').each(function(){
                var algo = $(this).find('a').attr('href');
                uno.push(algo);
            })        
        
            //PRINCIPAL BIZ
            $('article.mod-esp-bajada > h5').each(function(){
                var algo = $(this).find('a').attr('href');
                uno.push(algo);
            })
            
            break;

        case 'INFOBAE':
            $('div.headline').each(function(){
                var algo = $(this).find('a').attr('href');
                uno.push(algo);
        
            });
            break;
    
        default:
            break;
    }    

    return uno;
    
}