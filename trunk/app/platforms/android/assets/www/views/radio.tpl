{{
	$.ui.titleBar.textContent = it.title;
	$("#backButton")[0].style.visibility = 'hidden';
	$("#menubadge")[0].style.float = 'left';
	dtRadio = new Date(it.item.title);
}}
<div class="main-content music-info">
	<h2>{{=it.item.title}}</h2>
	<div class="button-grouped flex">
		<a class="button" href="#" onclick="audioPlayer.setAudio('{{=it.item.title}}','{{=it.item.url}}',true);">播放广播</a>
	</div>
</div>
