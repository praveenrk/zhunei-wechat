<?php
	require_once("../include/define.php");
	$xmlDoc = new DOMDocument();
	$xmlDoc->load("../wechat/vaticanradio.xml");
	$root = $xmlDoc->documentElement;
	
	$ret = null;
	$ret['mp3'] = '';
	$ret['url'] = $root->getElementsByTagName('MusicUrl')->item(0)->textContent;
	$ret['title'] = '中文广播('.$root->getElementsByTagName('Description')->item(0)->textContent.')';
	$ret['desc'] = '<br/><br/><br/><br/><br/>因软件有bug，会造成后台消耗流量，所以暂停梵蒂冈广播的收听。我们会尽快更新的，造成不便，深表遗憾！愿天主降幅你！';
	
	echo $_GET['callback'].'('.json_encode($ret).')';
?>