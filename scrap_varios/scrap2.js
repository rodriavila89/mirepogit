
var request = require('request');
var cheerio = require('cheerio');
var fs = require('fs');

var title, release, rating;

url = 'http://f1.cronista.com/';

request(url, function (error, response, html) {

    //console.log(response.statusCode);
    //console.log(html);
    
    if (!error && response.statusCode == 200) {
        
        console.log('IN');
        var $ = cheerio.load(html);


/*         $('div.site-content').each(function(i, element){
            //var a = $(this).prev();
            console.log('a.text()');
        }) */ 


        //$('article').filter('.entry-box.entry-70-mod-1').attr('class');

        //var a = $(this).prev();

        //console.log(b);


/*         $('.entry-box.entry-70-mod-1').filter(function(){
            var data = $(this);

            title = data.children().first();

            console.log(title);

        }); */

         

        var nose = $('article.entry-box.entry-70-mod-1 > div.entry-data > p').text();

        console.log(nose);


    }

});