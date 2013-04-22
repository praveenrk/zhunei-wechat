<?php
$conn = mysql_pconnect("localhost","liyake","me_lyk");
if(!$conn)
{
	$errorcode = 3;
	die("connect db error!");
}

mysql_select_db("liyake",$conn);
?>