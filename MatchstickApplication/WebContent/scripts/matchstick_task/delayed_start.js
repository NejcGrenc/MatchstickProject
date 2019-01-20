

var canvas;
var ctx;

// TODO: Update path!
var standardPath = "MatchstickApplication/";
var pauseImage;


function startWithPause()
{
    console.log("Page setting start");
    
    canvas = document.getElementById('canvas');
    ctx = canvas.getContext("2d");
    
    // Display no buttons while pause is active
    var greenButtonContainer = document.getElementById("sucessButton");
    var redButtonContainer = document.getElementById("failureButton");
    greenButtonContainer.style.display = 'none';
    redButtonContainer.style.display = 'none';
    
    preloadPauseImageAndStart();
    console.log("Page setting end");
}

function preloadPauseImageAndStart() 
{
	pauseImage = new Image();
	pauseImage.src = "../../" + standardPath + "images/ic_play_arrow_black_48px.svg";
	pauseImage.onload = onPauseImageLoad;
}
function onPauseImageLoad()
{
	drawPauseButton();
}

function drawPauseButton()
{
    clearCanvas();
    var canvasMiddleX = canvas.width / 2;
    var canvasMiddleY = canvas.height / 2;
    var imgWidth = 80;
    var imgHeight = 80;
    drawImage(pauseImage, canvasMiddleX - imgWidth/2, canvasMiddleY - imgHeight/2, imgWidth, imgHeight);
    
    canvas.addEventListener("mousedown", beginListener, false);
}

function clearCanvas()
{			
    ctx.beginPath();
    ctx.clearRect( 0, 0, canvas.width, canvas.height);
    ctx.closePath();
}


function drawImage(image, startX, startY, imgWidth, imgHeight) 
{
    startX = (typeof startX !== 'undefined') ? startX : 0;
    startY = (typeof startY !== 'undefined') ? startY : 0;
    imgWidth = (typeof imgWidth !== 'undefined') ? imgWidth : canvas.width;
    imgHeight = (typeof imgHeight !== 'undefined') ? imgHeight : canvas.height;

    ctx.beginPath();
    ctx.drawImage(image, startX, startY, imgWidth, imgHeight);
    ctx.closePath();
}

function beginListener()
{
    canvas.removeEventListener("mousedown", beginListener, false);
    
    // Call main start function from matchstick_main.js
    start();
}


function waitScreen()
{    
    var waitString = "Please wait";
    
    clearCanvas();
    ctx.beginPath();
    ctx.font = "25px Arial";
    ctx.textAlign = "center";
    ctx.fillText(waitString, canvas.width/2, canvas.height/2); 
    ctx.closePath();
}
