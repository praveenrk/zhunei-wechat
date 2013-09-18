<?php
//define your token
require_once("../include/dbconn.php");
require_once("../include/define.php");
require_once("html2text.php");
define("TOKEN", "wechat");

$wechatObj = new wechatCallbackapiTest();
if($wechatObj->checkSignature())
{
	$wechatObj->responseMsg();
}

class wechatCallbackapiTest
{
	public function valid()
    {
        $echoStr = $_GET["echostr"];

        //valid signature , option
        if($this->checkSignature())
		{
        	echo $echoStr;
        	exit;
        }
    }

    public function responseMsg()
    {
		//get post data, May be due to the different environments
		$postStr = $GLOBALS["HTTP_RAW_POST_DATA"];
      	//extract post data
		if (!empty($postStr))
		{
			$resultStr = "";
			
			$postObj = simplexml_load_string($postStr, 'SimpleXMLElement', LIBXML_NOCDATA);
			if($postObj->Event=="CLICK")
			{
				$resultStr = $this->getEventReply($postObj);
			}
			else
			{
				if($postObj->MsgType=="text")
				{
					$resultStr = $this->getTextReply($postObj);
				}
				else
				{
					$resultStr = $this->getDefaltReply($postObj);
				}
			}
			
			//将回复插入到数据库中
			$this->insertIntoDb("",$postStr,$resultStr);
			echo $resultStr;
			exit;
        }
		else
		{
        	echo "";
        	exit;
        }
    }
		
	public function checkSignature()
	{
        $signature = $_GET["signature"];
        $timestamp = $_GET["timestamp"];
        $nonce = $_GET["nonce"];
		
		$token = TOKEN;
		$tmpArr = array($token, $timestamp, $nonce);
		sort($tmpArr);
		$tmpStr = implode( $tmpArr );
		$tmpStr = sha1( $tmpStr );
		
		if( $tmpStr == $signature ){
			return true;
		}else{
			return false;
		}
	}
	
	private function getDefaltReply($postObj,$desc='')
	{
		$fromUsername = $postObj->FromUserName;
		$toUsername = $postObj->ToUserName;
		$time = time();
		if($desc=='')
			$desc = "感谢你的关注，我们正在努力完善『天主教小助手』，后续会为你带来更多的好功能！耶稣爱你！ ^_^";
		$textTpl = '<xml>
			<ToUserName><![CDATA[%s]]></ToUserName>
			<FromUserName><![CDATA[%s]]></FromUserName>
			<CreateTime>%s</CreateTime>
			<MsgType><![CDATA[text]]></MsgType>
			<Content><![CDATA[%s]]></Content>
			<MsgId>1234567890123456</MsgId>
			</xml>';
		return sprintf($textTpl, $fromUsername, $toUsername, $time, $desc);
	}
	
	private function getEventReply($postObj)
	{
		$key = $postObj->EventKey;
		
		$ArtCount = 0;
		$Articles = "";
		$resultStr='';
		
		if($key=='102')
		{
			//美文推荐
			return $this->getDefaltReply($postObj,'『美文推荐』模块正在努力设计中...感谢你的关注！');
		}
		else if($key=='201')
		{
			//日课及读经
			$modemap = array (
			'1' => 'mass',
			'2' => 'med',
			'3' => 'lod',
			'4' => 'ves',
			'5' => 'comp',
			'6' => 'let',
			'7' => 'thought',
			'8' => 'ordo',
			'9' => 'saint',
			);
			
			$url = ROOT_WEB_URL."getstuff/getstuff.php?date=".gmdate("Y-m-d",time()+3600*8);
			$json = json_decode(file_get_contents($url),true);
			foreach ($modemap as $key => $value)
			{
				if(isset($json[$value]))
				{
					$Articles = $Articles.$this->getSubArticle($value,trim(convert_html_to_text($json[$value])),0,0);
					$ArtCount = $ArtCount+1;
				}
			}
		}
		else if($key=='303')
		{
			//圣经金句
			$onelodo = "";
			$result = mysql_query("select lodo from lodo L JOIN (SELECT CEIL(MAX(ID)*RAND()) AS ID FROM lodo) AS m ON L.ID >= m.ID LIMIT 1;");
			if ($row = mysql_fetch_array($result))
			{
				$onelodo = $row['lodo'];
			}
			return $this->getDefaltReply($postObj,$onelodo);
		}
		else if($key=='304')
		{
			//各地教堂
			return $this->getDefaltReply($postObj,'『各地教堂』模块正在努力设计中...后续会在该模块中展示各地的教堂及弥撒时间，感谢你的关注！');
		}
		
		if($Articles!="")
		{
			$textTpl = '<xml>
				<ToUserName><![CDATA[%s]]></ToUserName>
				<FromUserName><![CDATA[%s]]></FromUserName>
				<CreateTime>%s</CreateTime>
				<MsgType><![CDATA[news]]></MsgType>
				<ArticleCount>%s</ArticleCount>
				<Articles>%s
				</Articles>
				<FuncFlag>1</FuncFlag>
				</xml>';
			return sprintf($textTpl, $postObj->FromUserName, $postObj->ToUserName, time(), $ArtCount, $Articles);
		}
		else
		{
			return $this->getDefaltReply($postObj);
		}
	}
	
