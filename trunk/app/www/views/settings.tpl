{{
	$.ui.titleBar.textContent = it.title;
	$("#backButton")[0].style.visibility = 'hidden';
	$("#menubadge")[0].style.float = 'left';
}}
<div class="input-group">
	<a class="button icon right" href="javascript:localDB.clearAll()">清空数据库</a>
</div>