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
	div
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
	</style>
</head>
<html>
<?php
	//先从数据库中获取
	$result = mysql_query("select name,text,createtime from pray order by id desc limit 10;");
	while ($row = mysql_fetch_array($result))
	{
		echo('<div><span  style="width:100%">昵称：'.$row['name'].'  留言时间：'.date('Y-m-d H:i',strtotime($row['createtime'])+3600*8).'</span><p>'.nl2br($row['text']).'</p></div>');
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
	<textarea id="input_text" name="text" style="width:100%; height:80px">在此输入你的祈祷意向，然后点击提交</textarea><br/>
	<input type="submit" value=" 提 交 ">
</form>

<script type="text/javascript">
var t = document.getElementById('input_text');
t.onfocus = function(){
	if(this.innerHTML == '在此输入你的祈祷意向，然后点击提交'){this.innerHTML = '';}
};

t.onblur = function(){
	if(this.innerHTML == ''){
		this.innerHTML = '在此输入你的祈祷意向，然后点击提交';
	}
};
</script>
<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-29392184-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>
</html>