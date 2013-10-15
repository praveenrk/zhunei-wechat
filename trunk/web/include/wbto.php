<?php
//用于发送微博到微博通
function send_to_wbto($content)
{
	$username = '天主教小助手';
	$password = 'password';
	
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

//how to use
//send_to_wbto('欢迎大家关注天主教小助手的微博');
?>