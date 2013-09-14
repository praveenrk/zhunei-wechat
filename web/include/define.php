<?php
define("ROOT_WEB_URL", "http://api.liyake.com/");

function getWechatShareScript($link,$title,$imgurl)
{
	return '</script><script language="javascript" type="text/javascript">var contentModel={
	"img_url": "'.$imgurl.'", "img_width": "","img_height": "","link": "'.$link.'", "desc": "'.$title.'", 
    "title": "'.$title.'", "src": "天主教小助手" };</script><script language="JavaScript" src="http://mat1.gtimg.com/www/js/newsapp/wechat/wechat20130809_min.js" type="text/javascript" charset="utf-8"></script>';
}
?>