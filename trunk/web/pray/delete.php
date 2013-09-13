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
<?php
	$id = (int)$_GET['id'];
	
	//先从数据库中获取
	$result = mysql_query("delete from pray where id=".$id.";");
	if(mysql_affected_rows()>0)
	{
		die('已删除');
	}
?>