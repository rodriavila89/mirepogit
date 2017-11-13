
var request = require('request');
var cheerio = require('cheerio');
var fs = require('fs');

const fetch = require('node-fetch');

var winston = require('winston');
const winstonRotator = require('winston-daily-rotate-file');

const errorLog = require('./logger').errorlog;
const successlog = require('./logger').successlog;

var schedule = require("node-schedule");
var rule = new schedule.RecurrenceRule();

rule.second = 10;





async function loadHtml(){

    //const searchUrl = 'http://f1.cronista.com/';
    //const searchUrl = 'http://www.lanacion.com.ar/';
    //const searchUrl = 'http://www.ambito.com/';
    //const searchUrl = 'https://www.infobae.com/';

    //const searchUrl = 'https://www.clarin.com/';
    const searchUrl = 'https://www.clarin.com/ondemand/eyJtb2R1bGVDbGFzcyI6IkNMQUNsYXJpbkNvbnRhaW5lckJNTyIsImNvbnRhaW5lcklkIjoidjNfY29sZnVsbF9ob21lIiwibW9kdWxlSWQiOiJtb2RfMjAxNzkxMTczOTg2NDY5IiwiYm9hcmRJZCI6IjEiLCJib2FyZFZlcnNpb25JZCI6IjIwMTcxMTAzXzAwNTMiLCJuIjoiMSJ9.json';

    

    const response = await fetch(searchUrl);
    const htmlString = await response.text();

    console.log(htmlString[3])

    
    

    if(response.status !== 200)
        throw Error('NOT FOUND!');
    

    const $ = cheerio.load(htmlString);


/*     return $('div.entry-data')
        .map((_, a) => ({
            imageUrl: $(this).find('a').attr('href')
            
        })); */

    var uno = new Array();
        
        //CRONISTA
/*     $('div.entry-data').each(function(){
        var algo = $(this).find('a').attr('href');
        uno.push(algo);
    }) */


/*     $('div.mt').each(function(){
        var algo = $(this).find('a').attr('href');
        uno.push(algo);

    }); */
    
/*     $('div.on-demand').each(function(){
        var algo = $(this).attr('data-src');
        
        uno.push(algo);

    }); */




/*     //lanacion
    $('a.f-linkNota').each(function(){
        var algo = $(this).attr('href');
        uno.push(algo);
    }) */



    //AMBITO
/*     $('article.portada-article-titulo').each(function(){
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
 */




    //uno.push(htmlString);
    return htmlString;


}








//schedule.scheduleJob(rule, function(){

    (async function() {


            try {
                var algo = await loadHtml();
                console.log(typeof(algo));
                //var myobj = JSON.parse(JSON.stringify(algo));
                //console.log(myobj)
                //console.log(algo.length);
                //var test111 = 'nose';
                //successlog.info(`Success Message and variables: ${test111}`);

                /* for (var x = 0; x < algo.length; x++) {
                    var element = algo[x];

                    if(typeof(element) !== 'undefined'){
                        console.log(element);
                    }
                    
                    
                } */

            } catch (e) {
                console.log(e)

            }


    })()

//});



//console.log(test);




/* request(url, function (error, response, html) {

    //console.log(response.statusCode);
    //console.log(html);
    
    if (!error && response.statusCode == 200) {
        
        console.log('IN');
        var $ = cheerio.load(html);

        var counter = 0;

        var jeje = $(this);

        $('div.entry-data').each(function(){
            
            var data2 = $(this);

            var algo = $(this).find('a').attr('href');

            console.log(algo);

            counter++


            //console.log(data2.children().length);



        });

        //console.log(counter);

        fs.writeFile('test2.html', html, function(err){
            console.log('check full mal');
        })

        //C:\Users\ravila\Documents\test_bench
        //var today = new Date();
        //var path = today.getFullYear() + '_' + today.getMonth() + '_' + today.getDate() + '_' + today.getHours() + '_' + today.getMinutes() + '_' + today.getSeconds()
        


    }

}); */

