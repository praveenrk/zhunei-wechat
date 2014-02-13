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

var playButton = document.getElementById('playbutton');
var stopButton = document.getElementById('stopbutton');
var activityIndicator = document.getElementById('activityindicator');
var playRange = document.getElementById('playRange');
stopButton.style.display = 'none';
activityIndicator.style.display = 'none';
playButton.style.display = 'block';
var audioSrcLink = "";



function onError(error) 
{
	console.log(error.message);
}

function onConfirmRetry(button) {
	if (button == 1) {
		audioPlayer.play();
	}
}

function pad2(number) {
	return (number < 10 ? '0' : '') + number
}

function onPlayRangeChange(v)
{
	audioPlayer.seekTo(v);
}

var myaudio = new Audio();
var isPlaying = false;
var readyStateInterval = null;

var audioPlayer = {
	setAudio: function(_t,_l,_p)
	{
		myaudio.title = _t;
		if(_l!=audioSrcLink)
		{
			audioSrcLink = _l;
			playRange.value = 0;
		}
		$("#playTitle").get(0).textContent=_t;
		console.log("set audio to new "+myaudio.src);
		if(_p)
		{
			audioPlayer.play();
		}
	},
	play: function()
	{
		isPlaying = true;
		if(myaudio.src!=audioSrcLink)
		{
			myaudio.src = audioSrcLink;
		}
		myaudio.play();
	
		readyStateInterval = setInterval(function(){
			 if (myaudio.readyState <= 2) {
				playButton.style.display = 'none';
				activityIndicator.style.display = 'block';
				playRange.value = 0;
			 }
		},1000);
		myaudio.addEventListener("timeupdate", function() {
			 if (isPlaying && myaudio.currentTime > 0)
			 {
				playRange.value = myaudio.currentTime;
				playRange.max = myaudio.duration;
			 }
		}, false);
		myaudio.addEventListener("error", function() {
			 console.log('myaudio ERROR');
		}, false);
		myaudio.addEventListener("canplay", function() {
			 console.log('myaudio CAN PLAY');
		}, false);
		myaudio.addEventListener("waiting", function() {
			 //console.log('myaudio WAITING');
			 isPlaying = false;
			 playButton.style.display = 'none';
			 stopButton.style.display = 'none';
			 activityIndicator.style.display = 'block';
		}, false);
		myaudio.addEventListener("playing", function() {
			 isPlaying = true;
			 playButton.style.display = 'none';
			 activityIndicator.style.display = 'none';
			 stopButton.style.display = 'block';
		}, false);
		myaudio.addEventListener("ended", function() {
			 audioPlayer.stop();
		}, false);
	},
	pause: function() {
		isPlaying = false;
		clearInterval(readyStateInterval);
		myaudio.pause();
		stopButton.style.display = 'none';
		activityIndicator.style.display = 'none';
		playButton.style.display = 'block';
	},
	stop: function() {
		isPlaying = false;
		clearInterval(readyStateInterval);
		myaudio.pause();
		stopButton.style.display = 'none';
		activityIndicator.style.display = 'none';
		playButton.style.display = 'block';
		myaudio = null;
		myaudio = new Audio(audioSrcLink);
		playRange.value = 0;
	},
	seekTo: function(v){
		if(myaudio.duration)
		{
			myaudio.currentTime = v;
		}
	}
};

{
	var dtNow = new Date();
	var title = "今日读经("+dtNow.Format("yyyy-MM-dd")+")";
	var link = "http://bcs.duapp.com/cathassist/thought/mp3/"+dtNow.Format("yyyy-MM-dd")+".mp3";
	audioPlayer.setAudio(title,link,false);
}