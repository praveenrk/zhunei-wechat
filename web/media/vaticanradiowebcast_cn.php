<html>
<head>
<title>梵蒂冈中文电台每日快讯</title>
<meta http-equiv=Content-Type content="text/html;charset=utf-8">
<meta name="viewport" content="user-scalable=no, width=device-width" />
<script type="text/javascript">
function cnmetadata(){
//	if(isNaN(myaudio.currentTime) || myaudio.currentTime<10)
	var _a = document.getElementById("audio");
	var _d = document.getElementById("dur_cn");
	var _h = parseInt(_a.duration/3600);
	var _m = parseInt((_a.duration%3600)/60);
	var _s = parseInt(_a.duration%60);
	_d.innerHTML = _h+"时"+_m+"分"+_s+"秒";
}
function enmetadata(){
//	if(isNaN(myaudio.currentTime) || myaudio.currentTime<10)
	var _a = document.getElementById("audioen");
	var _d = document.getElementById("dur_en");
	var _h = parseInt(_a.duration/3600);
	var _m = parseInt((_a.duration%3600)/60);
	var _s = parseInt(_a.duration%60);
	_d.innerHTML = _h+"时"+_m+"分"+_s+"秒";
}
</script>
</head>
<body style="text-align:center;">
<h2>梵蒂冈中文电台每日快讯</h2>
<h3>中文广播</h3>
<div align="center" id="mydiv">
	<audio id="audio" src="<?php
		$content = file_get_contents("http://media01.vatiradio.va/podmaker/podcaster.aspx?c=cinese");
		$xml = new DOMDocument();
		$xml->loadXml($content);
		$postUrls = $xml->getElementsByTagName("enclosure");
		foreach($postUrls as $url)
		{
			echo $url->getAttribute("url");
			break;
		}
	?>" controls preload="metadata" onloadedmetadata="cnmetadata()"></audio>
	<pre>预计时长：<font id="dur_cn">0时0分0秒</font></pre>
</div>
</br>
</br>
</br>
<h3>english news</h3>
<div align="center" id="mydiv2">
	<audio id="audioen" src="http://www.vaticanradio-us.org/webcasting/rg_inglese_2_1.mp3" controls preload="metadata" onloadedmetadata="enmetadata()"></audio>
	<pre>预计时长：<font id="dur_en">0时0分0秒</font></pre>
</div>
</br>
<pre align="center">提示：播放音频会损耗较多流量</pre>
</body>
<script type="text/javascript" language="javascript" src="/include/googleanalysis.js"></script>
</html>
