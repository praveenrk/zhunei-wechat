{{
	$.ui.titleBar.textContent = it.title;
	$("#backButton")[0].style.visibility = 'hidden';
	$("#menubadge")[0].style.float = 'left';
}}

<h2>主题设置</h2>
<ul class="list">
	 <li>
		<a href="javascript:setTheme('ios')" data-ignore="true">iOS theme</a>
	</li>
	<li>
		<a href="javascript:setTheme('android')" data-ignore="true">Android theme</a>
	</li>
	<li>
		<a href="javascript:setTheme('win8')" data-ignore="true">Win8 theme</a>
	</li>
	<li>
		<a href="javascript:setTheme('bb')" data-ignore="true">BB10 theme</a>
	</li>
   <li>
		<a href="javascript:setTheme('ios7')" data-ignore="true">ios7</a>
	</li>
</ul>
<h2>其它</h2>
<div class="input-group">
	<a class="button icon" href="javascript:localDB.clearAll()">清空数据库</a>
</div>