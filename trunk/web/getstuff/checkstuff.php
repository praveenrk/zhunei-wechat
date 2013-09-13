<?php
	require_once("../include/define.php");
	
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
	$index = 0;
	do
	{
		$datestr = gmdate("Y-m-d",time()+3600*8+$index*3600*24);
		foreach ($modemap as $key => $value)
		{
			$filestr = 'stuff/'.$datestr.'_'.$value.'.html';
			if(!file_exists($filestr))
			{
				$fp = fopen($filestr,"w");
				if(!$fp)
				{
					echo "System Error";
					die();
				}
				else
				{
					$url = ROOT_WEB_URL."getstuff/getstuff.php?date=".$datestr."&mode=".$value;
					$contents = file_get_contents($url); 
					fwrite($fp,$contents);
					fclose($fp);
					echo("generate file '".$filestr."'<br/>");
				}
			}
		}
		
		$index++;
	}
	while($index<5);
	//检查梵蒂冈广播电台
	file_get_contents("http://api.liyake.com/vaticanacn/checkrss.php");
	
	echo("<br/><br/><h1>done</h1>");
	echo('<script type="text/javascript" language="javascript" src="/include/googleanalysis.js"></script>');
?>