{{
	$.ui.titleBar.textContent = it.title;
	$("#backButton")[0].style.visibility = 'hidden';
	$("#menubadge")[0].style.float = 'left';
	dtRadio = new Date(it.item.desc);
}}
<div class="formGroupHead">{{=it.item.desc}}</div>
<audio src="{{=it.item.mp3}}" controls></audio>