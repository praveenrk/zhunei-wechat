//获取url中的参数
function getQueryString(name)
{
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
}

//设置主题
function setTheme(_t)
{
	if(_t=="win8")
	{
		$.os.ie=true;
	}
	$("#afui").get(0).className=_t;
	window.localStorage.theme=_t;
}


var app = new $.mvc.app();

app.loadModels(["stuff"]);
app.loadControllers(["stuff"]);
//Routing by hash change
//app.listenHashChange();

//We wait until app.ready is available to fetch the data, then we wire up the existing data in the templates
app.ready(function(){
	var _u = getQueryString('route');
	if(_u)
	{
		$.mvc.route(_u);
	}
	else
	{
		$.mvc.route("/stuff");//Load the default todo route
	}
});
