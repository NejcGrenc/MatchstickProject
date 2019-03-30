var assert = require('assert');
var expect = require('chai').expect;
var sinon = require('sinon');

var fs = require('fs');
eval(fs.readFileSync('WebContent/scripts/timer.js').toString());


describe('null Timer', function () {
	it('should return 00:00,0', function () {
		// prepare timer
		var timer = new Timer(null);
   
		// time() function should return '00:00,0'
		expect(timer.time()).to.be.equal('00:00,0');
	});
});

describe('Timer with only startTime', function () {
	it('should return value until now', function () {
		// prepare timer
		var timer = new Timer(null);
		
		timer.now = mockNow("2019-01-01T00:00:00.000+00:00");
		timer.startTimer();
		
		timer.now = mockNow("2019-01-01T00:01:02.300+00:00");
		timer.stopTimer();

		// time() function should return '01:02,3'
		expect(timer.time()).to.be.equal('01:02,3');
	});
});

describe('Timer with startTime and endTime', function () {
	it('should return value until between points', function () {
		// prepare timer
		var timer = new Timer(null);
		
		timer.now = mockNow("2019-01-01T00:00:00.000+00:00");
		timer.startTimer();
		
		timer.now = mockNow("2019-01-01T00:01:02.300+00:00");
		timer.stopTimer();

		// at a later moment
		timer.now = mockNow("2019-01-01T00:05:02.000+00:00");
		
		// time() function should return '01:02,3'
		expect(timer.time()).to.be.equal('01:02,3');
	});
});

describe('Timer continues', function () {
	it('should return value of both times combined', function () {
		// prepare timer
		var timer = new Timer(null);
		
		timer.now = mockNow("2019-01-01T00:00:00.000+00:00");
		timer.startTimer();
		
		timer.now = mockNow("2019-01-01T00:01:02.300+00:00");
		timer.stopTimer();
		
		// continue timer
		timer.now = mockNow("2019-01-01T00:02:00.000+00:00");
		timer.continueTimer();
		
		timer.now = mockNow("2019-01-01T00:03:02.300+00:00");
		timer.stopTimer();
		
		// time() function should return '02:04,6'
		expect(timer.time()).to.be.equal('02:04,6');
	});
});

describe('Timer prints into element', function () {
	it('should return 00:00,0', function () {
		// prepare mock dom element
		var domEl = {innerHTML: null};
		
		// prepare timer
		var timer = new Timer(domEl);
	  
		// call print function
		timer.printTime();
   
		// should the innerHTML be properly set to '00:00,0'
		expect(domEl.innerHTML).to.be.equal('00:00,0');
	});
});

describe('Timer prints into element', function () {
	it('should return 00:00,0', function () {
		// prepare mock dom element
		var domEl = {innerHTML: null};
		
		// prepare timer
		var timer = new Timer(domEl);
	  
		// call print function
		timer.printTime();
   
		// should the innerHTML be properly set to '00:00,0'
		expect(domEl.innerHTML).to.be.equal('00:00,0');
	});
});


function mockNow(timeStr) {
	return function() {
		return Date.parse(timeStr);
	}
}

