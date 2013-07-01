var cat = document.getElementsByClassName("catalogList")[0];
var ul = cat.getElementsByTagName("ul")[0];
var ccc = 0;
for( var l in ul.children)
{
	try
	{
		var a = ul.children[l].textContent;
		var c = a.replace(/[^\d]/g,'');
		if(c!="")
		{
			ccc = ccc + parseInt(c);
		}
	}
	catch(err)
	{
	}
}

alert(ccc);
