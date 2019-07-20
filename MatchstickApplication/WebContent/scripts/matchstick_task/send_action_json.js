	
// Default forward URL
var forward_url = "/matchstickTask";

function setForwardUrl(newUrl)
{
	forward_url = newUrl;
}



function onSucessSend()
{
    console.log();
	console.log("Done");
	waitScreen();

    var data = mapAllActionsToJson();
    var totalTime = timer.timeNumber();
    post(forward_url, {task_data: JSON.stringify({task_number: taskSequenceNumber, time: totalTime, actions: data})});
    
	console.log();

	// For now just
	//reset();
}

function mapAllActionsToJson()
{
    // Get data from action, like action.report()
	var data = [];
    for (act in actionList)
    {
        data.push(mapActionObjectToJson(actionList[act]));
    }
    return data;
}

function mapActionObjectToJson(action)
{
    var data = {};
    data.type = "normal";
    data.startEquation = action.startEquation;
    data.endEquation = action.endEquation;
    data.startMatchstickLoc = action.startMatchstickLoc;
    data.endMatchstickLoc = action.endMatchstickLoc;
    data.startTime = action.startTime;
    data.endTime = action.endTime;
    return data;        
}

function onStopSend()
{
    console.log();
	console.log("Stop");
	waitScreen();
	
	if (currentAction !== undefined && currentAction !== null)
    	endAction(currentAction);
	
    var data = mapAllActionsToJson();
    var pausedata = {};
    pausedata.type = "stop";
    pausedata.startEquation = "";
    if (data === undefined || data.length == 0) {
		// No previous data - add original eqation
    	pausedata.startEquation = originalEquation;
    }
    pausedata.endEquation = "";
    pausedata.startMatchstickLoc = {};
    pausedata.endMatchstickLoc = {};
    pausedata.startTime = 0;
    pausedata.endTime = 0;
    data.push(pausedata);
    
    var totalTime = timer.timeNumber();
    
    post(forward_url, {task_data: JSON.stringify({task_number: taskSequenceNumber, time: totalTime, actions: data})});
    
	console.log(); 	 
}

function onRestartSend()
{
    console.log();
	console.log("Restart");
	waitScreen();
	
	if (currentAction !== undefined && currentAction !== null)
    	endAction(currentAction);
	
    var data = mapAllActionsToJson();
    var restartdata = {};
    restartdata.type = "restart";
    restartdata.startEquation = "";
    if (data === undefined || data.length == 0) {
		// No previous data - add original eqation
    	restartdata.startEquation = originalEquation;
    }
    restartdata.endEquation = "";
    restartdata.startMatchstickLoc = {};
    restartdata.endMatchstickLoc = {};
    restartdata.startTime = 0;
    restartdata.endTime = 0;
    data.push(restartdata);
    
    var totalTime = timer.timeNumber();
    
    post(forward_url, {task_data: JSON.stringify({task_number: taskSequenceNumber, time: totalTime, actions: data})});
    
	console.log(); 	 
}

