<?php
require_once("../include/dbconn.php");
require_once("../include/define.php");
session_start();
header("Cache-Control: no-cache, must-revalidate");
header("Pragma: no-cache");
header("Content-type: text/html; charset=utf-8");
?>
<head>
	<title>代祷本</title>
	<meta http-equiv=Content-Type content="text/html;charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="format-detection" content="telephone=no">
	<style type="text/css">
	.css_div_class
	{
		margin: 10px 0 0 0;
		padding: 10px;
		border: 0;
		border: 1px dotted #785;
		background: #f5f5f5;
		font-family: "Courier New",monospace;
		font-size: 12px;
	}
	div p
	{
		text-indent: 0em;
		font-family: "Microsoft JhengHei",SimSun,monospace;
		color:#000000;
		font-size: 15px;
		font-weight:bold;
	}
	div span
	{
		color:#999;
	}
	
	input[type=submit]
	{
		margin: 20px 0 0 20%;
		background-position: bottom left;
		width: 60%;
		height: 30px;
		background-color:#f5f5f;
	}
	.css_btn_class {
		font-size:16px;
		font-family:Arial;
		font-weight:normal;
		-moz-border-radius:8px;
		-webkit-border-radius:8px;
		border-radius:8px;
		border:1px solid #dcdcdc;
		padding:9px 18px;
		text-decoration:none;
		background:-webkit-gradient( linear, left top, left bottom, color-stop(5%, #ededed), color-stop(100%, #dfdfdf) );
		background:-moz-linear-gradient( center top, #ededed 5%, #dfdfdf 100% );
		background:-ms-linear-gradient( top, #ededed 5%, #dfdfdf 100% );
		filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ededed', endColorstr='#dfdfdf');
		background-color:#ededed;
		color:#777777;
		display:inline-block;
		text-shadow:1px 1px 0px #ffffff;
		-webkit-box-shadow:inset 1px 1px 0px 0px #ffffff;
		-moz-box-shadow:inset 1px 1px 0px 0px #ffffff;
		box-shadow:inset 1px 1px 0px 0px #ffffff;
	}.css_btn_class:hover {
		background:-webkit-gradient( linear, left top, left bottom, color-stop(5%, #dfdfdf), color-stop(100%, #ededed) );
		background:-moz-linear-gradient( center top, #dfdfdf 5%, #ededed 100% );
		background:-ms-linear-gradient( top, #dfdfdf 5%, #ededed 100% );
		filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#ededed');
		background-color:#dfdfdf;
	}.css_btn_class:active {
		position:relative;
		top:1px;
	}
	</style>
</head>
<html>
<?php
	//先从数据库中获取
//	mysql_query("delete from pray where createtime>(utc_timestamp()-3600);");
	$result = mysql_query("select id,name,text,createtime from pray order by id desc limit 10;");
	while ($row = mysql_fetch_array($result))
	{
		echo('<div class="css_div_class"><span  style="width:100%">[<a href="detail.php?id='.$row['id'].'">'.$row['id'].'</a>]昵称：'.$row['name'].'  时间：'.date('Y-m-d H:i',strtotime($row['createtime'])+3600*8).'</span><p>'.nl2br($row['text']).'</p></div>');
	}
?>
<hr/>
<p><strong>提交你的代祷意向：</strong></p>
<form action="update.php" method="post">
	<label for="input_name" style="width:100px;">昵称：</label><input name="name" id="input_name" type="text" value="<?php
		if(isset($_SESSION['name']))
		{echo $_SESSION['name'];}else{ echo '匿名';}
		?>"></input>
	<br/>
	<p>代祷内容：</p>
	<textarea id="input_text" name="text" style="width:100%; height:80px">在此输入你的代祷意向，然后点击提交</textarea><br/>
	</p>
	<div align="center"><a href="#" class="css_btn_class" onclick="postText()">提交</a></div>
</form>

<script type="text/javascript">
var t = document.getElementById('input_text');
t.onfocus = function(){
	if(this.innerHTML == '在此输入你的代祷意向，然后点击提交'){this.innerHTML = '';}
};

t.onblur = function(){
	if(this.innerHTML == ''){
		this.innerHTML = '在此输入你的代祷意向，然后点击提交';
	}
};

function postText()
{
	var nick = document.getElementById('input_name').value;
	var text = document.getElementById('input_text').value;
	
	if(text == '在此输入你的代祷意向，然后点击提交')
	{
		alert("请输入代祷意向");
		return;
	}
	
	var data = "name="+window.encodeURIComponent(nick)+"&text="+window.encodeURIComponent(text);
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open("POST","./update.php",false);
	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlhttp.send(data);
	if(xmlhttp.responseText=="")
	{
		window.location.reload();
		window.scrollTo();
	}
	else
	{
		alert(xmlhttp.responseText);
	}
	return xmlhttp.responseText;
}
</script>
<script type="text/javascript" src="/include/googleanalysis.js"/>
</html>