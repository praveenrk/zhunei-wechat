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

function add2weibolist($content)
{
	mysql_query('insert into weibolist(text) values ("'.mysql_real_escape_string($content).'");');
}

function check2updateweibo()
{
	$result = mysql_query('select id,text from weibolist order by id desc limit 1;');
	while ($row = mysql_fetch_array($result))
	{
		send2wbto($row['text']);
		echo('send to wbto "'.$row['text'].'"');
		mysql_query('delete from weibolist where id='.$row['id'].';');
	}
}
//how to use
//send_to_wbto('欢迎大家关注天主教小助手的微博');
?>