{{
	$.ui.titleBar.textContent = it.title;
	$("#backButton")[0].style.visibility = 'hidden';
	$("#menubadge")[0].style.float = 'left';
	dtRadio = new Date(it.item.desc);
}}
<div class="main-content music-info">
	<h2>梵蒂冈中文广播({{=it.item.desc}})</h2>
	<audio src="{{=it.item.mp3}}" controls></audio>
</div>
