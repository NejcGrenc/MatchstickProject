
//
// Init
//


var canvasWidth;
var canvasHeight;

var allMatchsticks = [];
var allShadows = [];
var equation;


function start()
{
    console.log("Page setting start");

    canvasWidth = 1000;
    canvasHeight = 500;
    canvas_init();

    setUpEquation();
    
    //resize();
    refreshCanvas();
    calculateEquation();
    
    console.log("Page setting end");
}

function setUpEquation()
{
    allMatchsticks = [];
    allShadows = [];
    equation = null;

    // Random equation
    // var newEquationString = createRandomEquation();
    
    // Get from servlet
    var newEquationString = originalEquation; 
    
    equation = new EquationFrame(new Point(canvasWidth / 2, canvasHeight / 2), newEquationString);
}

function reset()
{
    console.log("Page reset");
    
    setUpEquation();
        
    refreshCanvas();
    calculateEquation();
}

function resize() 
{
    var canvas = document.getElementById('canvas');
    var canvasRatio = canvas.height / canvas.width;
    var windowRatio = window.innerHeight / window.innerWidth;
    // Perhaps needed for mobile:  window.devicePixelRatio
    var width;
    var height;

    if (windowRatio < canvasRatio) {
        height = window.innerHeight;
        width = height / canvasRatio;
    } else {
        width = window.innerWidth;
        height = width * canvasRatio;
    }

    canvas.style.width = width / 2 + 'px';
    canvas.style.height = height / 2 + 'px';
};


function addMatchstick(matchstick)
{
    allMatchsticks.push(matchstick);
}
function removeMatchstick(matchstick)
{
    var index = allMatchsticks.indexOf(matchstick);
    if (index > -1) {
        allMatchsticks.splice(index, 1);
    }
}
function getAllMatchsticks()
{
    return allMatchsticks;
}
function getAllAvailableMatchsticks()
{
    var allAvailableMatchsticks = [];
    for (var x in allMatchsticks)
    {
        var matchstick = allMatchsticks[x];
        if (! matchstick.isPermanentlyEmbedded())
        {
            allAvailableMatchsticks.push(matchstick);
        }
    }
    return allAvailableMatchsticks;
}



function addShadows(shadows)
{
    for (var x in shadows)
    {
        addShadow(shadows[x]);
    }
}
function addShadow(shadow)
{
    allShadows.push(shadow);
}
function getAllShadows()
{
    return allShadows;
}
function getAllEmptyShadows()
{
    var allEmptyShadows = [];
    for (var x in allShadows)
    {
        var shadow = allShadows[x];
        if (! shadow.containsMatchstick())
        {
            allEmptyShadows.push(shadow);
        }
    }
    return allEmptyShadows;
}
