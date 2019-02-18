
//
// Matchstick Shadow
//

var shadowColor = "#EEEEEE";

function Shadow(centerPoint, angle)
{
    this.point = centerPoint;
    this.angle = (angle === undefined || typeof angle === 'undefined') ? 0 : angle;
    this.containedMatchstick = null;
    this.permanentMatchstick = false;
   
    this.getWidth = function()
    {
        return matchstick_standard_width * 1.05;
    };
    this.getHeight = function()
    {
        return matchstick_standard_height * 1.05;
    };

    this.containsMatchstick = function()
    {
        return this.containedMatchstick !== null;
    };
    this.insertMatchstick = function(matchstick)
    {
        this.containedMatchstick = matchstick;
    };
    this.removeMatchstick = function()
    {   
        this.containedMatchstick = null;
    };
    this.getMatchstick = function()
    {
        return this.containedMatchstick;
    };
    
    this.setMatchstickPermanent = function(permanent)
    {
        this.permanentMatchstick = permanent;
    };
    this.addNewMatchstick = function()
    {
        // New matchstick
        var matchstick = new Matchstick(new Point(0, 0));
        addMatchstick(matchstick);
        
        this.insertMatchstick(matchstick);
        matchstick.embeddedShadow = this;
        fullyEmbedToShadow(matchstick);
    };
}

function pointInShadow(point, shadow)
{
    var angleSin = Math.sin(shadow.angle * Math.PI/180);
    var angleCos = Math.cos(shadow.angle * Math.PI/180);
    var width = shadow.getWidth();
    var height = shadow.getHeight();
    if(
         angleCos * (point.x - shadow.point.x) + angleSin * (point.y - shadow.point.y) >= -width / 2  &&
         angleCos * (point.x - shadow.point.x) + angleSin * (point.y - shadow.point.y) <=  width / 2  &&
        -angleSin * (point.x - shadow.point.x) + angleCos * (point.y - shadow.point.y) >= -height / 2 &&
        -angleSin * (point.x - shadow.point.x) + angleCos * (point.y - shadow.point.y) <=  height / 2  
       )    
    {
        return true;
    }
    return false;
}

function getShadowByLocation(currPoint, allShadows)
{
    for (var x in allShadows)
    {
        var shadow = allShadows[x];
        if (pointInShadow(currPoint, shadow))
        {
            return shadow;
        }
    }
    return null;
}

CanvasRenderingContext2D.prototype.drawShadow = function (shadow)
{
    // Create a matchstick image
    var shadowTopLeft = new Point( -shadow.getWidth()/2, -shadow.getHeight()/2 );
    
    // Prepare for drawing
    this.save(); // saves the coordinate system
    this.translate(shadow.point.x, shadow.point.y); // now the position (0,0) is found at (x, y)
    this.rotate(shadow.angle * Math.PI/180); // rotate around the start point
    this.beginPath();
    
    // Draw shadow
    this.fillStyle = shadowColor;
    this.fillRect(shadowTopLeft.x, shadowTopLeft.y, shadow.getWidth(), shadow.getHeight());
    this.fill();		
    
    // Finish drawing
    this.closePath();
    this.restore(); // Restore canvas to last saved state
    //ctx.drawImage(matchstick.image, matchstick.point.x, matchstick.point.y, matchstick.width, matchstick.height);
};

function getStandardizedShadowPositionInEquationFrame(shadow, equationFrame)
{
	var shadowFrameNo = equationFrame.findFrameForShadow(shadow);
	var shadowFrame = equationFrame.getFrame(shadowFrameNo);
	var shadowPos = shadowFrame.findShadowPosNumber(shadow);
	
	var shadowFrameType = null;
	if (shadowFrame instanceof NumeralFrame) {shadowFrameType = "N"}
	else if (shadowFrame instanceof OperatorFrame) {shadowFrameType = "O"}
	else if (shadowFrame instanceof ComparatorFrame) {shadowFrameType = "C"}
	
	var shadowPosData = {
			posShadowInFrame: shadowPos,
			posFrameInEquation: shadowFrameNo,
			frameType: shadowFrameType
	}
	return shadowPosData;
}


//
// Equation standard
//

var eqautionElement_standardWidth = 90;
var equationElement_standardHeight = 170;

//
// Matchstick Numeral frame
//

