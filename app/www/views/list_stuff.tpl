{{
	$.ui.titleBar.textContent = it.title;
	$("#backButton")[0].style.visibility = 'hidden';
	$("#menubadge")[0].style.float = 'left';
	dtPrev = new Date(it.date.toDateString()); dtPrev.setDate(dtPrev.getDate()-1);
	dtNext = new Date(it.date.toDateString()); dtNext.setDate(dtNext.getDate()+1);
	nextDate = dtNext.Format("yyyy-MM-dd");
	prevDate = dtPrev.Format("yyyy-MM-dd");
}}
<div class="button-grouped flex">
	<a class="button icon left" href="/stuff/getstuff/{{=prevDate}}">上一日</a>
	<a class="button icon refresh" href="/stuff/update/{{=it.date.Format("yyyy-MM-dd")}}">{{=it.date.Format("yyyy年MM月dd日")}}</a>
	<a class="button icon right" href="/stuff/getstuff/{{=nextDate}}">下一日</a>
</div>
<div style="clear:both"></div>
<ul id="stuff-list" class="list">
	{{ for (var c in it.stuff){
	}}
	<li class="artitem"><a href="/stuff/detail/{{=it.date.Format("yyyy-MM-dd")}}/{{=c}}">
		<img src="./pics/{{=c}}.jpg" class="artimg" />
		<h3 class="arttitle">{{=stuffconfig[c]}}</h3>
		<p class="artcontent">{{=htmlencode(htmldecode(it.stuff[c]).substr(0,30))}}</p>
	</a></li>
	{{}}}
</ul>