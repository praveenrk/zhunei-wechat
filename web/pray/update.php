<?php
	require_once("../include/dbconn.php");
	require_once("../include/define.php");
	session_start();
	/*
	错误码定义
	1 没有日期参数或日期参数不正确
	2 未获取到数据
	3 连接数据库失败
	*/
	//http://mhchina.a24.cc/api/v1/getstuff/
	header("Content-type: text/html; charset=utf-8");
?>
<head>
	<meta name="viewport" content="user-scalable=no, width=device-width" />  
	<title>更新结果</title>
</head>
<?php
	function gotoend()
	{
		global $error;
		echo $error;
		echo '<form action="index.php" method="post">
		<input style="float:right;" type="submit" value=" 返  回 ">
	</form>';
	}
	$text = "";
	$name = "";
	$error = "";
	if(isset($_POST["text"]))
		$text = trim($_POST["text"]);
	if($text==""  || $text=="在此输入你的祈祷意向，然后点击提交")
	{
		$error= "请输入祈祷意向!";
		gotoend();
	}
	if(isset($_POST['name']))
	{
		$name=trim($_POST['name']);
		$_SESSION['name'] = $name;
	}
	
	
	//先从数据库中获取
	$result = mysql_query("insert into pray (name,text,createtime) values ('".$name."','".$text."',utc_timestamp());");
	if(mysql_query("select row_count();")<1)
	{
		$error = "添加祈祷意向失败，请稍后重试...";
		gotoend();
	}
	
	$error = "更新成功！";
	gotoend();
?>