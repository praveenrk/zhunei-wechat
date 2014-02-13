/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var caAudioPlayer = null;
var caAudioTimer = null;

function onClickPlayBtn()
{
	var b = $("#playBtn").get(0);
	if(b.className.indexOf('pause')>-1)
	{
		console.log("clicked pause button");
		audioPlayer.pause();
		b.className = 'play';
	}
	else
	{
		console.log("clicked play button");
		audioPlayer.play();
		b.className = 'pause';
	}
}

function onPlayRangeChange(v)
{
	audioPlayer.seekTo(v*1000);
}

function onPlayerSuccess()
{
	audioPlayer.stop();
	console.log("Audio had played success!");
}

function onPlayerError()
{
	audioPlayer.release();
	console.log("Audio played error!");
}

function onUpdateAudioPosition()
{
	audioPlayer.getPosition(
		function(pos) {
			if (pos > -1) {
				setAudioPosition(pos);
			}
		},
		function(e) {
			console.log("Error getting pos=" + e);
		}
	);
}

function setAudioPosition(c)
{
	var t = audioPlayer.getDuration();
	var _r = $("#playRange").get(0);
	if(t>0)
	{
		if(_r.max!=t)
		{
			_r.max = t;
		}
	}
	else
	{
		_r.max = 10;
	}
	_r.value = c;
	console.log("duration:"+t+" position:"+c);
}

var audioPlayer = {
	setAudio: function(_t,_l,_p)
	{
		var player = $("#playAudio").get(0);
		audioPlayer.title = _t;
		if(_l!=audioPlayer.src)
		{
			audioPlayer.src = _l;
			player.src = _l;
		}
		$("#playTitle").get(0).textContent=_t;
		console.log("set audio to new "+audioPlayer.src);
		if(_p)
		{
			player.play();
		}
		
		/*
		if(_l!=audioPlayer.src)
		{
			audioPlayer.release();
			audioPlayer.src = _l;
			$("#playBtn").get(0).className="play";
			setAudioPosition(0);
		}
		$("#playTitle").get(0).textContent=_t;
		console.log("set audio to new "+audioPlayer.src);
		if(_p)
		{
			$("#playBtn").get(0).className = 'pause';
			audioPlayer.play();
		}*/
	},
    play: function()
	{
		if($.os.android)
		{
			if(caAudioPlayer==null)
			{
				console.log("create new media...");
				caAudioPlayer = new Media(audioPlayer.src,onPlayerSuccess,onPlayerError);
			}
			caAudioPlayer.play();
			console.log("play audio file "+audioPlayer.src);
		}
		else if($.os.ios || $.os.ios7)
		{
			if(caAudioPlayer==null)
			{
				console.log("create new media...");
				caAudioPlayer = new Media(audioPlayer.src,onPlayerSuccess,onPlayerError);
			}
			caAudioPlayer.play({ playAudioWhenScreenIsLocked : true });
			console.log("play audio file "+audioPlayer.src);
		}
		
		clearInterval(caAudioTimer);
		caAudioTimer = setInterval("onUpdateAudioPosition()",1000);
		return;
    },
	pause: function()
	{
		if(caAudioPlayer!=null)
		{
			caAudioPlayer.pause();
		}
		clearInterval(caAudioTimer);
		console.log("play audio pause");
	},
	stop: function()
	{
		if(caAudioPlayer!=null)
		{
			caAudioPlayer.stop();
		}
		clearInterval(caAudioTimer);
		$("#playBtn").get(0).className = 'play';
		console.log("play audio stop");
	},
	release: function()
	{
		if(caAudioPlayer!=null)
		{
			caAudioPlayer.release();
			caAudioPlayer = null;
		}
		clearInterval(caAudioTimer);
		$("#playBtn").get(0).className = 'play';
		console.log("play audio release");
	},
	seekTo: function(ms)
	{
		caAudioPlayer.seekTo(ms);
		console.log("play audio seek to "+ms+"ms");
	},
	getDuration: function()
	{
		try
		{
			return caAudioPlayer.getDuration();
		}
		catch(err)
		{
			return -1;
		}
	},
	getPosition: function(f1,f2)
	{
		return caAudioPlayer.getCurrentPosition(f1,f2);
	}
};
