<html>
	<head>
		<title>天主教小助手微信公众首页</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta name="viewport" content="user-scalable=no, width=device-width" />
		<meta http-equiv="keywords" content="天主教小助手、天主教微信、天主教、微信、微信公众号、服务号"/>
		<meta http-equiv="description" content="天主教小助手微信公众服务号、提供每日日课及读经、圣人传记、梵蒂冈广播、信仰分享、思高圣经、天主教教理等内容"/>
		<link href="/css/index.css" type="text/css" rel="stylesheet">
	</head>
	<body>
		<center>
			<?php
				require_once("include/define.php");
				require_once("include/dbconn.php");
				$result = mysql_query("select lodo from lodo L JOIN (SELECT CEIL(MAX(ID)*RAND()) AS ID FROM lodo) AS m ON L.ID >= m.ID LIMIT 1;");
				if ($row = mysql_fetch_array($result))
				{
					echo('<h3 style="color:red;">'.$row['lodo'].'</h3>');
				}
				$modemap = array (
					'弥撒及读经' => 'mass',
					'日祷' => 'med',
					'晨祷' => 'lod',
					'晚祷' => 'ves',
					'夜祷' => 'comp',
					'诵读' => 'let',
					'反省' => 'thought',
					'礼仪' => 'ordo',
					'圣人传记' => 'saint',
					);
				echo "<h1>天主教每日弥撒及祷告</h1><h2>今天（".gmdate('Y年m月d日',time()+3600*8)."）</h2>";
				$datestr = gmdate("Y-m-d",time()+3600*8);
				foreach ($modemap as $key => $value)
				{
					echo('<a href="/getstuff/stuff/'.$datestr.'_'.$value.'.html">'.$key.'</a></br>');
				}
				echo "<br/><h2>明天（".gmdate('Y年m月d日',time()+3600*32)."）</h2>";
				$datestr = gmdate("Y-m-d",time()+3600*32);
				foreach ($modemap as $key => $value)
				{
					echo('<a href="/getstuff/stuff/'.$datestr.'_'.$value.'.html">'.$key.'</a></br>');
				}
				echo "<br/><h2>后天（".gmdate('Y年m月d日',time()+3600*56)."）</h2>";
				$datestr = gmdate("Y-m-d",time()+3600*56);
				foreach ($modemap as $key => $value)
				{
					echo('<a href="/getstuff/stuff/'.$datestr.'_'.$value.'.html">'.$key.'</a></br>');
				}
			?>
			<br/><br/>
			<h1>天主教小助手小工具</h1>
			<h2><a href="/bible/bible/" alt="思高版圣经（MP3音频）">思高版圣经（MP3音频）</a></h2>
			<h2><a href="/media/vaticanradio.html" alt="梵蒂冈广播电台">梵蒂冈广播电台</a></h2>
			<h2><a href="/pray/index.php" alt="天主教小助手代祷本">代祷本</a></h2>
			<h2><a href="/vaticanacn/index.php" alt="梵蒂冈中文快讯">梵蒂冈中文快讯</a></h2>
			<h2><a href="/faithlife/index.php" alt="信仰生活（来源信德网）">信仰生活（来源信德网）</a></h2>
			<h2><a href="/articles/index.php" alt="主内分享">主内分享（小助手推荐）</a></h2>
			<h2><a href="/music/music.php" alt="教会歌曲">随机听歌（放松一下）</a></h2>
			<h2><a href="/3rd/more.html" alt="更多小工具">更多小工具</a></h2>
			</br></br>
			<h1>应用下载</h1>
			<h2><a href="http://bible.cathassist.org/" alt="圣经小助手">圣经小助手</a></h2>
			<h2><a href="http://app.cathassist.org/desc.html" alt="天主教小助手">天主教小助手</a></h2>
			</br></br>
			<h1>欢迎关注天主教小助手微信公众帐号</h1>
			<p>拿起你的微信扫描下面的二维码即可关注(帐号为:<b>iGospel</b>)，也可以在微信中搜索<b>天主教小助手</b>进行关注</p>
			<div><img src="/wechat/pics/qrcode.jpg" alt="天主教小助手微信公众名片"></img></div>
			<div class="links">常用链接：<a href="http://zh.radiovaticana.va/" target="_blank">梵蒂冈广播电台</a><a href="http://www.chinacatholic.org/" target="_blank">信德网</a><a href="http://www.chinacath.org/" target="_blank">天主教在线</a><a href="http://blog.sina.com.cn/along999" target="_blank">光与爱之家（博客）</a></div>
		</center>
	</body>
	<script type="text/javascript" language="javascript" src="/include/googleanalysis.js"></script>
</html>