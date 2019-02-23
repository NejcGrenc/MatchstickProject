

function blockSpecialCharForNameField(event) 
{
	return isAllowedForNameField(eventToCharValue(event));
}

function blockSpecialCharForTextArea(event) 
{
	return isAllowedForTextArea(eventToCharValue(event));
}


// Private functions

function eventToCharValue(event)
{
	var val;
    if (event.keyCode !== undefined && event.keyCode !== null && event.keyCode !== 0)
    	val = event.keyCode;
    else if (event.charCode !== undefined && event.charCode !== null && event.charCode !== 0)
    	val = event.charCode;
    else
    	val = event.which;
    console.log(val);
    return val;
}

function isAllowedForNameField(keyVal)
{
	var isLowercase = (keyVal >= 97 && keyVal <= 122);
	var isUppercase = (keyVal >= 65 && keyVal <= 90);
	var isNumber = (keyVal >= 48 && keyVal <= 57);
	var isSpace = keyVal == 32;
	var isHyphen = keyVal == 45;
	
	return isLowercase || isUppercase || isNumber || isSpace || isHyphen || isDot;
}

function isAllowedForTextArea(keyVal)
{
	var isLowercase = (keyVal >= 97 && keyVal <= 122);
	var isUppercase = (keyVal >= 65 && keyVal <= 90);
	var isNumber = (keyVal >= 48 && keyVal <= 57);
	var isSpace = keyVal == 32;
	var isComma = keyVal == 44;
	var isHyphen = keyVal == 45;
	var isDot = keyVal == 46;
	var isExclamationMark = keyVal == 33;
	var isQuestionMark = keyVal == 63;
	var isAParenthesis = (keyVal == 40 || keyVal == 41);
	var isEnter = keyVal == 13;
	
	return isLowercase || isUppercase || isNumber || isSpace || isComma || isHyphen || 
		   isDot || isExclamationMark || isQuestionMark || isAParenthesis || isEnter;
}