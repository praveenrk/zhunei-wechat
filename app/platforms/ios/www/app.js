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

function onDeviceReady()
{
	// waiting for button
	document.addEventListener("backbutton", function(e){
		if(window.location.href.substr(window.location.href.length-5)=="stuff")
		{
			e.preventDefault();
			navigator.app.exitApp();
		}
		else
		{
			navigator.app.backHistory();
		}
	}, false);
}

//初始化cordova函数
function initCordovaFunc()
{
	document.addEventListener("deviceready", onDeviceReady, false);
}

//在自带浏览器中打开链接
function openLinkInExternal(_l)
{
	if (navigator.app)
	{
		navigator.app.loadUrl(_l,{ openExternal:true });
		return;
	}
    else if($.os.ios || $.os.ios7)
    {
        window.open(_l+'#cordova=external',"_blank");
        return;
    }
	window.open(_l,"_blank");
}

var app = new $.mvc.app();

app.loadModels(["stuff"]);
app.loadControllers(["stuff"]);
//Routing by hash change
//app.listenHashChange();

window.addEventListener("popstate", function(){
	var _u = getQueryString('route');
	if(_u)
	{
		$.mvc.route(_u,null,true);
	}
	else
	{
		$.mvc.route("/stuff");//Load the default todo route
	}
});