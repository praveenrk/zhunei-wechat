{{$.ui.titleBar.textContent = it.title+"("+it.date.Format("yyyy-MM-dd")+")";}}
<ul id="stuff-list" class="list">
	{{ for (var c in it.stuff){
	}}
	<li class="artitem"><a href="/stuff/detail/{{=it.date.Format("yyyy-MM-dd")}}/{{=c}}">
		<img src="./pics/{{=c}}.jpg" class="artimg" />
		<p class="artcontent">{{=htmlencode(htmldecode(it.stuff[c]).substr(0,50))}}</p>
	</a></li>
	{{}}}
</ul>