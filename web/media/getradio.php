<?php
	require_once("../include/define.php");
	$xmlDoc = new DOMDocument();
	$xmlDoc->load("../wechat/vaticanradio.xml");
	$root = $xmlDoc->documentElement;
	
	$ret = null;
	$ret['mp3'] = $root->getElementsByTagName('MusicUrl')->item(0)->textContent;
	$ret['title'] = $root->getElementsByTagName('Title')->item(0)->textContent;
	$ret['desc'] = $root->getElementsByTagName('Description')->item(0)->textContent;
	
	echo $_GET['callback'].'('.json_encode($ret).')';
?>