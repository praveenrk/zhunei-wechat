<?php 
require '..'.DIRECTORY_SEPARATOR.'include'.DIRECTORY_SEPARATOR.'common.inc.php';
include WX_PATH.'include'.DIRECTORY_SEPARATOR.'collection.class.php';

$urls=array(
	'1'=>'http://www.chinacatholic.org/index.php?m=content&c=rss&rssid=16',//人生信仰
	'2'=>'http://www.chinacatholic.org/index.php?m=content&c=rss&rssid=27',//福音传播
	'3'=>'http://www.chinacatholic.org/index.php?m=content&c=rss&rssid=33',//信德文萃
	'4'=>'http://www.chinacatholic.org/index.php?m=content&c=rss&rssid=39',//礼仪生活
);
$cjconfig=array('sourcecharset'=>'utf-8','sourcetype'=>4);
$ctypearr=array(
'1'=>'人生信仰',
'2'=>'福音传播',
'3'=>'信德文萃',
'4'=>'礼仪生活',
);

function get_content($url){
	
	if(empty($url)) return false;
	$data=array();

	$html = @file_get_contents($url);
	
	$html = explode('<p style="font-size:12px;font-weight:normal;">', $html);
	if(is_array($html)) $html1 = explode('</p>', $html[1]);
	$str=$html1[0];
	$strarr=explode('|',$str);
	
	$data['inputtime'] = trim($strarr[0]);
	//$authorarr = explode("：",$strarr[1]);
	//$data['author'] = trim($authorarr[1]);
	//$comeformarr =explode("：",$strarr[2]);
	//$data['comefrom']=trim($comeformarr[1]);


	$html =explode('div class="content">',$html[1]);
	//print_r($html[1]);exit;
	if(is_array($html)) $html = explode('<div class="bk10"></div>', $html[1]);
	$data['content']=$html[0];
	//print_r($html[0]);exit;
	return $data;
}


@set_time_limit(600);
foreach($urls as $k=>$url_list){

	$url = collection::get_url_lists($url_list, $cjconfig);
	//var_dump($url );exit;
	if (is_array($url) && !empty($url)){
		foreach ($url as $v) {
			if (empty($v['url']) || empty($v['title'])) continue;
			//$v = new_addslashes($v);
			$v['title'] = strip_tags($v['title']);
			$md5 = md5($v['url']);
			if ( !$db->get_one('id','faithlife'," md5url='$md5' ") ) {
				
				$cinfo = get_content($v['url']);//获取发布时间、作者、来源、内容

				//生成静态页面
				ob_start();
				include WX_PATH.'faithlife'.DIRECTORY_SEPARATOR.'show.tpl.php';

				$file='faithlife'.DIRECTORY_SEPARATOR.date('Y').DIRECTORY_SEPARATOR.date('m').DIRECTORY_SEPARATOR.md5($v['url']).'.html';
				$nurl=WX_PATH.$file;
				createhtml($nurl);
				
				$addinfo = array('md5url'=>$md5, 'ctype'=>$k, 'furl'=>$v['url'], 'title'=>$v['title'], 'nurl'=>$file, 'inputtime'=>$cinfo['inputtime']);
				$addinfo = new_addslashes($addinfo);

				$db->insert($addinfo,'faithlife');
					

			}
		}
	}

}
?>