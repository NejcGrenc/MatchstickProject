function Timer(domElementInput)
{
	var domElement = domElementInput;
	
	var previousTime = 0;
	if (timer_presetTimeValue != null)
		previousTime = timer_presetTimeValue;
	
	var startTime = null;
	var endTime = null;
	
	var toString = function()
	{
		var distance = totalTime();
		
		var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
		var seconds = Math.floor((distance % (1000 * 60)) / 1000);
		var millis  = Math.floor((distance % (1000)) / 100);
		
		var str = "";
		str += (minutes < 10) ? "0" : "";
		str += minutes + ":";
		str += (seconds < 10) ? "0" : "";
		str += seconds + ",";
		str += millis;
		return str;
	};
	
	var totalTime = function()
	{
		var distance;
		if (startTime == null)
			distance = 0;
		else if (endTime != null)
			distance = endTime - startTime;
		else
			distance = currMoment() - startTime;
		distance += previousTime;
		return distance;
	};
	
	var currMoment = function()
	{
		return new Date().getTime();
	};
	
	
	// Public
	this.now = function()
	{
		return currMoment();
	};
	this.startTimer = function()
	{
		startTime = this.now();
	};
	this.stopTimer = function()
	{
		endTime = this.now();
	};
	this.continueTimer = function()
	{
		if (startTime == null) { this.startTimer(); return; }
		if (endTime == null) { return; }
		
		previousTime += (endTime - startTime);
		endTime = null;
		this.startTimer();
	};
	
	this.printTime = function()
	{
		domElement.innerHTML = toString();
	};
	this.time = function()
	{
		return toString();
	}
	this.timeNumber = function()
	{
		return totalTime();
	}
	
	this.run = function()
	{
		window.setInterval(this.printTime, 100);
	};
}


var timer_presetTimeValue = null;
var timer_presetTime = function(value)
{
	console.log("Setting timer", value);
	timer_presetTimeValue = Number(value);
};


