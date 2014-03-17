{{
	$.ui.titleBar.textContent = it.title;
	$("#backButton")[0].style.visibility = 'hidden';
	$("#menubadge")[0].style.float = 'left';
}}
<div class="main-content music-info">
	<h3>歌名：{{=it.item.name}}</h3>
	<h3>专辑：{{=it.item.alume}}</h3>
	<h3>歌手：{{=it.item.singer}}</h3>
	<img src="{{=it.item.pic}}"></img>
</div>
<div class="button-grouped flex">
	<a class="button" href="javascript:void(0)" onclick="audioPlayer.setAudio('{{=it.item.name}}','{{=it.item.mp3}}',true,getAudioMusicNext,null);">播放歌曲</a>
	<a class="button icon right" href="/music">下一首</a>
</div>