	private function getTextReply($postObj)
	{
		$modemap = array (
		'1' => 'mass',
		'2' => 'med',
		'3' => 'lod',
		'4' => 'ves',
		'5' => 'comp',
		'6' => 'let',
		'7' => 'thought',
		'8' => 'ordo',
		'9' => 'saint',
		);
		
		$fromUsername = $postObj->FromUserName;
		$toUsername = $postObj->ToUserName;
		$time = time();
		
		$ArtCount = 0;
		$Articles = "";
		$keyword = trim($postObj->Content);
		if( (strpos($keyword,"经")!==false) or (strpos($keyword,"欢喜")!==false) or (strpos($keyword,"痛苦")!==false) or (strpos($keyword,"荣福")!==false) or (strpos($keyword,"光明")!==false) or (strpos($keyword,"玫瑰")!==false) or (strpos($keyword,"颂")!==false) or (strpos($keyword,"诵")!==false) )
			$keyword = "12";
		
		if($keyword=="0")
		{
			$url = ROOT_WEB_URL."getstuff/getstuff.php?date=".gmdate("Y-m-d",time()+3600*8);
			$json = json_decode(file_get_contents($url),true);
			foreach ($modemap as $key => $value)
			{
				if(isset($json[$value]))
				{
					$Articles = $Articles.$this->getSubArticle($value,trim(convert_html_to_text($json[$value])),0,0);
					$ArtCount = $ArtCount+1;
				}
			}
			{			
				//增加代祷本的第一项
				$textTpl = '<item><Title><![CDATA[%s]]></Title><Url><![CDATA['.ROOT_WEB_URL.'pray/index.php]]></Url><Description><![CDATA[来自于代祷本]]></Description><PicUrl><![CDATA['.ROOT_WEB_URL.'wechat/pics/comp1.jpg]]></PicUrl></item>';
				
				$pray = $this->getPrays(1);
				$index = strpos($pray,"\n",30);
				$title = "代祷意向:\n";
				if($index>-1 and $index<100)
				{
					$title = $title.substr($pray,0,$index);
				}
				else
				{
					$title = $title.mb_substr($pray,0,30,"UTF-8");
				}
				$Articles = $Articles.sprintf($textTpl,$title);
				$ArtCount = $ArtCount+1;
			}
		}
		else if(isset($modemap[$keyword]))
		{
			for($i=0;$i<3;$i++)
			{
				if($i==0)
				{
					$Articles = $Articles.$this->getSubArticle($modemap[$keyword],"",1,$i);
				}
				else
				{
					$Articles = $Articles.$this->getSubArticle($modemap[$keyword],"",0,$i);
				}
				$ArtCount = $ArtCount+1;
			}
		}
		else if($keyword=="10")
		{
			$textTpl = '<xml>
				<ToUserName><![CDATA[%s]]></ToUserName>
				<FromUserName><![CDATA[%s]]></FromUserName>
				<CreateTime>%s</CreateTime>
				<MsgType><![CDATA[news]]></MsgType>
				<ArticleCount>1</ArticleCount>
				<Articles>
				<item><Title><![CDATA[代祷意向]]></Title><Url><![CDATA['.ROOT_WEB_URL.'pray/index.php]]></Url><Description><![CDATA[%s]]></Description><PicUrl><![CDATA['.ROOT_WEB_URL.'wechat/pics/comp_l1.jpg]]></PicUrl></item>
				</Articles>
				<FuncFlag>1</FuncFlag>
				</xml>';
			$resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, $this->getPrays(5));
			return $resultStr;
		}
		else if($keyword=="11")
		{
			$textTpl = '<xml>
				<ToUserName><![CDATA[%s]]></ToUserName>
				<FromUserName><![CDATA[%s]]></FromUserName>
				<CreateTime>%s</CreateTime>
				<MsgType><![CDATA[news]]></MsgType>
				<ArticleCount>1</ArticleCount>
				<Articles>
				<item><Title><![CDATA[梵蒂冈中文电台每日快讯]]></Title><Url><![CDATA['.ROOT_WEB_URL.'media/vaticanradiowebcast_cn.php]]></Url><Description><![CDATA[%s]]></Description><PicUrl><![CDATA['.ROOT_WEB_URL.'wechat/pics/radiovatican.png]]></PicUrl></item>
				</Articles>
				<FuncFlag>1</FuncFlag>
				</xml>';
			$resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, "点此可进入收听页面\n温馨提示：播放音频会损耗较多流量");
			return $resultStr;
		}
		else if($keyword=="12")
		{
			$textTpl = '<xml>
				<ToUserName><![CDATA[%s]]></ToUserName>
				<FromUserName><![CDATA[%s]]></FromUserName>
				<CreateTime>%s</CreateTime>
				<MsgType><![CDATA[news]]></MsgType>
				<ArticleCount>1</ArticleCount>
				<Articles>
				<item><Title><![CDATA[常用经文]]></Title><Url><![CDATA['.ROOT_WEB_URL.'prayer/index.html]]></Url><Description><![CDATA[%s]]></Description><PicUrl><![CDATA['.ROOT_WEB_URL.'wechat/pics/med_l1.jpg]]></PicUrl></item>
				</Articles>
				<FuncFlag>1</FuncFlag>
				</xml>';
			$resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, "天主教常用经文，详情请点击我");
			return $resultStr;
		}
		else if($keyword=="13")
		{
			$textTpl = '<xml>
				<ToUserName><![CDATA[%s]]></ToUserName>
				<FromUserName><![CDATA[%s]]></FromUserName>
				<CreateTime>%s</CreateTime>
				<MsgType><![CDATA[news]]></MsgType>
				<ArticleCount>1</ArticleCount>
				<Articles>
				<item><Title><![CDATA[推荐给好友]]></Title><Url><![CDATA['.ROOT_WEB_URL.'wechat/help.html]]></Url><Description><![CDATA[%s]]></Description><PicUrl><![CDATA['.ROOT_WEB_URL.'wechat/pics/logo.jpg]]></PicUrl></item>
				</Articles>
				<FuncFlag>1</FuncFlag>
				</xml>';
			$resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, "详情请点击我");
			return $resultStr;
		}
		
		if($Articles!="")
		{
			$textTpl = '<xml>
				<ToUserName><![CDATA[%s]]></ToUserName>
				<FromUserName><![CDATA[%s]]></FromUserName>
				<CreateTime>%s</CreateTime>
				<MsgType><![CDATA[news]]></MsgType>
				<ArticleCount>%s</ArticleCount>
				<Articles>%s
				</Articles>
				<FuncFlag>1</FuncFlag>
				</xml>';
			$resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, $ArtCount, $Articles);
		}
		else
		{
			$resultStr = $this->getDefaltReply($postObj);
		}
		return $resultStr;
	}
	
	private function getPrays($count)
	{
		$article = "";
		//先从数据库中获取
		$result = mysql_query("select name,text,createtime from pray order by id desc limit ".$count.";");
		while ($row = mysql_fetch_array($result))
		{
			if($article!="")
				$article = $article."\n\n";
			if($count==1)
			{
				$article = $row['text'];
			}
			else
			{
				$article = $article.'昵称：'.$row['name'].'  时间：'.date('m-d H:i',strtotime($row['createtime'])+3600*8)."\n".$row['text'];
			}
		}
		return $article;
	}
	
	private function getSubArticle($mode,$content,$isLarge,$offset)
	{
		$titlemap = array (
		'mass' => '弥撒及读经',
		'med' => '日祷经文',
		'lod' => '晨祷经文',
		'ves' => '晚祷经文',
		'comp' => '夜祷经文',
		'let' => '诵读',
		'thought' => '反省',
		'ordo' => '礼仪',
		'saint' => '圣人传记',
		);
		$title = "";
		$url = ROOT_WEB_URL."getstuff/stuff/".gmdate("Y-m-d",time()+3600*8+($offset*24*3600))."_".$mode.".html";
		if($content=="")
		{
			$content = convert_html_to_text(file_get_contents($url));
		}
		$picurl = ROOT_WEB_URL."wechat/pics/".$mode."1.jpg";
		if(isset($titlemap[$mode]))
			$title=$titlemap[$mode];
		$textTpl = '<item><Title><![CDATA[%s]]></Title><Url><![CDATA[%s]]></Url><Description><![CDATA[%s]]></Description><PicUrl><![CDATA[%s]]></PicUrl></item>';
		$resultStr = "";
		if($isLarge>0)
		{
			$picurl = ROOT_WEB_URL."wechat/pics/".$mode."_l1.jpg";
			$index = strpos($content,"\n",140);
			$desc = "";
			if($index>0)
			{
				$title = $title."\n".substr($content,0,$index);
				$desc = substr($content,0,$index);
			}
			else
				$desc = mb_substr($content,0,20,"UTF-8");
				
			$resultStr = sprintf($textTpl,$title,$url,$desc, $picurl);
		}
		else
		{
			$index = strpos($content,"\n",20);
			if($index>-1 and $index<100)
			{
				$title = $title."\n".substr($content,0,$index);
			}
			else
			{
				$title = $title."\n".mb_substr($content,0,30,"UTF-8");
			}
			$resultStr = sprintf($textTpl,$title,$url,mb_substr($content,0,30,"UTF-8"), $picurl);
		}
		return $resultStr;
	}
	
	private function insertIntoDb($get,$post,$result)
	{
		return;
		//检测用户名及密码是否正确
		$result = mysql_query("insert into wechat (get,post) values ('".$result."','".$post."');");
	}
}
?>