

var canvas;
var ctx;

var currentAction;
var actionList = [];

var restrictionOnlyOne = false;


//
// Main function
//

function canvas_init()
{	
    canvas = document.getElementById('canvas');
    ctx = canvas.getContext("2d");
    
    currentAction = null;
    
    canvas.addEventListener("mousedown", mouseDownListener, false);		
    
    refreshCanvas();

}

function refreshCanvas()
{			
    ctx.beginPath();
    ctx.clearRect ( 0 , 0 , canvas.width, canvas.height );
    ctx.closePath();
    
    var allShadows = getAllShadows();
    for (var x in allShadows)
    {
        ctx.drawShadow(allShadows[x]);
    }
    
    var allMatchsticks = getAllMatchsticks();
    for (var x in allMatchsticks)
    {
        ctx.drawMatchstick(allMatchsticks[x]);
    }
    
    if (typeof drawAdditionalElements === "function")
    	drawAdditionalElements();
}

function calculateEquation()
{
    var greenButtonContainer = document.getElementById("sucessButton");
    var greenButton = document.getElementById("button-done");
    var redButtonContainer = document.getElementById("failureButton");
    var redButton = document.getElementById("button-notDone");
    var redButtonRestartContainer = document.getElementById("failureRestartButton");
    var redButtonRestart = document.getElementById("button-notDoneRestart");
    var done = false;
    
    var eqStr = equation.toString();
    var addInfo = "";
    
    var noActionInProgress = (currentAction === null);
    if (noActionInProgress)
    {
        var calc = new Calculator(eqStr);
        if (calc.isValid())
        {
            if (calc.isCorrect())
            {
                done = true;
                
                addInfo = "is correct.";
                if (tooltipSolvedText !== undefined) addInfo = tooltipSolvedText();
            }
            else
            {
                addInfo = "is valid, but not correct.";
                if (tooltipOnlyValidText !== undefined) addInfo = tooltipOnlyValidText();
            }
        }
    }
    
    var equationString = "Equation";
    if (tooltipEquationText !== undefined) equationString = tooltipEquationText();

    var tooltip = equationString + ":  " + eqStr + "  " + addInfo;
    if (done) {
    	timer.stopTimer();
    	
        greenButtonContainer.style.display = 'inline';
        redButtonContainer.style.display = 'none';
        redButtonRestartContainer.style.display = 'none';
        
        greenButton.title = "Contine to next equation";
        if (tooltipContinueText !== undefined)  greenButton.title = tooltipContinueText();
    }
    else if (restrictionOnlyOne && actionList.length === 1)
    {
    	timer.stopTimer();
    	blockAll();
    	
        greenButtonContainer.style.display = 'none';
        redButtonContainer.style.display = 'none';
        redButtonRestartContainer.style.display = 'inline';
        redButtonRestart.title = "Restart";
    }
    else 
    {
    	timer.continueTimer();
    	
        greenButtonContainer.style.display = 'none';
        redButtonContainer.style.display = 'inline';
        redButtonRestartContainer.style.display = 'none';
        redButton.title = tooltip;
    }
}

function blockAllExceptStartAndEndOnes()
{
	var lastAction = actionList[0];
	var matchStart = lastAction.startMatchstickLoc;
	var matchEnd   = lastAction.endMatchstickLoc;
	
	// block all shadows except start and end plan ones
	for (var i = 0; i < allShadows.length; i++)
	{
		if (i == matchStart || i == matchEnd)
			continue;
		allShadows[i].setBlocked(true);
	}	
}

function blockAll()
{
	for (var i = 0; i < allShadows.length; i++)
		allShadows[i].setBlocked(true);
}

function unblockAll()
{
	for (var i = 0; i < allShadows.length; i++)
		allShadows[i].setBlocked(false);	
}



//
// Point
//

function Point(locX, locY)
{
    this.x = locX;
    this.y = locY;
}


//
// Action
//

