
var cheerio = require('cheerio');
const fetch = require('node-fetch');
var fs = require('fs');


var misConstants = [
    [
        {
            index: 'ECC',
            path: 'C:/Users/ravila/Documents/test_bench/ECC/', 
            home: 'http://f1.cronista.com/',
            prop: 'div.entry-data'
            
        }
    ],
    [
        {
            index: 'LN',
            path: 'C:/Users/ravila/Documents/test_bench/LN/', 
            home: 'http://www.lanacion.com.ar/',
            prop: 'a.f-linkNota'
        }
    ]
]


function createPath(){

    var misPaths2 = new Object();
    
    var today = new Date();
    var pathBench = today.getFullYear() + '_' + ('0' + (today.getMonth()+1)).slice(-2) + '_' + today.getDate() + '_' + today.getHours() + '_' + today.getMinutes() + '_' + today.getSeconds();

    for (var i = 0; i < 2; i++) {

        var pathCompleto = misConstants[i][0].path + pathBench;

        var pathCompletoHome = pathCompleto + '/home';
        var pathCompletoNota = pathCompleto + '/nota';


        fs.mkdirSync(pathCompleto);
        fs.mkdirSync(pathCompletoHome);
        fs.mkdirSync(pathCompletoNota);

        misPaths2['home'+misConstants[i][0].index] = pathCompletoHome;
        misPaths2['nota'+misConstants[i][0].index] = pathCompletoNota;

        misConstants[i][0]['pathHome'] = pathCompletoHome;
        misConstants[i][0]['pathNota'] = pathCompletoNota;


        console.log(pathCompletoHome);
        console.log(pathCompletoNota);


        
    }

    return misPaths2;




}




var test1 = createPath();

//console.log(test1);

const cantidad = Object.keys(test1).length;

//console.log(cantidad)


(async function() {

    for (var x = 0; x < 2; x++) {

        if(misConstants[x][0].index == 'ECC'){

            misConstants[x][0]['urlNotas'] = await scrapHome(misConstants[x][0].home, misConstants[x][0].pathHome);
            console.log('TODAS LAS URLS');
            console.log(misConstants[x][0].urlNotas);

        }



        
        //console.log(misConstants[x][0].pathHome);
        //console.log(misConstants[x][0].pathNota);
        
    }
})()









async function scrapHome(url, pathHome){
    
        var uno = new Array();
    
        const searchUrl = url;
        const response = await fetch(searchUrl);
        const htmlString = await response.text();
    
        if(response.status !== 200){
            throw Error('NOT FOUND!');
        }
        //const respuesta = createPath(htmlString);
    
        //const respuesta = writeHtml(pathHome, 'home.html', htmlString)
    
        //console.log('RSVP de createPATH: ' + respuesta);
        
        const $ = cheerio.load(htmlString);
        
        $('div.entry-data').each(function(){
            var algo = $(this).find('a').attr('href');
            uno.push(algo);
        })
    
        return uno;
    
}