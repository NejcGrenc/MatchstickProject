

var canvas;
var ctx;

var currentTimeouts = [];

var taskInstance = 0;
var totalScore = [];
var totalTimer = [];
var timerBegin = null;
var done = false;
var currentTask = null;

var remainingImages;
var standardPath = "/";
var standardImageLocation = "../../" + standardPath + "images/task/";
var standardImageFigure = "fig";
var standardImageResolution = "res";
var standardImageEnding = ".jpg";
var beginImage;
var failImage;
var sucessImage;
var focusImage;

var resolutionMapBufferZone = 0.04;


//
// Task data
//
var tasks = 
    [
    {name:"0", solution:"1"},
    {name:"1", solution:"1"},
    {name:"2", solution:"3"},
    {name:"3", solution:"4"},
    {name:"4", solution:"6"},
    {name:"5", solution:"5"},
    {name:"6", solution:"3"},
    {name:"7", solution:"2"},
    {name:"8", solution:"5"},
    {name:"9", solution:"1"},
    {name:"10", solution:"3"},
    {name:"11", solution:"1"},
    {name:"12", solution:"3"},
    {name:"13", solution:"3"},
    {name:"14", solution:"2"},
    {name:"15", solution:"6"}
    ];


//
// Init
//

function start()
{
    console.log("Page setting start");
    canvas_init();
    
    waitScreen();
    preload_images_andStart();
     
    console.log("Page setting end");
}
function canvas_init()
{	
    canvas = document.getElementById('canvas');
    ctx = canvas.getContext("2d");
    clearCanvas();
}
function waitScreen()
{    
    var waitString = "Please wait";
    if (pleaseWaitText !== undefined)
    	waitString = pleaseWaitText();
    
    clearCanvas();
    ctx.beginPath();
    ctx.font = "40px Arial";
    ctx.textAlign = "center";
    ctx.fillText(waitString, canvas.width/2, canvas.height/2); 
    ctx.closePath();
}

