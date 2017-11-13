
var request = require('request');
var cheerio = require('cheerio');
var fs = require('fs');

const fetch = require('node-fetch');




async function loadHtml(){

    //const searchUrl = 'https://www.hola.com.ar/2075816-lionel-messi-y-antonela-roccuzzo-primera-salida-en-la-dulce-espera';
    //const searchUrl = 'http://www.lanacion.com.ar/';
    //const searchUrl = 'https://www.infobae.com/opinion/2017/11/02/consolidado-el-triunfo-el-gobierno-resolver-los-temas-estrategicos/';
    const searchUrl = 'https://www.clarin.com/ondemand/eyJtb2R1bGVDbGFzcyI6IkNMQUNsYXJpbkNvbnRhaW5lckJNTyIsImNvbnRhaW5lcklkIjoidjNfY29sZnVsbF9ob21lIiwibW9kdWxlSWQiOiJtb2RfMjAxNzkxMTczOTg2NDY5IiwiYm9hcmRJZCI6IjEiLCJib2FyZFZlcnNpb25JZCI6IjIwMTcxMTAzXzAwNTMiLCJuIjoiMSJ9.json';

    const response = await fetch(searchUrl);
    const htmlString = await response.text();

    if(response.status !== 200)
        throw Error('NOT FOUND!');
    

    const $ = cheerio.load(htmlString);

    var uno = "OK";
        
    
    return htmlString;


}



(async function() {

    try {
        const algo = await loadHtml();
        console.log(algo);

    } catch (e) {
        //console.log(e)

    }


})()