function Action()
{
    // Allowed action types are
    // "grab" & "drag"
    this.type = null;
    this.matchstick;
    
    this.startEquation;
    this.endEquation;
    this.startMatchstickLoc;
    this.endMatchstickLoc;
    this.startTime;
    this.endTime;

    this.report = function()
    {
    	var duration = this.endTime - this.startTime;
    	var startMatchstickCode = "" + this.startMatchstickLoc.posFrameInEquation + this.startMatchstickLoc.frameType + 
    							  "-" + this.startMatchstickLoc.posShadowInFrame;
    	var endMatchstickCode = "" + this.endMatchstickLoc.posFrameInEquation + this.endMatchstickLoc.frameType + 
								"-" + this.endMatchstickLoc.posShadowInFrame;
        var reportStr = "Action " + this.type + " took " + duration + " ms.\n" +
        				"It moved matchstick from " + startMatchstickCode + " to " +  endMatchstickCode + ".\n" +
        				"It transformed the equation from " + this.startEquation + " to " + this.endEquation + ".";
        return reportStr;
    };
    
}

function startNewAction(matchstick)
{
	var newAction = new Action();
	newAction.matchstick = matchstick;
	
	newAction.startEquation = equation.toString();
	newAction.startTime = new Date().getTime();
	    	
	var currentShadow = matchstick.embeddedShadow;
	var shadowLoc = getStandardizedShadowPositionInEquationFrame(currentShadow, equation);
	newAction.startMatchstickLoc = shadowLoc;
	
	return newAction;
}

function endAction(action)
{
	action.endEquation = equation.toString();
	action.endTime = new Date().getTime();
	
	var currentShadow = action.matchstick.embeddedShadow;
	var shadowLoc = getStandardizedShadowPositionInEquationFrame(currentShadow, equation);
	action.endMatchstickLoc = shadowLoc;
}



//
// Listeners
//

function mouseDownListener(mouseEvent) 
{
    var mousePoint = getMousePoint(mouseEvent);
    
    // Is grab action underway
    if (currentAction !== null)
    {
    	// if the matchstick is not embeded,
    	// count mouse down as rotate
		var currentMatchstick = currentAction.matchstick;
		clampToCanvas(mousePoint, currentMatchstick);
		if (! currentMatchstick.isEmbedded())
		{
			 rotate(currentMatchstick, 1 * 45);
			 refreshCanvas();
		}
		
        canvas.removeEventListener("mousedown", mouseDownListener, false);
        window.addEventListener("mouseup", mouseUpListener, false);
    }
    // Is new matchstick action
    else if (isClickedOnMatchstick(mousePoint))
    {				
        var currentMatchstick = getMatchstickByLocation(mousePoint, getAllMatchsticks());
        currentAction = startNewAction(currentMatchstick);

        canvas.removeEventListener("mousedown", mouseDownListener, false);
        window.addEventListener("mousemove", mouseMoveListener, false);
        window.addEventListener("mouseup", mouseUpListener, false);
        window.addEventListener('DOMMouseScroll', mouseScrollListener, false); // Firefox
        window.addEventListener('mousewheel', mouseScrollListener, false); // Others (Chrome)
        
    }	
}

function mouseMoveListener(mouseEvent)
{
    var mousePoint = getMousePoint(mouseEvent);
    
    // Upon receiving move event first
    // set action type as drag
    if (currentAction.type === null)
    {
        currentAction.type = "drag";
    }
    
    var currentMatchstick = currentAction.matchstick;
    clampToCanvas(mousePoint, currentMatchstick);
    centerMatchstickToPoint(mousePoint, currentMatchstick);
    
    if (currentMatchstick.isEmbedded())
    {
        // Is moved outside of the current shadow?
        if (! pointInShadow(mousePoint, currentMatchstick.embeddedShadow))
        {
            // Clean the shadow & Matchstick is not embedded
            currentMatchstick.embeddedShadow.removeMatchstick();
            currentMatchstick.embeddedShadow = null;
        }
    }
    else 
    {
        // Is hovering over new shadow?
        var currentShadow = getDroppedToShadow(mousePoint);
        if (currentShadow !== null)
        {       
            // Drop into shadow
            currentShadow.insertMatchstick(currentMatchstick);
            currentMatchstick.embeddedShadow = currentShadow;
        }
    }

    refreshCanvas();
}

