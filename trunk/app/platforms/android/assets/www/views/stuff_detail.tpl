{{
	$.ui.titleBar.textContent = stuffconfig[it.type];
	var bb = $("#backButton")[0];
	bb.href="/stuff/getstuff/"+it.date;
	bb.innerHTML= '日课...';
	bb.style.visibility = 'visible';
	$("#menubadge")[0].style.float = 'right';
}}
<div id="stuff_detail">
{{=replackAudioTag(it.stuff)}}
</div>