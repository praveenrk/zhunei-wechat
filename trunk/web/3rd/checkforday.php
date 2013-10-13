<?php
	echo('<h1>获取每日弥撒、日课</h1>');
	echo(file_get_contents('http://t.liyake.com/getstuff/checkstuff.php'));
?>