Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function htmlencode(s){
    var div = document.createElement('div');
    div.appendChild(document.createTextNode(s));
	var e = div.innerHTML;
	e=e.replace(/\r\n/g,"</br>")  
	e=e.replace(/\n/g,"</br>");
    return e;
}

function htmldecode(s)
{
    var div = document.createElement('div');
    div.innerHTML = s;
    return div.innerText || div.textContent;
}

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
			audioPlayer.release();
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