
var request = require('request');
var cheerio = require('cheerio');
var fs = require('fs');

var title, release, rating;

url = 'https://www.infobae.com/';

request(url, function (error, response, html) {

    //console.log(response.statusCode);
    //console.log(html);
    
    if (!error && response.statusCode == 200) {
        
        console.log('IN');
        var $ = cheerio.load(html);
         

        //DESTACADA
        $('div.top-table-col.top-table-col-wide.top-table-col-right.no-vertical-rule.col-lg-8.col-md-8.col-sm-8.col-xs-12.right.mobile-first').filter(function(){
            var data = $(this);
            
            var titulo = data.children().first().children().first().children().first().text();
            var link = data.children().first().children().first().children().first().find('a').attr('href');

            //console.log(data.children().length);

            //console.log('titulo: ' + titulo);
            //console.log('link: ' + link);
            
        });




        //COLUMNA DERECHA
        $('div.top-table-col.top-table-col-narrow.top-table-col-left.no-vertical-rule.col-lg-4.col-md-4.col-sm-4.col-xs-12.right.mobile-last').filter(function(){

            var data2 = $(this);

            //console.log(contenido[i].children().first().children().first().children().first().text());

            //console.log(data2.children().last());

            //console.log(data2.children().first().children().first().children().first().text())


            for (var i = 0; i < data2.children().length; i++) {

                var titulo = data2.children().eq(i).children().first().children().first().text();

                var link = data2.children().eq(i).children().first().children().first().find('a').attr('href');

                console.log('TITULO-'+i+': ' + titulo);
                console.log('LINK-'+i+': ' + link);
                
                //console.log(data2.children().eq(i));

                //console.log(data2.children().eq(i).children().first().children().first().text());
                
            }


        });


    }

});