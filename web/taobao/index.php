<?php
require_once("../include/define.php");
if(is_weixin())
{
	echo('<html><head>
	<title>天主教小助手淘宝店</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta name="viewport" content="user-scalable=no, width=device-width" />
	<style type="text/css">
	h4{color:rgb(222,0,0);}
	p{font-size:18px;color:rgb(33,33,33);}
	span.key{color:rgb(222,0,0);}
	p.tips{font-size:15px;color:rgb(128,0,0);}
	</style></head><body><h1>【天主教小助手】淘宝店开张了！</h1><p>可通过以下几种方式进入店铺：</p>
	<p>1、打开淘宝网或淘宝手机客户端，搜索“<span class="key">天主教小助手</span>”，进入店铺。</p>
	<p>2、点击右上角的【...】按钮，选择『<span class="key">在浏览器中打开</span>』（安卓）或『<span class="key">在Safari中打开</span>』（苹果产品），即可进入【天主教小助手】店铺。</p>
	<br/><br/><br/><p class="tips">感谢大家一致以来的支持，我们会为大家带来更多精品。主佑平安！</p></body><script type="text/javascript" language="javascript" src="http://www.cathassist.org/include/googleanalysis.js"></script></html>');
}
else
{
	header("Location: http://shop114244839.taobao.com/");
}
?>