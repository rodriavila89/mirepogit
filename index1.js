var request = require('request'),
    crypto = require('crypto');
 
var username = 'jbarilari@cronista.com',
    apiKey = 'api&user&n9QUyGmyTMpbelFVS4WxjQ==';
 
var date = new Date().toISOString(),
    hmac = crypto.createHmac('sha256', apiKey).update(date).digest('hex');

request.post({
    url: 'http://172.16.11.72:8088/load/?url=https://api.cxense.com/traffic/user',
    //url: 'https://api.cxense.com/traffic/user',
    //http://soporte.eccmedios.com.ar:8088/load/?url=https:https://api.cxense.com/traffic/user
    //proxy: 'http://172.16.11.72:8088/load/?url=',
    //proxy: 'http://172.16.11.72:8088/',
    //proxy: 'http://soporte.eccmedios.com.ar:8088',
    //proxy: 'http://soporte:soporte2015@proxyecc1.eccmedios:8080/',
    headers: { 'X-cXense-Authentication': 'username=' + username + ' date=' + date + ' hmac-sha256-hex=' + hmac },
    body: {
	"siteIds":[
		"1146315451622288932", //ECC
		"1146315962711178923", //AP
		"1144064162777410674"  //IT
		],
    "fields": ["events"], //
    "groups": ["ecc"], 
    "orderBy": "events", "start": "-1d", 
    "count": 50000
}
,
    json: true
}, function (error, response, body) {
    if (!error && response.statusCode === 200) {
		if(!body.groups) {
			console.log('body traffic no data: ' + body);
			return;
		}
        console.log('body traffic users count: ' + body.groups[0].items.length);
		console.log('body traffic users:' , JSON.stringify(body));
    } else {
		console.log('error',error);
		console.log('body: ' , JSON.stringify(body));
	}
});

/*
request.post({
    url: 'http://172.16.11.72:8088/load/?url=https://api.cxense.com/traffic/event',
    headers: { 'X-cXense-Authentication': 'username=' + username + ' date=' + date + ' hmac-sha256-hex=' + hmac },
    body: {
    //"siteId":"1146315962711178923", // AP
	//"siteId":"1146315451622288932", //ECC
	"siteIds":[
		"1146315451622288932", //ECC
		"1146315962711178923", //AP
		"1144064162777410674"  //IT
		],
	 "groups":["url"], 
    //"filters": [{"type":"user", "group":"ecc", "item":"webuser-pabdala@cronista.com"} ], 
	"filters": [{"type":"user", "group":"ecc", "item":"webUser-jbaraforsale@gmail.com"} ], 
	//"filters": [{"type":"user", "group":"ecc", "item":"webuser-ropencho@gmail.com"} ], 
 
	"fields": ["events"],
    "groups": ["url"],
	
	//"groups":["url"], "fields":["events"], 
  //"filters":[{"type":"event","group":"referrerHost","item":"google.com"}],  "count":1
	
}
,
    json: true
}, function (error, response, body) {
    if (!error && response.statusCode === 200) {
		if(body.groups.length==0) {
			console.log('body traffic event no data: ' + JSON.stringify(body));
			return;
		}
		console.log('body traffic event count: ' , body.groups[0].items.length);
		console.log('body traffic event: ' , JSON.stringify(body));
    } else {
		console.log('error',error);
		console.log('body traffic events: ' , JSON.stringify(body));
    }
});

request.post({
    url: 'http://172.16.11.72:8088/load/?url=https://api.cxense.com/traffic/keyword',
    headers: { 'X-cXense-Authentication': 'username=' + username + ' date=' + date + ' hmac-sha256-hex=' + hmac },
    body: {
    //"siteId":"1146315962711178923", // AP
	//"siteId":"1146315451622288932", //ECC
	"siteIds":[
		"1146315451622288932", //ECC
		"1146315962711178923", //AP
		"1144064162777410674"  //IT
		],

	"fields": ["title", "events"], 
	"groups": ["company", "location", "person", "entity", "concept", "category"], 
	"filters": [
		{ "type": "user", "group": "ecc", 
		"item": "webUser-jbaraforsale@gmail.com" }
	], 
	"start": "-2d", //"stop": "-1d", 
	"count": 100 
	
}
,
    json: true
}, function (error, response, body) {
    if (!error && response.statusCode === 200) {
		if(body.groups.length==0) {
			console.log('body traffic keyword no data: ' + JSON.stringify(body));
			return;
		}
		console.log('body traffic keyword count: ' , body.groups[0].items.length);
		console.log('body traffic keyword: ' , JSON.stringify(body));
    } else {
		console.log('error',error);
		console.log('body traffic keyword: ' , JSON.stringify(body));
    }
});
*/