
var request = require('request');
var cheerio = require('cheerio');
var fs = require('fs');
var path = require('path');
const fetch = require('node-fetch');

var constECC = {
    path: 'C:/Users/ravila/Documents/test_bench/ECC/', 
    home: 'http://f1.cronista.com/', 
    prop: '' 
};



var pathBase = 'C:/Users/ravila/Documents/test_bench/ECC/';
var pathBaseLN = 'C:/Users/ravila/Documents/test_bench/LN/';


url = 'http://f1.cronista.com/';

urlLN = 'http://www.lanacion.com.ar/';




(async function() {
    
    try {

        var today = new Date();
        var pathBench = today.getFullYear() + '_' + ('0' + (today.getMonth()+1)).slice(-2) + '_' + today.getDate() + '_' + today.getHours() + '_' + today.getMinutes() + '_' + today.getSeconds();

        const path = await createPath(pathBench);

        //console.log(path['home']);
        
        const ver1 = await scrapHome(url, path['home']);
        const verHomeLN = await scrapHomeLN(urlLN, path['homeLN']);

        //console.log(ver1);

        const ver2 = await scrapNota(ver1, path['nota']);
        const verNotaLN = await scrapNotaLN(verHomeLN, path['notaLN']);

        //console.log(ver2);

    } catch (e) {
        console.log(e);
    }

})()










function createPath(myPath){
    
    var pathCompleto = pathBase + myPath;
    var pathCompletoLN = pathBaseLN + myPath;

    var pathCompletoHome = pathCompleto + '/home';
    var pathCompletoNota = pathCompleto + '/nota';

    var pathCompletoHomeLN = pathCompletoLN + '/home';
    var pathCompletoNotaLN = pathCompletoLN + '/nota';

    fs.mkdirSync(pathCompleto);
    fs.mkdirSync(pathCompletoHome);
    fs.mkdirSync(pathCompletoNota);

    fs.mkdirSync(pathCompletoLN);
    fs.mkdirSync(pathCompletoHomeLN);
    fs.mkdirSync(pathCompletoNotaLN);

    var misPaths = new Object();

    misPaths['home'] = pathCompletoHome;
    misPaths['nota'] = pathCompletoNota;

    misPaths['homeLN'] = pathCompletoHomeLN;
    misPaths['notaLN'] = pathCompletoNotaLN;

    return misPaths;

}




function writeHtml(path, fileName, template){

    var rsp;

    fs.writeFile(path +  "\\" + fileName, template, function(err){
        rsp = 'Write OK!';
    });

    return rsp;
}







async function scrapHome(url, pathHome){

    var uno = new Array();

    const searchUrl = url;
    const response = await fetch(searchUrl);
    const htmlString = await response.text();

    if(response.status !== 200){
        throw Error('NOT FOUND!');
    }
    //const respuesta = createPath(htmlString);

    const respuesta = writeHtml(pathHome, 'home.html', htmlString)

    console.log('RSVP de createPATH: ' + respuesta);
    
    const $ = cheerio.load(htmlString);
    
    $('div.entry-data').each(function(){
        var algo = $(this).find('a').attr('href');
        uno.push(algo);
    })

    return uno;

}



async function scrapHomeLN(url, pathHome){
    
    var uno = new Array();

    const searchUrl = url;
    const response = await fetch(searchUrl);
    const htmlString = await response.text();

    if(response.status !== 200){
        throw Error('NOT FOUND!');
    }
    //const respuesta = createPath(htmlString);

    const respuesta = writeHtml(pathHome, 'home.html', htmlString)

    console.log('RSVP de createPATH: ' + respuesta);
    
    const $ = cheerio.load(htmlString);
    
    $('a.f-linkNota').each(function(){
        var algo = $(this).attr('href');
        uno.push(algo);
    })

    return uno;

}







async function scrapNota(misNotas, pathNotas){

    //const cantidad = Object.keys(misNotas).length;

    for (var i = 0; i < misNotas.length; i++) {
        var element = misNotas[i];        

        var forSearch = "http://";
        var forSearch2 = "https://";

        //check si no es de ECC (redirect)
        var checkUrl = ( element.indexOf( forSearch ) != -1 || element.indexOf( forSearch2 ) != -1 ) ? element : 'http://f1.cronista.com' + element;

        console.log(checkUrl);

        const response = await fetch(checkUrl);

        const htmlString = await response.text();

        const respuesta = writeHtml(pathNotas, 'nota_' + i + '.html', htmlString);        
        
    }
    

    const cantidad = misNotas.length;

    return cantidad;

}




async function scrapNotaLN(misNotas, pathNotas){
    
    //const cantidad = Object.keys(misNotas).length;

    for (var i = 0; i < misNotas.length; i++) {
        var element = misNotas[i];        

        var forSearch = "http://";
        var forSearch2 = "https://";

        //check si no es de ECC (redirect)
        var checkUrl = ( element.indexOf( forSearch ) != -1 || element.indexOf( forSearch2 ) != -1 ) ? element : 'http://www.lanacion.com.ar' + element;

/*             if(element.indexOf( forSearch ) == -1){
            var checkUrl = 'http://www.lanacion.com.ar' + element;
        } */

        console.log(checkUrl);

        const response = await fetch(checkUrl);

        const htmlString = await response.text();

        const respuesta = writeHtml(pathNotas, 'nota_' + i + '.html', htmlString);        
        
    }
    

    const cantidad = misNotas.length;

    return cantidad;

}