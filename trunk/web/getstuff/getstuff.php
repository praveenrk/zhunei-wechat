<?php
	require_once("../include/dbconn.php");
	require_once("../include/define.php");
	function unicode2utf8($str)
	{
			if(!$str) return $str;
			$decode = json_decode($str);
			if($decode) return $decode;
			$str = '["' . $str . '"]';
			$decode = json_decode($str);
			if(count($decode) == 1){
					return $decode[0];
			}
			return $str;
	}
	$trimedUtf8 = array(unicode2utf8("\\u0014"));
	/*
	错误码定义
	1 没有日期参数或日期参数不正确
	2 未获取到数据
	3 连接数据库失败
	*/
	//http://mhchina.a24.cc/api/v1/getstuff/
	header("Cache-Control: no-cache, must-revalidate");
	header("Pragma: no-cache");
	header("Content-type: text/html; charset=utf-8");
	require_once("chinese_conversion/convert.php");
	$isjson = true;
	$errorcode = 1;
	$mode="";
	if(array_key_exists("type",$_GET))
	{
		if($_GET["type"]=="xml")
			$isjson = false;
	}
	if(array_key_exists("mode",$_GET))
	{
		$mode = $_GET["mode"];
	}
	
	if(array_key_exists("date",$_GET)==false)
	{
		$errorcode = 1;
		goto ERROR;
	}
	$date = DateTime::createFromFormat("Y-m-d",$_GET["date"]);
	if(date('Y-m-d',strtotime($_GET["date"]))!=$_GET["date"])
	{
		$errorcode = 1;
		goto ERROR;
	}

	//返回的数据
	$stuff_mass = "";		//弥撒
	$stuff_med = "";		//日祷
	$stuff_comp = "";		//夜祷
	$stuff_let = "";		//诵读
	$stuff_lod = "";		//晨祷
	$stuff_thought = "";	//反省
	$stuff_ordo = "";		//礼仪
	$stuff_ves = "";		//晚祷
	$stuff_saint = "";		//圣人传记
	
	$isupdate = false;
	{
		//先从数据库中获取
		$result = mysql_query("select * from stuff where time='".$date->format('Y-m-d')."';");
		if(mysql_num_rows($result)>0)
		{
			$isupdate = true;
			$row = mysql_fetch_array($result);
			if($row['valid']>0)
			{
				//已经拥有数据可以直接获取
				$stuff_mass = $row["mass"];		//弥撒
				$stuff_med = $row["med"];		//日祷
				$stuff_comp = $row["comp"];		//夜祷
				$stuff_let = $row["let"];		//诵读
				$stuff_lod = $row["lod"];		//晨祷
				$stuff_thought = $row["thought"];	//反省
				$stuff_ordo = $row["ordo"];		//礼仪
				$stuff_ves = $row["ves"];		//晚祷
				$stuff_saint = $row["saint"];		//圣人传记
				
				goto END;
			}
			else if($row['lastupdate']==date('Y-m-d'))
			{
				$errorcode = 2;
				goto ERROR;
			}
		}
		else
		{
			goto GETSTUFF;
		}
	}

GETSTUFF:
	$mcurl = curl_init();
	curl_setopt($mcurl,CURLOPT_URL,"http://mhchina.a24.cc/api/v1/getstuff/");
	curl_setopt($mcurl, CURLOPT_RETURNTRANSFER, 1);//设置是否返回信息
//	curl_setopt($mcurl, CURLOPT_HTTPHEADER, $header);//设置HTTP头
	curl_setopt($mcurl, CURLOPT_POST, 1);//设置为POST方式
	curl_setopt($mcurl, CURLOPT_POSTFIELDS, '{"sdb":true,"to":"'.$date->format('Y-m-d').'","from":"'.$date->format('Y-m-d').'"}');//POST数据
	$response = curl_exec($mcurl);//接收返回信息
	$json = json_decode($response,true);
	if($json==null)
	{
		goto GETSTUFFERROR;
	}
	
	{
		//获取返回的数据
		$json_date = $json[$date->format('Y-m-d')];
		if($json_date == null)
		{
			goto GETSTUFFERROR;
		}
		
		$json_mass = $json_date['mass'];
		if($json_mass)
			$stuff_mass = zhconversion_hans($json_mass['content']);
		
		$json_med = $json_date['med'];
		if($json_med)
			$stuff_med = zhconversion_hans($json_med['content']);
		
		$json_comp = $json_date['comp'];
		if($json_comp)
			$stuff_comp = zhconversion_hans($json_comp['content']);
		
		$json_let = $json_date['let'];
		if($json_let)
			$stuff_let = zhconversion_hans($json_let['content']);

		$json_lod = $json_date['lod'];
		if($json_lod)
			$stuff_lod = zhconversion_hans($json_lod['content']);

		$json_thought = $json_date['thought'];
		if($json_thought)
			$stuff_thought = zhconversion_hans($json_thought['content']);
		
		$json_ordo = $json_date['ordo'];
		if($json_ordo)
			$stuff_ordo = zhconversion_hans($json_ordo['content']);
		
		$json_ves = $json_date['ves'];
		if($json_ves)
			$stuff_ves = zhconversion_hans($json_ves['content']);
		
		$json_saint = $json_date['saint'];
		if($json_saint)
			$stuff_saint = zhconversion_hans($json_saint['content']);
		goto INSERTSTUFF;
	}
	
