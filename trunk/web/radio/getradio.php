<?php
	function cn_urlencode($url)
	{
		$pregstr   =  "/[\x{4e00}-\x{9fa5}]+/u" ; //UTF-8中文正则
		if (preg_match_all( $pregstr , $url , $matchArray ))
		{ //匹配中文，返回数组
			foreach ( $matchArray [0]  as   $key => $val )
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
	
	
	$channel = "";
	if(array_key_exists("channel",$_GET))
	{
		$channel = $_GET["channel"];
	}
	
	$date = time()+3600*8;
	if(array_key_exists("date",$_GET))
	{
		$date = DateTime::createFromFormat('Y-m-d',$_GET["date"])->getTimestamp();
		if($date>(time()+3600*8))
			$date=(time()+3600*8);
	}
	
	if($channel=="cx")
	{
		//晨星广播
		$strDate = gmdate('Y-m-d',$date);
		$cxfile = './cx/'.$strDate;
		$cxjson = null;
		if(!file_exists($cxfile))
		{
			$cxdate = gmdate("Y-n-j", $date);
			$cxradio = 'http://radio.cxsm.org/playlist/'.$cxdate.'.txt';
			$cxlist = explode("\n",file_get_contents($cxradio));		//或是url list
			$cnpreg = "/[\x{4e00}-\x{9fa5}]+/u";
			$cxjson["title"] = "晨星生命之音";
			$cxjson["date"] = $strDate;
			$cxjson["logo"] = "http://cathassist.org/radio/logos/cx.png";
			$i = 0;
			foreach($cxlist as $v)
			{
				$v = iconv("GB2312", "UTF-8//IGNORE",trim($v));
				$title = "";
				preg_match_all($cnpreg, substr($v,strrpos($v,'/')), $title);
				$title=implode("", $title[0]);
				$cxjson['items'][$i] = array('title'=>$title,'src'=>cn_urlencode($v));
				++$i;
			}
			file_put_contents($cxfile,json_encode($cxjson));
		}
		else
		{
			$cxjson = json_decode(file_get_contents($cxfile),true);
		}
		echo(json_encode($cxjson));
	}
?>