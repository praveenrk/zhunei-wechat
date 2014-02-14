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

function replackAudioTag(_html)
{
	var e = $( '<div></div>' );
	e.html(_html);
	$('audio',e).each(
		function()
		{
			var _title = this.innerHTML;
			var _src = this.src;
			if(_title=="")
			{
				var pos = _src.lastIndexOf("/")*1;
				_title = _src.substring(pos+1);
			}
			var _btn = $('<div class="button-grouped flex"><a class="button" href="javascript:void(0)" onclick="audioPlayer.setAudio(\''+_title+'\',\''+_src+'\',true);">播放音频</a></div>');
			this.parentElement.replaceChild(_btn.get(0),this);
		}
	);
	return e.get(0).innerHTML;
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

//同步“自动播放下一首”按钮的状态
function checkAutoPlayNext()
{
	try
	{
		if(window.localStorage.autoPlayNext=="false")
			$('#autoPlayNext').get(0).checked = false;
		else
			$('#autoPlayNext').get(0).checked = true;
	}
	catch(err)
	{
		$('#autoPlayNext').get(0).checked = false;
	}
}

//设置自动播放下一首的数据
function setAutoPlayNext()
{
	window.localStorage.autoPlayNext = $('#autoPlayNext').get(0).checked;
}

function onDeviceReady()
{
	// waiting for button
	document.addEventListener("backbutton", function(e){
		if(window.location.href.substr(window.location.href.length-5)=="stuff")
		{
			e.preventDefault();
			audioPlayer.stop();
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