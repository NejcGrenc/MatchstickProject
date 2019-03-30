var assert = require('assert');
var expect = require('chai').expect;
var sinon = require('sinon');

var fs = require('fs');
eval(fs.readFileSync('WebContent/scripts/translations/translate.js').toString());


describe('setLanguageBall', function () {
	it('should return english ball', function () {
		// prepare user language
		mockEmptyBallObject();
		userLang = 'en';
	  
		// call function
		setLanguageBall();
   
		// should be source properly set
		expect(ball.src).to.be.equal('images/languages/flag-en.png');

	});
});

describe('setLanguageBall', function () {
	it('should return no ball, when none is specified', function () {
		// prepare user language
		let spy = sinon.spy(console, 'log');
		mockEmptyBallObject();
		userLang = '';
	  
		// call function
		setLanguageBall();
   
		// should be source properly set
		expect(ball.src).to.be.equal(undefined);
				
		assert(spy.calledWith("ERROR: No language specified."));

	});
});


function mockEmptyBallObject() {
	ball = {src: undefined};
	document = { getElementById(id) { return ball }};
	
	expect(ball.src).to.be.equal(undefined);
	return ball;
}

