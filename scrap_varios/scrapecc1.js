
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

});

