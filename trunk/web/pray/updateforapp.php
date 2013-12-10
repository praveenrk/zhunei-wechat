<?php
	require_once("../include/dbconn.php");
	require_once("../include/define.php");
	require_once("../include/wbto.php");
	
	session_start();
?>
<?php
	function gotoend()
	{
		global $error;
		$ret['error'] = $error;
		die($_GET['callback'].'('.json_encode($ret).')');
	}
	
	$error = "";
	if($_GET["type"]!="jsonp")
	{
		die("");
	}
	
	if(isset($_GET["text"]))
		$text = trim($_GET["text"]);
	if(strlen($text)<20)
	{
		$error= "请输入祈祷意向!";
		gotoend();
	}
	
	if (!isset($_SESSION['pray_time']))
	{
		$_SESSION['pray_time'] = 0;
	}
	$cur_time = date(time());
	$last_time = $_SESSION['pray_time'];
	if(abs($cur_time-$last_time) < 600)
	{
		//10分钟只能提交一次祈祷意向
		$error = '请不要频繁提交祈祷意向，'.(600-abs($cur_time-$last_time)).'秒后再试，主佑！';
		gotoend();
	}
	else
	{
		$_SESSION['pray_time'] = $cur_time;
	}
	
	if(isset($_GET['name']))
	{
		$name=trim($_GET['name']);
	}
	
	//先从数据库中获取
	$result = mysql_query("insert into pray (name,text,createtime) values ('".$name."','".$text."',utc_timestamp());");
	if(mysql_affected_rows()<1)
	{
		$error = "添加祈祷意向失败，请稍后重试...";
		gotoend();
	}
	//send to wbto
	add2weibolist('#彼此代祷# '.$name.'：'.$text);
	$ret['error']=$error;
	die($_GET['callback'].'('.json_encode($ret).')');
?>