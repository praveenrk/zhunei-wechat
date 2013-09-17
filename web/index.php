<html>
	<head>
		<title>主内青年团微信公众首页</title>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<meta name="viewport" content="user-scalable=no, width=device-width" />
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
			<br/><br/><br/>
			<h1>梵蒂冈中文电台每日快讯</h1>
			<h2>中文广播</h2>
			<div align="center" id="mydiv">
				<audio id="audio" src="http://media01.vatiradio.va/podcast/feed/cinese_<?php echo(gmdate("dmy",time()+3600*8));?>.mp3" controls></audio>
			</div>
			</br>
			<h3>english news</h3>
			<div align="center" id="mydiv2">
				<audio id="audioen" src="http://www.vaticanradio-us.org/webcasting/rg_inglese_2_1.mp3" controls></audio>
			</div>
			<br/><br/>
			<h1>主内青年团小工具</h1>
			<h2><a href="/pray/index.php" alt="主内青年团代祷本">代祷本</a></h2>
			<br/>
			<h2><a href="/vaticanacn/index.php" alt="梵蒂冈中文快讯">梵蒂冈中文快讯</a></h2>
			</br></br>
			<h1>欢迎关注主内青年团微信公众帐号</h1>
			<p>拿起你的微信扫描下面的二维码即可关注(帐号为:<b>zhuneiqingnian</b>)，也可以在微信中搜索<b>主内青年</b>进行关注</p>
			<div><img src="/wechat/pics/qrcode.jpg" alt="主内青年团微信公众名片"></img></div>
		</center>
	</body>
	<script type="text/javascript" language="javascript" src="/include/googleanalysis.js"></script>
</html>