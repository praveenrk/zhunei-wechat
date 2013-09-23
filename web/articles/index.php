<?php
require_once("../include/dbconn.php");
require_once("../include/define.php");
session_start();
header("Cache-Control: no-cache, must-revalidate");
header("Pragma: no-cache");
header("Content-type: text/html; charset=utf-8");
?>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>update</title>
<script type="text/javascript" src="scripts/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="scripts/jHtmlArea-0.7.5.js"></script>
<script type="text/javascript" src="WdatePicker/WdatePicker.js"></script>
<link rel="Stylesheet" type="text/css" href="style/jHtmlArea.css" />
<script type="text/javascript">
function onlogout()
{
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			parent.location.reload();
			return;
		}
	}
	xmlhttp.open("POST","../users/logout.php",true);
	xmlhttp.send();
}
</script>
<?php
//登录
if(isset($_SESSION['userid']))
{
	$username = $_SESSION['name'];
	echo "欢迎你，".$username;
	echo '<input type="button" name="btnLogin" value=" 注 销 " onclick="onlogout()"/>';
}
?>