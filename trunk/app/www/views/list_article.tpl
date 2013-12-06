{{
	$.ui.titleBar.textContent = it.title;
	$("#backButton")[0].style.visibility = 'hidden';
	$("#menubadge")[0].style.float = 'left';
}}
<ul id="stuff-list" class="list">
	{{ for (var c in it.items){
	}}
	<li class="artitem"><a href="/article/vaticanacn_item/{{=it.items[c]['id']}}">
		<img src="{{=it.items[c]['pic']}}" class="artimg" />
		<h3 class="arttitle">{{=it.items[c]['title']}}</h3>
		<p class="artcontent">{{=it.items[c]['title']}}</p>
	</a></li>
	{{}}}
</ul>