<?php
	require_once("../include/dbconn.php");
	require_once("../include/define.php");
	function get_inner_html($node)
	{
		$innerHTML= '';
		$children = $node->childNodes;
		foreach ($children as $child)
		{
			$innerHTML .= $child->ownerDocument->saveXML( $child );
		}

		return $innerHTML;
	}
	
	function getArticle($link,$title,$filestr)
	{
		$contents = file_get_contents($link);
		$doc = new DOMDocument();
		$doc->loadHTML($contents);
		$content2 = $doc->getElementById('content2');
		if(!is_null($content2))
		{
			$fp = fopen($filestr,"w");
			if(!$fp)
			{
				return;
			}
			else
			{
				$content='<html><head><title>'.$title.'</title><meta http-equiv=Content-Type content="text/html;charset=utf-8"><meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"><meta name="apple-mobile-web-app-capable" content="yes"><meta name="apple-mobile-web-app-status-bar-style" content="black"><meta name="format-detection" content="telephone=no"></head><body>'.get_inner_html($content2).'</body><script type="text/javascript" language="javascript" src="/include/googleanalysis.js"></script>';
				fwrite($fp,$content);
				$result = mysql_query('insert into vaticanacn (src,local,time) values '.'("'.mysql_real_escape_string($link).'","'.mysql_real_escape_string($filestr).'",curdate());');
			}
			fclose($fp);
		}
	}
	
//	getArticle("http://zh.radiovaticana.va/articolo.asp?c=727740","title",'articles/123.html');
//	return;
	$rssurl = "http://zh.radiovaticana.va/rssarticoli.asp";
	$rsscontent = file_get_contents($rssurl);
	$rss = simplexml_load_string($rsscontent);
	$channel = $rss->channel;
	for($i=(count($channel->item)-1);$i>=0;$i--)
	{
		$item = $channel->item[$i];
		$filestr = 'articles/'.md5($item->link).'.html';
		
		if(!file_exists($filestr))
		{
			getArticle($item->link,$item->title,$filestr);
			echo('<a href="'.ROOT_WEB_URL.'vaticanacn/'.$filestr.'">'.$item->title.'</a></br><br/>');
		}
	}
?>