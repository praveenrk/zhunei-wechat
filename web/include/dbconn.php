<?php
$conn = mysql_pconnect("localhost","gospel","gospel");
if(!$conn)
{
	$errorcode = 3;
	die("connect db error!");
}

mysql_select_db("liyake",$conn);
?>