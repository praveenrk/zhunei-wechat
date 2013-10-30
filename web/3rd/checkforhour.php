<?php
	require_once("../include/wbto.php");
	echo('<h1>获取梵蒂冈电台文章</h1>');
	echo(file_get_contents('http://t.liyake.com/vaticanacn/checkrss.php'));
	
	echo('<h1>获取梵蒂冈电台广播</h1>');
	echo(file_get_contents('http://t.liyake.com/media/checkrss.php'));
	
	echo('<h1>获取信德网文章</h1>');
	echo(file_get_contents('http://t.liyake.com/faithlife/caiji.php'));
	
	echo('<h1>发送微博</h1>');
	echo(check2updateweibo());
?>