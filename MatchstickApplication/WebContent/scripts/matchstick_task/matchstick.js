

var allMatchsticks;

var matchstick_standard_width = 10;
var matchstick_standard_height = 50;

var standard_matchstickImageLocation = "images/matchstick_original.png";
var matchstickStickColor = "#F79447";
var matchstickBallColor = "#D80A0A";

// Old values
//var matchstickStickColor = "#007777";
//var matchstickBallColor = "#FF6699";

//
// Constructor
//

function Matchstick(point, width, height)
{
    this.point = point;
    if (width === undefined || typeof width === 'undefined')
    {
        this.width = matchstick_standard_width;
        this.height = matchstick_standard_height;
    }
    else
    {
        this.width = width;
        this.height = height;  
    }
    this.angle = 0;
    
    this.embeddedShadow = null;
    this.isEmbedded = function()
    {
        return this.embeddedShadow !== null;
    };
    this.isNotEmbedded = function()
    {
        return this.embeddedShadow === null;
    };
    this.isPermanentlyEmbedded = function()
    {
        if (this.isEmbedded() && this.embeddedShadow.permanentMatchstick)
            return true;
    };
}


// Setters

function centerMatchstickToPoint(center, matchstick)
{
//    var x = center.x - (matchstick.width / 2);
//    var y = center.y - (matchstick.height / 2);
//    var newPoint = new Point(x, y);
    matchstick.point = center;
}

function rotate(matchstick, degree)
{
    //matchstick.image.style.transform = "rotate(90deg)";
    matchstick.angle += degree;
    matchstick.angle %= 360;
}


// Functions

function pointInMatchstick(point, matchstick)
{
    var angleSin = Math.sin(matchstick.angle * Math.PI/180);
    var angleCos = Math.cos(matchstick.angle * Math.PI/180);
    if(
         angleCos * (point.x - matchstick.point.x) + angleSin * (point.y - matchstick.point.y) >= -matchstick.width / 2  &&
         angleCos * (point.x - matchstick.point.x) + angleSin * (point.y - matchstick.point.y) <=  matchstick.width / 2  &&
        -angleSin * (point.x - matchstick.point.x) + angleCos * (point.y - matchstick.point.y) >= -matchstick.height / 2 &&
        -angleSin * (point.x - matchstick.point.x) + angleCos * (point.y - matchstick.point.y) <=  matchstick.height / 2  
       )    
    {
        return true;
    }
    return false;
}

function getMatchstickByLocation(currPoint, allMatchsticks)
{
    for (var x in allMatchsticks)
    {
        var matchstick = allMatchsticks[x];
        if (pointInMatchstick(currPoint, matchstick))
        {
            return matchstick;
        }
    }
    return null;
}

function fullyEmbedToShadow(matchstick)
{
    matchstick.point = matchstick.embeddedShadow.point;
    matchstick.angle = matchstick.embeddedShadow.angle;
}


CanvasRenderingContext2D.prototype.drawMatchstick = function (matchstick)
{
    // Prepare location data
    // Use shadow data if matchstick is embedded
    var locX = (matchstick.isNotEmbedded()) ? matchstick.point.x : matchstick.embeddedShadow.point.x;
    var locY = (matchstick.isNotEmbedded()) ? matchstick.point.y : matchstick.embeddedShadow.point.y;
    var ang = (matchstick.isNotEmbedded()) ? matchstick.angle : matchstick.embeddedShadow.angle;
    
    // Create a matchstick image
    var stickWidth = matchstick.width * 0.7;
    var stickHeight = matchstick.height * 0.9;
    var ballRadius = matchstick.width / 2;
    var stickTopLeft = new Point( -stickWidth / 2, -matchstick.height * 0.4);
    var ballCenter = new Point(0 , -matchstick.height / 2 + ballRadius);                             
    
    // Prepare for drawing
    this.save(); // saves the coordinate system
    this.translate(locX, locY); // now the position (0,0) is found at (x, y)
    this.rotate(ang * Math.PI/180); // rotate around the start point
    this.beginPath();
    
    // Draw matchstick stick
    this.fillStyle = matchstickStickColor;
    this.fillRect(stickTopLeft.x, stickTopLeft.y, stickWidth, stickHeight);
    this.fill();	
 
    // Draw matchstick ball
    this.fillStyle = matchstickBallColor;
    this.moveTo(ballCenter.x, ballCenter.y);
    this.arc(ballCenter.x, ballCenter.y, ballRadius, 0, Math.PI*2, true);
    this.fill();	
    
    // Finish drawing
    this.closePath();
    this.restore(); // Restore canvas to last saved state
    //ctx.drawImage(matchstick.image, matchstick.point.x, matchstick.point.y, matchstick.width, matchstick.height);
};