function NumeralFrame(centerPoint, width, height)
{
    this.point = centerPoint;
    this.shadows = [];
    this.addShadow = function(pos, shadow)
    {
        this.shadows[pos] = shadow;
    };
    
    if (width === undefined || typeof width === 'undefined')
    {
        this.width = eqautionElement_standardWidth;
        this.height = equationElement_standardHeight;
    }
    else
    {
        this.width = width;
        this.height = height;  
    }
    
    
    //  Visual representation of 
    //  the numeral frame
    //      _____
    //   _ |__1__| _
    //  | |       | |
    //  |2|       |3|
    //  |_| _____ |_|
    //   _ |__4__| _
    //  | |       | |
    //  |5|       |6|
    //  |_| _____ |_|
    //     |__7__|
    //
   
    this.mapping = {
        "1110111": "0",
        "0010010": "1",
        "1011101": "2",
        "1011011": "3",
        "0111010": "4",
        "1101011": "5",
        "1101111": "6",
        "1010010": "7",
        "1111111": "8",
        "1111011": "9"
    };
   
    this.addShadow(1, new Shadow(new Point(this.point.x, this.point.y - this.height*0.4), 270));
    this.addShadow(2, new Shadow(new Point(this.point.x - this.width*0.4, this.point.y - this.height*0.2), 0));
    this.addShadow(3, new Shadow(new Point(this.point.x + this.width*0.4, this.point.y - this.height*0.2), 0));
    this.addShadow(4, new Shadow(this.point, 270));
    this.addShadow(5, new Shadow(new Point(this.point.x - this.width*0.4, this.point.y + this.height*0.2), 0));
    this.addShadow(6, new Shadow(new Point(this.point.x + this.width*0.4, this.point.y + this.height*0.2), 0));
    this.addShadow(7, new Shadow(new Point(this.point.x, this.point.y + this.height*0.4), 270));
    
    addShadows(this.shadows);
    
    
    this.toElementString = function()
    {
        var tmpString = "";
        for (var x in this.shadows)
        {
            tmpString += (this.shadows[x].containsMatchstick()) ? "1" : "0";
        }
        var result = this.mapping[tmpString];
        return (result !== undefined && typeof result !== 'undefined') ? result : "?";
    };
    
    this.findShadowPosNumber = function(shadow)
    {
    	for (var i = 1; i <= this.shadows.length; i++)
    		if (shadow == this.shadows[i])
    			return i;
    	return null;
    };
}


//
// Matchstick Operator frame
//

function OperatorFrame(centerPoint, width, height)
{
    this.point = centerPoint;
    this.shadows = [];
    this.addShadow = function(pos, shadow)
    {
        this.shadows[pos] = shadow;
    };
    
    if (width === undefined || typeof width === 'undefined')
    {
        this.width = eqautionElement_standardWidth;
        this.height = equationElement_standardHeight;
    }
    else
    {
        this.width = width;
        this.height = height;  
    }


    //  Visual representation of 
    //  the operator frame
    //      
    //       _1  
    //    /\| |/\ 3
    //   _\_|_|_/_ 
    //  |_________| 4
    //    / | | \
    //    \/|_|\/ 2
    //
    
    this.mapping = {
        "1001": "+",
        "0001": "-",
        "0110": "*",
        "0010": "/"
    };
    
    this.addShadow(1, new Shadow(this.point,   0));
    this.addShadow(2, new Shadow(this.point, 315));     
    this.addShadow(3, new Shadow(this.point,  45));
    this.addShadow(4, new Shadow(this.point, 270));
    
    addShadows(this.shadows);
    
    
    this.toElementString = function()
    {
        var tmpString = "";
        for (var x in this.shadows)
        {
            tmpString += (this.shadows[x].containsMatchstick()) ? "1" : "0";
        }
        var result = this.mapping[tmpString];
        return (result !== undefined && typeof result !== 'undefined') ? result : "?";
    };
    
    this.findShadowPosNumber = function(shadow)
    {
    	for (var i = 1; i <= this.shadows.length; i++)
    		if (shadow == this.shadows[i])
    			return i;
    	return null;
    };
}


//
// Matchstick Comparator frame
//

function ComparatorFrame(centerPoint, width, height)
{
    this.point = centerPoint;
    this.shadows = [];
    this.addShadow = function(pos, shadow)
    {
        this.shadows[pos] = shadow;
    };

    if (width === undefined || typeof width === 'undefined')
    {
        this.width = eqautionElement_standardWidth;
        this.height = equationElement_standardHeight;
    }
    else
    {
        this.width = width;
        this.height = height;  
    }
    
    //  Visual representation of 
    //  the comparator frame 
    //   _________
    //  |_________| 1
    //   _________
    //  |_________| 2
    //
    
    
    var shadow_1 = new Shadow(new Point(this.point.x, this.point.y - this.height*0.05), 270);
    shadow_1.addNewMatchstick();
    shadow_1.setMatchstickPermanent(true);
    var shadow_2 = new Shadow(new Point(this.point.x, this.point.y + this.height*0.05), 270);
    shadow_2.addNewMatchstick();    
    shadow_2.setMatchstickPermanent(true);
            
    this.addShadow(1, shadow_1);
    this.addShadow(2, shadow_2);
    
    addShadows(this.shadows);
    
    this.toElementString = function()
    {
        return "=";
    };
    
    this.findShadowPosNumber = function(shadow)
    {
    	for (var i = 1; i <= this.shadows.length; i++)
    		if (shadow == this.shadows[i])
    			return i;
    	return null;
    };
}


