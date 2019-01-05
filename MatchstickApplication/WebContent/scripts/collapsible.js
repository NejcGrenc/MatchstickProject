function enableCollapsible()
{
	var coll = document.getElementsByClassName("collapsible");
	var i;
	
	for (i = 0; i < coll.length; i++) {
	  coll[i].addEventListener("click", function()
	  {
	    this.classList.toggle("active");

	    for (var sibling = this.nextElementSibling; sibling !== null; sibling = sibling.nextElementSibling)
	    {
	    	if (sibling.classList.contains("collapsible") || sibling.classList.contains("collapsible_content"))
	    	{
				if (sibling.style.display === "block") {
					sibling.style.display = "none";
				} else {
					sibling.style.display = "block";
				}
	    	}
	    }	    
	  });
	}
}