GETSTUFFERROR:
	if($isupdate)
	{
		mysql_query("update stuff set lastupdate=curdate() where time='".$date->format('Y-m-d')."';");
	}
	else
	{
		mysql_query("insert into stuff (time,valid,lastupdate) values ('".$date->format('Y-m-d')."',0,curdate());");
	}	
	$errorcode = 2;
	goto ERROR;
	
INSERTSTUFF:
	//插入到数据库
	if($isupdate)
	{
		mysql_query("update stuff set mass='".mysql_real_escape_string($stuff_mass)."',med='".mysql_real_escape_string($stuff_med)."',comp='".mysql_real_escape_string($stuff_comp)."',let='".mysql_real_escape_string($stuff_let)."',lod='".mysql_real_escape_string($stuff_lod)
		."',thought='".mysql_real_escape_string($stuff_thought)."',ordo='".mysql_real_escape_string($stuff_ordo)."',ves='".mysql_real_escape_string($stuff_ves)."',saint='".mysql_real_escape_string($stuff_saint)."',valid=1,lastupdate=curdate() "
		."where time='".$date->format('Y-m-d')."';");
	}
	else
	{
	
		$result = mysql_query('insert into stuff (time,mass,med,comp,let,lod,thought,ordo,ves,saint,valid,lastupdate) values '.
		'("'.$date->format('Y-m-d').'","'.mysql_real_escape_string($stuff_mass).'","'.mysql_real_escape_string($stuff_med).'","'.mysql_real_escape_string($stuff_comp).'","'.mysql_real_escape_string($stuff_let).'","'.mysql_real_escape_string($stuff_lod)
		.'","'.mysql_real_escape_string($stuff_thought).'","'.mysql_real_escape_string($stuff_ordo).'","'.mysql_real_escape_string($stuff_ves).'","'.mysql_real_escape_string($stuff_saint).'",1,curdate());');
	}
	goto END;
ERROR:
	if($isjson)
		echo '{"error":'.$errorcode.'}';
	else
		echo '<error>'.$errorcode.'</error>';
	return;
	
END:
	$ret["mass"] = ($stuff_mass);
	$ret["med"] = ($stuff_med);
	$ret["comp"] = ($stuff_comp);
	$ret["let"] =  ($stuff_let);
	$ret["lod"] =  ($stuff_lod);
	$ret["thought"] =  ($stuff_thought);
	$ret["ordo"] =  ($stuff_ordo);
	$ret["ves"] =  ($stuff_ves);
	$ret["saint"] =  ($stuff_saint);
	if($isjson)
	{
		$ret = preg_replace("#\\\u([0-9a-f]+)#ie", "iconv('UCS-2', 'UTF-8', pack('H4', '\\1'))", json_encode($ret));
//		$ret = '{"mass":"'.$stuff_mass.'","med":"'.$stuff_med.'","comp":"'.$stuff_comp.'","let":"'.$stuff_let
//		.'","lod":"'.$stuff_lod.'","thought":"'.$stuff_thought.'","ordo":"'.$stuff_ordo.'","ves":"'.$stuff_ves.'","saint":"'.$stuff_saint.'"}';
	}
	else
	{
		$ret = '<mass>'.htmlspecialchars($stuff_mass, ENT_QUOTES).'</mass><med>'.htmlspecialchars($stuff_med, ENT_QUOTES).'</med><comp>'
		.htmlspecialchars($stuff_comp, ENT_QUOTES).'</comp><let>'.htmlspecialchars($stuff_let, ENT_QUOTES)
		.'<let><lod>'.htmlspecialchars($stuff_lod, ENT_QUOTES).'</lod><thought>'.htmlspecialchars($stuff_thought, ENT_QUOTES).'</thought><ordo>'
		.htmlspecialchars($stuff_ordo, ENT_QUOTES).'</ordo><ves>'.htmlspecialchars($stuff_ves, ENT_QUOTES).'</ves><saint>'.htmlspecialchars($stuff_saint, ENT_QUOTES).'</saint>';
	}
	
	$ret = str_replace($trimedUtf8,"",$ret);
	
	if($mode!="")
	{
		$json = json_decode($ret,true);
		echo'<head>
		<meta name="viewport" content="user-scalable=no, width=device-width" />  
		</head><html>';
		echo $json[$mode];
		echo "</html>";
	}
	else
	{
		echo $ret;
	}
	return;
//	echo json_encode($retArray,JSON_UNESCAPED_UNICODE|JSON_UNESCAPED_SLASHES);
?>