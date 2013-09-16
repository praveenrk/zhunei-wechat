<?php
header("Content-type: text/html; charset=utf-8");
define("ACCESS_TOKEN", "-CE_EQWa1jxr2YRJENNaLt2XwxYFjFlpyk-yDqrhdfwWvIMZIBD0HjmDp0YbrAdCgd4y3xXjzeZbMA0JEn8ka-QOWx40tfEZtgaG8HGpNMlAjpkbRenj01x-86Xm6WUyxkQTTHS_L8dZ2LhcxmsbYg");

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


$data = '{
	"button" : [
	{
		"name" : "实时更新",
		"sub_button" : [
		{
			"type" : "click",
			"name" : "中文快讯",
			"key" : "101"
		},
		{
			"type" : "click",
			"name" : "美文推荐",
			"key" : "102"
		}]
	},
	{
		"name" : "每日灵修",
		"sub_button" : [
		{
			"type" : "click",
			"name" : "弥撒及读经",
			"key" : "201"
		},
		{
			"type" : "click",
			"name" : "日课",
			"key" : "206"
		},
		{
			"type" : "click",
			"name" : "反省",
			"key" : "207"
		},
		{
			"type" : "click",
			"name" : "圣人传记",
			"key" : "208"
		},
		{
			"type" : "click",
			"name" : "礼仪",
			"key" : "209"
		}]
	},
	{
		"name" : "更多...",
		"sub_button" : [
		{
			"type" : "view",
			"name" : "梵蒂冈电台",
			"url" : "http://api.liyake.com/media/vaticanradiowebcast_cn.php"
		},
		{
			"type" : "view",
			"name" : "代祷本",
			"url" : "http://api.liyake.com/pray/index.php"
		},
		{
			"type" : "click",
			"name" : "每日圣言",
			"key" : "303"
		},
		{
			"type" : "click",
			"name" : "各地教堂",
			"key" : "304"
		}]
	}]
}';


echo createMenu($data);
echo getMenu();
//echo deleteMenu();
?>