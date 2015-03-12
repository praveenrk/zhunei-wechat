<?php
	chdir(dirname(__FILE__));
	
	set_time_limit(60*15);		//设置超时为15分钟
	
	error_reporting(E_ERROR | E_PARSE);
	
	function curl_download($remote, $local) {
		$cp = curl_init($remote);
		$fp = fopen($local, "w");
		curl_setopt($cp, CURLOPT_FILE, $fp);
		curl_setopt($cp, CURLOPT_HEADER, 0);

		curl_exec($cp);
		curl_close($cp);
		fclose($fp);
	}
	
	function cn_urlencode($url)
	{
		$pregstr   =  "/[\x{4e00}-\x{9fa5}]+/u" ; //UTF-8中文正则
		if (preg_match_all( $pregstr , $url , $matchArray ))
		{ //匹配中文，返回数组
			foreach($matchArray[0] as $key=>$val)
			{
				$url = str_replace ( $val , urlencode( $val ),  $url ); //将转译替换中文
			}
			if ( strpos ( $url , ' ' ))
			{ //若存在空格
				$url = str_replace ( ' ' , '%20' , $url );
			}
		}
		return $url;
	}
	
	function url_exists($urlpath)
	{
		$h = get_headers($urlpath);
		if(strpos($h[0],'OK')>-1)
		{
			return true;
		}
		return false;
	}
//	die(base64_encode('media.cathassist.org/vaticanradio/cn/mp3/2015-01-08.mp3'));

	if(array_key_exists("url",$_GET) === false)
	{
		die("");
	}

	$url = $_GET["url"];
	
	{
		$local = './objs/'.md5($url).'.tmp';
		$lll = './objs/'.md5($url).'ttt';
		if(!file_exists($lll))
		{
			echo("caching link:".$url."<br/>");
			curl_download($url,$local);
			
			if(!rename($local,$lll))
			{
				echo("rename false<br/>");
			}
		}
	}
	
	echo('<br/><h2>Done!!!</h2>');
?>