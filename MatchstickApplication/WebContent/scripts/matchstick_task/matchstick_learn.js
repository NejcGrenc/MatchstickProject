
/*
 * This function is used for drawing matchstick_learn specific items.
 * It is by default called on every canvas refresh.
 */
function drawAdditionalElements()
{
	if (typeof allShadows !== 'undefined' && allShadows.length > 0)
	{
		if (currentAction == null && (! new Calculator(equation.toString()).isCorrect()))
			drawArrowToShadow(allShadows[startShadowPlanValue], startShadowPlanDirectionRight);
		else if (currentAction !== null)
			drawArrowToShadow(allShadows[endShadowPlanValue], endShadowPlanDirectionRight);
	}
}

/*
 * Mandatory values
 */
var startShadowPlanValue;
var startShadowPlanDirectionRight;
var endShadowPlanValue;
var endShadowPlanDirectionRight;


// This is hard-coded for equation style NONONCN 
var matchsticksPerElements = [7,4,7,4,7,2,7];
function shadowValue(sequentialElementNo, sequentialMatchstickInElementNo)
{
	var shadowValue = 0;
	for (x in matchsticksPerElements)
	{
		if (x < sequentialElementNo)
			shadowValue += matchsticksPerElements[x];
		else
		{
			shadowValue += sequentialMatchstickInElementNo;
			break;
		}
	}
	return shadowValue;
}

function setPlanStartShadow(sequentialElementNo, sequentialMatchstickInElementNo, directionRight)
{
	startShadowPlanValue = shadowValue(sequentialElementNo, sequentialMatchstickInElementNo);
	startShadowPlanDirectionRight = directionRight;
}
function setPlanEndShadow(sequentialElementNo, sequentialMatchstickInElementNo, directionRight)
{
	endShadowPlanValue = shadowValue(sequentialElementNo, sequentialMatchstickInElementNo);
	endShadowPlanDirectionRight = directionRight;
}



function onInit()
{
	console.log("On init - learn");
	
	// block all shadows except start and end plan ones
	for (var i = 0; i < allShadows.length; i++)
	{
		if (i == startShadowPlanValue || i == endShadowPlanValue)
			continue;
		allShadows[i].setBlocked(true);
	}
}


function drawArrowToShadow(shadow, revert)
{
	var center_x = shadow.point.x;
	var center_y = shadow.point.y;

	if (revert)
		drawSimpleArrow(ctx, center_x+100, center_y-100, center_x+20, center_y-20);
	else
		drawSimpleArrow(ctx, center_x-100, center_y-100, center_x-20, center_y-20);
}

function drawSimpleArrow(ctx, fromx, fromy, tox, toy){
    //variables to be used when creating the arrow
    var headlen = 10;
    var lineWidth = 12;
    
    var angle = Math.atan2(toy-fromy, tox-fromx);
    var arrowColor = "#cc0000"

    //starting path of the arrow from the start square to the end square and drawing the stroke
    ctx.beginPath();
    ctx.moveTo(fromx, fromy);
    ctx.lineTo(tox, toy);
    ctx.strokeStyle = arrowColor;
    ctx.lineWidth = lineWidth;
    ctx.stroke();

    //starting a new path from the head of the arrow to one of the sides of the point
    ctx.beginPath();
    ctx.moveTo(tox, toy);
    ctx.lineTo(tox-headlen*Math.cos(angle-Math.PI/7),toy-headlen*Math.sin(angle-Math.PI/7));

    //path from the side point of the arrow, to the other side point
    ctx.lineTo(tox-headlen*Math.cos(angle+Math.PI/7),toy-headlen*Math.sin(angle+Math.PI/7));

    //path from the side point back to the tip of the arrow, and then again to the opposite side point
    ctx.lineTo(tox, toy);
    ctx.lineTo(tox-headlen*Math.cos(angle-Math.PI/7),toy-headlen*Math.sin(angle-Math.PI/7));

    //draws the paths created above
    ctx.strokeStyle = arrowColor;
    ctx.lineWidth = lineWidth;
    ctx.stroke();
    ctx.fillStyle = arrowColor;
    ctx.fill();
    
    ctx.fillStyle = '#000000';
    ctx.closePath();
}

