
function fullyHideElementWithId(elementId)
{
	if (elementId !== null || elementId !== undefined)
	{
		var element = document.getElementById(elementId);
		if (element !== null || element !== undefined)
		{
			element.style.display = "hide";
		}
	}
}

function hideElementWithId(elementId)
{
	if (elementId !== null || elementId !== undefined)
	{
		var element = document.getElementById(elementId);
		if (element !== null || element !== undefined)
		{
			element.style.visibility = "hidden";
		}
	}
}

function showElementWithId(elementId)
{
	if (elementId !== null || elementId !== undefined)
	{
		var element = document.getElementById(elementId);
		if (element !== null || element !== undefined)
		{
			element.style.visibility = "visible";
		}
	}
}