function mouseUpListener(mouseEvent)
{		
    var mousePoint = getMousePoint(mouseEvent);
    
    // Upon receiving up event first
    // set action type as grab
    if (currentAction.type === null)
    {
        currentAction.type = "grab";
    }
    
    // The drag action is under way
    // Try to end the action
    else
    {
        var currentMatchstick = currentAction.matchstick;
        clampToCanvas(mousePoint, currentMatchstick);

        // Fully embed matchstick
        // Action is finished
        if (currentMatchstick.isEmbedded())
        {
            fullyEmbedToShadow(currentMatchstick);
            
            endAction(currentAction);

            // Store the action
            console.log("Action report", currentAction.report());
            actionList.push(currentAction);
            
            currentAction = null;

            window.removeEventListener("mousemove", mouseMoveListener, false);
            window.removeEventListener('DOMMouseScroll', mouseScrollListener, false); // Firefox
            window.removeEventListener('mousewheel', mouseScrollListener, false); // Others (Chrome)
            
        }
        // If it is not finished
        // Change action to grab and continue
        else
        {
        	currentAction.type = "grab";
        }
    }
    
    window.removeEventListener("mouseup", mouseUpListener, false);
    canvas.addEventListener("mousedown", mouseDownListener, false);
    
    refreshCanvas();
    calculateEquation();
}

function mouseScrollListener(mouseEvent)
{
    // Rotate matchstick in desired direction
    var degrees = 45;
	
    var deltaFirefox = mouseEvent.detail; // Firefox
	var scrollDirectionFirefox = (deltaFirefox === 0) ? 0 : ((deltaFirefox > 0) ? 1 : -1); 

	var delta = mouseEvent.wheelDelta; // Chrome
	var scrollDirectionChrome = (delta === 0) ? 0 : ((delta > 0) ? -1 : 1); 
  
	var scrollDirection = 0; // Combined scroll direction
	if (deltaFirefox !== 'undefined' && deltaFirefox !== 0)
	{
		scrollDirection = scrollDirectionFirefox;
	}
	else if (delta !== 'undefined' && delta !== 0)
	{
		scrollDirection = scrollDirectionChrome;
	}
	
	// Rotate
	rotate(currentAction.matchstick, scrollDirection * degrees);

    refreshCanvas();

    // Prevent default action (page scroll)
    mouseEvent.preventDefault();
    return false; 
}

function getMousePoint(mouseEvent)
{
    var bRect = canvas.getBoundingClientRect();
    var mouseX = (mouseEvent.clientX - bRect.left)*(canvas.width/bRect.width);
    var mouseY = (mouseEvent.clientY - bRect.top)*(canvas.height/bRect.height);
    return new Point(mouseX, mouseY);
}

//clamp x and y positions to prevent object from dragging outside of canvas
function clampToCanvas(mouse, matchstick)
{
    var marginX = matchstick.width / 2;
    var marginY = matchstick.height / 2;
    mouse.x = (mouse.x - marginX < 0) ? marginX : ((mouse.x + marginX > canvas.width) ? (canvas.width - marginX) : mouse.x);
    mouse.y = (mouse.y - marginY < 0) ? marginY : ((mouse.y + marginY > canvas.height) ? (canvas.height - marginY) : mouse.y);
}


function isClickedOnMatchstick(mousePoint)
{
    return (getMatchstickByLocation(mousePoint, getAllAvailableMatchsticks()) !== null);
}

function getDroppedToShadow(mousePoint)
{
    return getShadowByLocation(mousePoint, getAllEmptyShadows());
}
