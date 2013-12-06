{{
	$.ui.titleBar.textContent = it.item.title;
	var bb = $("#backButton")[0];
	bb.href="/article/vaticanacn/"+(parseInt(art_next_to)+1);
	bb.innerHTML= '普世教会';
	bb.style.visibility = 'visible';
	$("#menubadge")[0].style.float = 'right';
}}
<div id="stuff_detail">
{{=it.item.content}}
</div>