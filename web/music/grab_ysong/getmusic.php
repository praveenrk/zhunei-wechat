<?php
	require_once("dbconn.php");
	require_once("simple_html_dom.php");
	
	{
		//清空数据库
//		mysql_query("delete from singer where id>0;");
//		mysql_query("delete from alume where id>0;");
//		mysql_query("delete from song where id>0;");
	}
	$rssurl = "http://www.ysong.org/";
	$html = new simple_html_dom();
	$html->load_file($rssurl);
	//$all{src,obj{src,name,}}
	$singers;
	//从首页获取所有的列表
	echo("read root page\n");
	{
		$sid = 0;
		$s = $html->find('.wz12_ffff1 a');
		for($i=0;$i<count($s);$i++)
		{
			$src = "http://www.ysong.org".$s[$i]->href;
			$html = new simple_html_dom();
			$html->load_file($src);
			$s2 = $html->find('.wz12_005c a');
			for($j=0;$j<count($s2);$j++)
			{
				echo($s2[$j]->innertext."\n");
				$singers[$sid]['name'] = $s2[$j]->innertext;
				$singers[$sid]['src'] = $s2[$j]->href;
				$result = mysql_query('select id,name from singer where src="'.$s2[$j]->href.'"');
				if(mysql_num_rows($result)<1)
				{
					mysql_query('insert into singer(name,src) values("'.$s2[$j]->innertext.'","'.$s2[$j]->href.'");');
					$singers[$sid]['id'] = mysql_insert_id();
				}
				else
				{
					while ($row = mysql_fetch_array($result))
					{
						$singers[$sid]['id'] = $row['id'];
					}
				}
				$sid++;
			}
		}
	}
	
	for($i=0;$i<count($singers);$i++)
	{
		$s = $singers[$i];
		echo("get album of ".$s['name']."\n");
		//Album
		$html = new simple_html_dom();
		$html->load_file($s['src']);
		$s2 = $html->find('td[bgcolor] a');
		for($j=0;$j<count($s2);$j++)
		{
			$src = $s2[$j]->href;
			$name = $s2[$j]->first_child()->alt;
			$pic = $s2[$j]->first_child()->src;
			echo($name."\t");
			$singers[$i]['alume'][$j]['src'] = $src;
			$singers[$i]['alume'][$j]['name'] = $name;
			$singers[$i]['alume'][$j]['pic'] = "http://www.ysong.org".$pic;
			
			$result = mysql_query('select id,name from alume where src="'.$src.'"');
			if(mysql_num_rows($result)<1)
			{
				mysql_query('insert into alume(name,pic,src,singer) values("'.$name.'","'.'http://www.ysong.org'.$pic.'","'.$src.'",'.$singers[$i]['id'].');');
				$singers[$i]['alume'][$j]['id'] = mysql_insert_id();
			}
			else
			{
				while ($row = mysql_fetch_array($result))
				{
					$singers[$i]['alume'][$j]['id'] = $row['id'];
				}
				continue;
			}
			
			//Song
			$html = new simple_html_dom();
			$html->load_file($src);
			$songs = $html->find("tr[height]");
			for($k=0;$k<count($songs);$k++)
			{
				$song = $songs[$k];
				$src = $song->children(9)->children(0)->href;
				$name = $song->children(1)->plaintext;
				$singers[$i]['alume'][$j]['songs'][$k]['name'] = $name;
				$singers[$i]['alume'][$j]['songs'][$k]['src'] = $src;
				
				$result = mysql_query('select id,name from song where src="'.$src.'"');
				if(mysql_num_rows($result)<1)
				{
					mysql_query('insert into song(name,src,alume,singer) values("'.$name.'","'.$src.'",'.$singers[$i]['alume'][$j]['id'].','.$singers[$i]['id'].');');
					$singers[$i]['alume'][$j]['songs'][$k]['id'] = mysql_insert_id();
				}
				else
				{
					while ($row = mysql_fetch_array($result))
					{
						$singers[$i]['alume'][$j]['songs'][$k]['id'] = $row['id'];
					}
				}
			}
		}
		echo("\n\n");
	}
?>