<?php
	echo('<h1>获取梵蒂冈电台文章</h1>');
	echo(file_get_contents('http://t.liyake.com/vaticanacn/checkrss.php'));
	
	echo('<h1>获取信德网文章</h1>');
	echo(file_get_contents('http://t.liyake.com/faithlife/caiji.php'));
?>