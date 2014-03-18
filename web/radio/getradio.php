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
	
	//查询url是否存在
	function url_exists($urlpath)
	{
		$h = get_headers($urlpath);
		if(strpos($h[0],'OK')>-1 || strpos($h[0],'302')>-1)
		{
			return true;
		}
		return false;
	}
	
	
	//基础频道类
	class BaseChannel
	{
		public static function append2All($c,$j)
		{
			$ja = json_decode(file_get_contents("list"),true);
			
			if(array_key_exists($c,$ja))
			{
				$curDate = DateTime::createFromFormat('Y-m-d',$ja[$c]['date'])->getTimestamp();
				$newDate = DateTime::createFromFormat('Y-m-d',$j['date'])->getTimestamp();
				if($curDate<$newDate)
				{
					$ja[$c] = $j;
					file_put_contents("list",json_encode($ja));
				}
			}
			else
			{
				$ja[$c] = $j;
				file_put_contents("list",json_encode($ja));
			}
		}
		
		public static function createChannel($c)
		{
			if($c=="cx")
			{
				return new CxChannel();
			}
			else if($c=="vacn")
			{
				return new VacnChannel();
			}
			else if($c=="gos")
			{
				return new GosChannel();
			}
			else
			{
				return new BaseChannel();
			}
		}
		public function getRadio($date)
		{
			return json_decode(file_get_contents("list"),true);
		}
	}
	
	//晨星生命之音
	class CxChannel extends BaseChannel
	{
		function getRadio($date)
		{
			$strDate = date('Y-m-d',$date);
			$cxfile = './cx/'.$strDate;
			$cxjson = null;
			if(!file_exists($cxfile))
			{
				$cxdate = date("Y-n-j", $date);
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
				if($i<1)
				{
					return null;
				}
				file_put_contents($cxfile,json_encode($cxjson));
				BaseChannel::append2All("cx",$cxjson);
			}
			else
			{
				$cxjson = json_decode(file_get_contents($cxfile),true);
			}
			return $cxjson;
		}
	}
	
	//梵蒂冈中文广播
	class VacnChannel extends BaseChannel
	{
		function getRadio($date)
		{
			$strDate = date('Y-m-d',$date);
			$vafile = './vacn/'.$strDate;
			$vajson = null;
			if(!file_exists($vafile))
			{
				$vajson["title"] = "梵蒂冈中文广播";
				$vajson["date"] = $strDate;
				$vajson["logo"] = "http://cathassist.org/radio/logos/vacn.jpg";
				$itemsrc = "http://media.cathassist.org/vaticanradio/cn/mp3/".$strDate.".mp3";
				if(url_exists($itemsrc))
				{
					$title = "梵蒂冈中文广播";
					$vajson['items'][0] = array('title'=>$title,'src'=>$itemsrc);
					file_put_contents($vafile,json_encode($vajson));
					BaseChannel::append2All("vacn",$vajson);
				}
				else
				{
					return null;
				}
			}
			else
			{
				$vajson = json_decode(file_get_contents($vafile),true);
			}
			return $vajson;
		}
	}
	
	//每日福音
	class GosChannel extends BaseChannel
	{
		function getRadio($date)
		{
			$strDate = date('Y-m-d',$date);
			$gosfile = './gos/'.$strDate;
			$gosjson = null;
			if(!file_exists($gosfile))
			{
				$gosjson["title"] = "每日福音";
				$gosjson["date"] = $strDate;
				$gosjson["logo"] = "http://cathassist.org/radio/logos/gos.jpg";
				$itemsrc = "http://media.cathassist.org/thought/mp3/".$strDate.".mp3";
				if(url_exists($itemsrc))
				{
					$title = "每日福音";
					$gosjson['items'][0] = array('title'=>$title,'src'=>$itemsrc);
					file_put_contents($gosfile,json_encode($gosjson));
					BaseChannel::append2All("gos",$gosjson);
				}
				else
				{
					return null;
				}
			}
			else
			{
				$gosjson = json_decode(file_get_contents($gosfile),true);
			}
			return $gosjson;
		}
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
		if($date>time()+3600*8)
		{
			$date=(time()+3600*8);
		}
	}
	
	$cc = BaseChannel::createChannel($channel);
	$ret = $cc->getRadio($date);
	$count = 0;
	while($ret==null && $count<10)
	{
		//查询历史十天内的数据
		$date = $date-3600*24;
		$ret = $cc->getRadio($date);
		$count++;
	}
	if($ret==null)
	{
		die("Something is wrong about this channel.");
	}
	echo(json_encode($ret));
?>