//
// Equation frame
//

function EquationFrame(centerPoint, structure)
{
    this.point = centerPoint;
    this.frameList = [];
    this.addFrame = function(pos, frame)
    {
        this.frameList[pos] = frame;
    };
    this.getFrame = function(pos)
    {
    	return this.frameList[pos];
    };
    
    this.equationStructure = [];
    this.set = function (structure)
    {
        var equationFill = [];
        if (structure === "default")
        {
            this.equationStructure = ['N', 'O', 'N', 'C', 'N'];
            equationFill =           ["e", "e", "e", "=", "e"];  // 'e' stands for empty
        }
        else
        {
            for (var x in structure)
            {
                var symbol = structure[x];
                if (["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"].indexOf(symbol) >= 0)
                    this.equationStructure.push('N');
                else if (["+", "-", "*", "/"].indexOf(symbol) >= 0)
                    this.equationStructure.push('O');
                else if ("=" === symbol)
                    this.equationStructure.push('C');
                else 
                {
                    console.log("Unknown symbol", symbol);
                    return;
                }
                equationFill.push(symbol);    
            }
        }
        
        var numberOfElements = this.equationStructure.length;
        var stnadardElementWidth = eqautionElement_standardWidth;
        var spaceBetweenElements = stnadardElementWidth * 0.3;

        if (numberOfElements === 0)
        {
            console.log("Number of equaiton elements must be bigger than 0");
        }   
        // Width of n elements and (n-1) spaces between them
        var totalWidth = numberOfElements * (stnadardElementWidth + spaceBetweenElements) - spaceBetweenElements;
        var leftEdge = centerPoint.x - (totalWidth / 2);

        for (var i = 0; i < numberOfElements; i++)
        {
            var elementCenterX = leftEdge + i * (stnadardElementWidth + spaceBetweenElements) + stnadardElementWidth/2;
            var elementType = this.equationStructure[i];
            if (elementType === 'N')
            {
                var frame = new NumeralFrame(new Point(elementCenterX, centerPoint.y));
                this.addFrame(i, frame);
                if (equationFill[i] !== "e") 
                    fillFrame(frame, equationFill[i]);
            }
            else if (elementType === 'O')
            {
                var frame = new OperatorFrame(new Point(elementCenterX, centerPoint.y));
                this.addFrame(i, frame);
                if (equationFill[i] !== "e") 
                    fillFrame(frame, equationFill[i]);
            }
            else if (elementType === 'C')
            {
                var frame = new ComparatorFrame(new Point(elementCenterX, centerPoint.y));
                this.addFrame(i, frame);
                //fillFrame(frame, equationFill[i]);
            }
            else
            {
                console.log("Unindetified element type: " + elementType);
            }
        }
    };
    this.set(structure);

    this.toString = function()
    {
        var str = "";
        for (var i = 0; i < this.equationStructure.length; i++)
        {
            str += this.frameList[i].toElementString();
        }
        return str;
    };
    
    this.findFrameForShadow = function(shadow)
    {
    	for (var i = 0; i < this.frameList.length; i++)
    		if (this.frameList[i].findShadowPosNumber(shadow) !== null)
    			return i;
    	return null;
    };
}

function fillFrame(frame, stringElement)
{
    var sequence = getKeyByValue(frame.mapping, stringElement);
    if (sequence === null)
    {
        console.log("Non-existet symbol:", stringElement, "in", frame.mapping);
        return;
    }

    for (var i = 0; i < frame.shadows.length - 1; i++)
    {
        var shadowNo = i+1;
        if (sequence[i] === "1")
        {
            if (! frame.shadows[shadowNo].containsMatchstick())
                frame.shadows[shadowNo].addNewMatchstick();
        }
        else
        {
            if (frame.shadows[shadowNo].containsMatchstick())
            {
                var currMatch = frame.shadows[shadowNo].getMatchstick();
                frame.shadows[shadowNo].removeMatchstick();
                removeMatchstick(currMatch);
            }
        }
    }
};
// Dictionary helper
function getKeyByValue(dict, value)
{
    for (var key in dict) 
    {
        if (dict.hasOwnProperty(key)) 
        {
            if(dict[key] === value)
                return key;
        }
    }
    return null;
};

function createRandomEquation()
{
    /* Sequences */
    var numerals = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
    var operators = ["+", "-", "*", "/"];
    var comparator = "=";
    
    var equation = "";
    equation += numerals[Math.floor((Math.random() * 10))];
    equation += operators[Math.floor((Math.random() * 4))];
    equation += numerals[Math.floor((Math.random() * 10))];
    equation += comparator;
    equation += numerals[Math.floor((Math.random() * 10))];
    return equation;
}