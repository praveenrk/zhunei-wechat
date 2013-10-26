<?php
require_once("../include/dbconn.php");

//用于发送微博到微博通
function send2wbto($content)
{
	$username = '天主教小助手';
	$password = 'pass';
	
	$fields = array();
	$fields['source'] = 'wordpress';
	$fields['content'] = urlencode($content);
	
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, "http://wbto.cn/api/update.json");
	curl_setopt($ch, CURLOPT_USERPWD, "$username:$password");
	curl_setopt($ch, CURLOPT_FAILONERROR, TRUE);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,TRUE);
	curl_setopt($ch, CURLOPT_TIMEOUT, 10);
	curl_setopt($ch, CURLOPT_POST, TRUE);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
	$result = curl_exec($ch);
	curl_close($ch);  
}

function add2weibolist($content,$t=0)
{
	if($t==0)
	{
		$t = time()+3600*8;
	}
	mysql_query('insert into weibolist(text,time) values ("'.mysql_real_escape_string($content).'","'.gmdate("Y-m-d H:i:s",$t).'");');
}

function check2updateweibo()
{
	$result = mysql_query('select id,text,time from weibolist where time<"'.gmdate("Y-m-d H:i:s",time()+3600*8).'";');
	while ($row = mysql_fetch_array($result))
	{
		send2wbto($row['text']);
		echo('send to wbto "'.$row['text'].'"'.'&nbsp;&nbsp;time:'.$row['time']);
		mysql_query('delete from weibolist where id='.$row['id'].';');
	}
}
//how to use
//send_to_wbto('欢迎大家关注天主教小助手的微博');
?>