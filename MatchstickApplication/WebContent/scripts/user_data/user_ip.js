
/**
 * Get the user IP through the webkitRTCPeerConnection
 * @param onNewIP {Function} listener function to expose the IP locally
 * @return undefined
 */
function getUserIP(onNewIP) { //  onNewIp - your listener function for new IPs
    //compatibility for firefox and chrome
    var myPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
    var pc = new myPeerConnection({
        iceServers: []
    }),
    noop = function() {},
    localIPs = {},
    ipRegex = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/g,
    key;

    function iterateIP(ip) {
        if (!localIPs[ip]) onNewIP(ip);
        localIPs[ip] = true;
    }

     //create a bogus data channel
    pc.createDataChannel("");

    // create offer and set local description
    pc.createOffer().then(function(sdp) {
        sdp.sdp.split('\n').forEach(function(line) {
            if (line.indexOf('candidate') < 0) return;
            line.match(ipRegex).forEach(iterateIP);
        });
        
        pc.setLocalDescription(sdp, noop, noop);
    }).catch(function(reason) {
        // An error occurred, so handle the failure to connect
    });

    //listen for candidate events
    pc.onicecandidate = function(ice) {
        if (!ice || !ice.candidate || !ice.candidate.candidate || !ice.candidate.candidate.match(ipRegex)) return;
        ice.candidate.candidate.match(ipRegex).forEach(iterateIP);
    };
}

// Usage

//getUserIP(function(ip){
//    alert("Got IP! :" + ip);
//});

function getIP(json) {
	console.log("My public IP address is: ", json.ip);
}

//<script type="application/javascript" src="https://api.ipify.org?format=jsonp&callback=getIP"></script>


function httpGetAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous 
    xmlHttp.withCredentials = true;
    xmlHttp.setRequestHeader('Accept', 'application/json');
    xmlHttp.send(null);
}
httpGetAsync("https://api.ipify.org?format=jsonp&callback=getIP", getIP);



//// Probably requires HTTPS
// const url='https://api.ipify.org/';
// This one is good with HTTP
const Http = new XMLHttpRequest();
const url='https://ipinfo.io/json';
Http.open("GET", url);
console.log("!SENT! ");
Http.onreadystatechange=(e)=>{
console.log("!!!!!!!!!!!!!!!! " + Http.responseText);
}
Http.send();



// 3rd option
//    - it is blockecd by CORS

const xhr = new XMLHttpRequest();
const url2='https://api.ipgeolocation.io/ipgeo?apiKey=..............';
xhr.open("GET", url2);
xhr.withCredentials = true;
xhr.setRequestHeader('Accept', 'application/json');
xhr.onreadystatechange = function() {
	console.log("! https://api.ipgeolocation.io " + xhr.responseText);
	console.log("!2https://api.ipgeolocation.io " + this.responseText);
}
xhr.send();






var IPGeolocationAPI = require('ip-geolocation-api-javascript-sdk');
//Create IPGeolocationAPI object. Constructor takes two parameters.
//1) API key (Optional: To authenticate your requests through "Request Origin", you can skip it.)
//2) Async (Optional: It is used to toggle "async" mode in the requests. By default, it is true.)
var ipgeolocationApi = new IPGeolocationAPI("<API KEY>", false); 

//Function to handle response from IP Geolocation API
function handleResponse(json) {
    console.log("geolocation", json);
}
// Get complete geolocation for the calling machine's IP address
ipgeolocationApi.getGeolocation(handleResponse);



// JQUERY ' IPGeolocation
console.log("JQUERY ' ipapi");
$.getJSON('https://ipapi.co/json/', function(data) {
	  console.log(JSON.stringify(data, null, 2));
	  console.log("JQUERY ' ipapi");
	});

