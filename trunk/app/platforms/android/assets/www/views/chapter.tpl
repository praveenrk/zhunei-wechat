{{
	$.ui.titleBar.textContent = it.items.title;
	
	var bb = $("#backButton")[0];
	bb.href=it.items.purl;
	bb.innerHTML= it.items.ptitle;
	bb.style.visibility = 'visible';
	$("#menubadge")[0].style.float = 'right';
}}
{{ if(it.items.mp3){}}
<audio src="http://bcs.duapp.com/cathassist/bible/mp3{{=it.items.mp3}}" controls></audio>
{{}}}
<p class="bc">
	{{=it.items.text}}
</p>