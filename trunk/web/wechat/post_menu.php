<?php
header("Content-type: text/html; charset=utf-8");
define("ACCESS_TOKEN", "YcqeSuXlPCFkf39IWk_CsqtBYQQLPyX9xVqga_McfDfwTZiuXJF94x_5D6lfW5d1zUxP1mp3UQemYo5eiUKA8J_OniM1oL2kncTCCA3Gpmp2xvLfjl4snM6Ue9Hi2dkgaT2Mk1jSNJZiaGgw0-4ZMw");

//创建菜单
function createMenu($data){
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=".ACCESS_TOKEN);
	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
	curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
	curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (compatible; MSIE 5.01; Windows NT 5.0)');
	curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
	curl_setopt($ch, CURLOPT_AUTOREFERER, 1);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	$tmpInfo = curl_exec($ch);
	if (curl_errno($ch)) {
	  return curl_error($ch);
	}
	curl_close($ch);
	return $tmpInfo;
}
//获取菜单
function getMenu(){
	return file_get_contents("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=".ACCESS_TOKEN);
}
//删除菜单
function deleteMenu(){
	return file_get_contents("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=".ACCESS_TOKEN);
}


$data = file_get_contents('menu.json');


echo createMenu($data);
echo getMenu();
//echo deleteMenu();
?>