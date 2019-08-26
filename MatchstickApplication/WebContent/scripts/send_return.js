
/* Main variable here is returnUrl, which needs to be specified from outside. */

function postReturn(parameters)
{		
	// Set default if not specified.
	parameters = parameters || {}; 
	if (post)
	{
		post(returnUrl, parameters);
	}
	else
	{ console.log("Failed at going back to previous page. Post function missing."); }
}