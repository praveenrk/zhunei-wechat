<?php
session_start();
header("Content-type: text/html; charset=utf-8");
?>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>提交圣经金句</title>
<script type="text/javascript">
function onlogin()
{
	var user = username.value;
	var pass = password.value;
	
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			alert(xmlhttp.responseText);
			parent.location.reload();
			return;
		}
	}
	xmlhttp.open("POST","../users/login.php",true);
	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlhttp.send("submit=1&username="+user+"&password="+pass+"");
}

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

function onupdate()
{
	var xmlhttp = new XMLHttpRequest();
	var btnUpdate = document.getElementById("btnUpdate");
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			alert(xmlhttp.responseText);
			btnUpdate.disabled = false;
			return;
		}
	}
	
	btnUpdate.disabled = true;
	xmlhttp.open("POST","../lodo/update.php",true);
	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var params = "&lodo="+document.getElementById("input_lodo").value;
	xmlhttp.send(params);
}
</script>
</head>
<?php
//登录
if(!isset($_SESSION['userid'])){
    echo '
<div>
<label for="username" class="label">用户名:</label>
<input id="username" name="username" type="text"/>
<label for="password" class="label">密 码:</label>
<input id="password" name="password" type="password"/>
<input type="button" name="btnLogin" value="  确 定  " onclick="onlogin()"/>
</div>';
}
else
{
	$username = $_SESSION['name'];
	echo "欢迎你，".$username;
echo '<input type="button" name="btnLogin" value=" 注 销 " onclick="onlogout()"/>';
echo '<input type="button" name="btnUpdate" id="btnUpdate" value=" 更 新 " onclick="onupdate()"/>';
}
?>
<table style="width: 100%; height: 100%;">
    <tr>
        <td class="style1">弥撒:</td>
        <td><textarea id="input_lodo" cols="50" rows="15"></textarea></td>
    </tr>
</table>
