<?php
	require_once("../include/dbconn.php");
	require_once("../include/define.php");
	
	function getList($from,$count)
	{
		
	}
	
	if(array_key_exists("type",$_GET))
	{
		if($_GET["type"]=="list")
		{
			$from = 0;
			$count = 10;
			echo getList($from,$count);
		}
	}
	if(array_key_exists("mode",$_GET))
	{
		$mode = $_GET["mode"];
	}
	
?>