function preload_images_andStart() 
{
    remainingImages = 4 + tasks.length;
    
    beginImage = new Image();
    beginImage.src = "../../" + standardPath + "images/ic_play_arrow_black_48px.svg";
    beginImage.onload = onImageLoad;
    failImage = new Image();
    failImage.src = "../../" + standardPath + "images/ic_clear_black_48px.svg";
    failImage.onload = onImageLoad;
    sucessImage = new Image();
    sucessImage.src = "../../" + standardPath + "images/ic_done_black_48px.svg";
    sucessImage.onload = onImageLoad;
    focusImage = new Image();
    focusImage.src = "../../" + standardPath + "images/ic_add_black_48px.svg";
    focusImage.onload = onImageLoad;
    
    for (i = 0; i < tasks.length; i++) 
    {
        tasks[i].figure = new Image();
        tasks[i].figure.src = standardImageLocation + standardImageFigure + i + standardImageEnding;
        tasks[i].figure.onload = onImageLoad;
        tasks[i].resolution = new Image();
        tasks[i].resolution.src = standardImageLocation + standardImageResolution + i + standardImageEnding;
        tasks[i].figure.onload = onImageLoad;
    }
}
function onImageLoad()
{
    remainingImages--;
    if (remainingImages <= 0) {
         startExperiment();
    }
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

function incrementTaskCounter() 
{
	var solvingTaskString = "Solving task";
    if (solvingTaskText !== undefined)
    	solvingTaskString = solvingTaskText();
	
    taskInstance++;
    document.getElementById('taskCounter').innerHTML = solvingTaskString + ": " + taskInstance + " / " + tasks.length;
}

function startTimer(startfunction, time)
{
    currentTimeouts.push(setTimeout(startfunction, time));
}
function clearAllTimers()
{
    for (var i=0; i<currentTimeouts.length; i++)
        clearTimeout(currentTimeouts[i]);
}


/*
 * 
 * Task sequences
 * 
 */

//
// Original screen
//
function startExperiment()
{
    clearAllTimers();
    drawBegin();
    
    canvas.addEventListener("mousedown", beginListener, false);
}
function drawBegin()
{
    clearCanvas();
    var canvasMiddleX = canvas.width / 2;
    var canvasMiddleY = canvas.height / 2;
    var imgWidth = 80;
    var imgHeight = 80;
    drawImage(beginImage, canvasMiddleX - imgWidth/2, canvasMiddleY - imgHeight/2, imgWidth, imgHeight);
}

function beginListener()
{
    canvas.removeEventListener("mousedown", beginListener, false);
    prepareNewTask();
}



//
// Select task
//
function prepareNewTask()
{
    currentTask = selectTask();
    if (currentTask !== null)
        waitBeforeTask();
    else
        finish();
}
function selectTask() 
{
    if (!done && taskInstance < tasks.length)
    {
        // Continue with next task
        incrementTaskCounter();
        return tasks[taskInstance-1];
    }
    else
        return null; // We are out of tasks
}
function waitBeforeTask() 
{
    clearCanvas();
    startTimer(focus, 1000);
}
function focus()
{
    clearCanvas();
    drawFocus();
    
    startTimer(startTask, 2000);
}
function drawFocus()
{
    clearCanvas();
    var canvasMiddleX = canvas.width / 2;
    var canvasMiddleY = canvas.height / 2;
    var imgWidth = 60;
    var imgHeight = 60;
    drawImage(focusImage, canvasMiddleX - imgWidth/2, canvasMiddleY - imgHeight/2, imgWidth, imgHeight);
}

//
// Present figure
//
function startTask() 
{
    drawTaskImages(currentTask);
    
    canvas.addEventListener("mousedown", solutionListener, false);	
    
    timerBegin = new Date().getTime();
}
function presentTask(taskData) 
{
    clearCanvas();
    drawImage(taskData.resolution);
}
function drawTaskImages(taskData)
{
    clearCanvas();
    
    var canvasMiddleWidth = canvas.width/2;
    
    drawImage(taskData.figure, 0, 0, canvasMiddleWidth - 1, canvas.height);
    drawTaskImagesSeparator(canvasMiddleWidth);
    drawImage(taskData.resolution, canvasMiddleWidth + 1, 0, canvasMiddleWidth, canvas.height);
}
function drawTaskImagesSeparator(canvasMiddleWidth)
{
    ctx.beginPath(); 
    ctx.fillRect(canvasMiddleWidth, 0, 2, canvas.height);
    ctx.fill();
    ctx.closePath();
}

function solutionListener(mouseEvent)
{
    var bRect = canvas.getBoundingClientRect();
    var mouseX = (mouseEvent.clientX - bRect.left)*(canvas.width/bRect.width);
    var mouseY = (mouseEvent.clientY - bRect.top)*(canvas.height/bRect.height);

    var figureZoneClick = (mouseX < canvas.width/2 + 2);
    if (figureZoneClick)
        return;

    var sextant = claculateSextant(mouseX, mouseY, canvas.width/2 + 2, canvas.width / 2, canvas.height);
    if (sextant !== null)
        finishTask(sextant);
}
function claculateSextant(pointX, pointY, startX, width, height)
{
    var buffer01 = (pointY > height / 2 - height * resolutionMapBufferZone 
                    && pointY < height / 2 + height * resolutionMapBufferZone);
    var buffer02 = (pointX > startX + width / 3 - width * resolutionMapBufferZone 
                    && pointX < startX + width / 3 + width * resolutionMapBufferZone);
    var buffer03 = (pointX > startX + 2 * width / 3 - width * resolutionMapBufferZone 
                    && pointX < startX + 2 * width / 3 + width * resolutionMapBufferZone);
    if (buffer01 || buffer02 || buffer03)
        return null;
    
    var sextant = 0;
    if (pointY > height / 2)
        sextant += 3;
    
    if (pointX < startX + width / 3)
        sextant += 1;
    else if (pointX < startX + 2 * width / 3)
        sextant += 2;
    else
        sextant += 3;
    
    return sextant;
}

//
// Finish task
//
function finishTask(sextant)
{
    canvas.removeEventListener("mousedown", solutionListener, false);	
    
    totalTimer.push(new Date().getTime() - timerBegin);
    console.log(totalTimer);
    timerBegin = null;
    
    resolveSolution(sextant, currentTask);
    
    startTimer(prepareNewTask, 1000);
}
function resolveSolution(sextant, taskData)
{
    if (sextant == taskData.solution)
        sucessfulSolution();
    else
        failedSolution();
}
function sucessfulSolution()
{
    totalScore.push(true);
    drawSolution(sucessImage);
}
function failedSolution()
{
    totalScore.push(false);
    drawSolution(failImage);
}
function drawSolution(image)
{
    clearCanvas();
    var canvasMiddleX = canvas.width / 2;
    var canvasMiddleY = canvas.height / 2;
    var imgWidth = 80;
    var imgHeight = 80;
    drawImage(image, canvasMiddleX - imgWidth/2, canvasMiddleY - imgHeight/2, imgWidth, imgHeight);
}

//
// Finish the experiment
//
function finish()
{
    clearAllTimers();
    done = true;
    
	var resultString = "Result";
    if (resultText !== undefined)
    	resultString = resultText();
	var averageString = "Average time per figure";
    if (averageText !== undefined)
    	averageString = averageText();
    
    var summedScore = 0;
    for (score in totalScore) {
    	console.log("score", score);
    	if (totalScore[score]) summedScore++;
    }
    var summedTimer = 0;
    for (timer in totalTimer) {
    	console.log("timer", timer);

    	summedTimer += totalTimer[timer];
    }

    var totalResultString = resultString + ":  " + summedScore + " / " + tasks.length;
    var averageTimeString = averageString + ":  " + Math.floor(summedTimer / tasks.length) + " ms";
    
    clearCanvas();
    ctx.beginPath();
    ctx.font = "40px Arial";
    ctx.textAlign = "center";
    ctx.fillText(totalResultString, canvas.width/2, canvas.height/2 - 23); 
    ctx.fillText(averageTimeString, canvas.width/2, canvas.height/2 + 23); 
    ctx.closePath();
    
    var greenButtonContainer = document.getElementById("sucessButton");
    var greenButton = document.getElementById("button-done");
    greenButtonContainer.style.display = 'inline';
    greenButton.title = "Finish";
}


//
// Testing
// This function is strictly for testing purposes
//
function limitTasks(numberOfTasks)        
{
	tasks = tasks.slice(0, numberOfTasks);
}


        