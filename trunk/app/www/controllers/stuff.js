Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function htmlencode(s){
    var div = document.createElement('div');
    div.appendChild(document.createTextNode(s));
	var e = div.innerHTML;
	e=e.replace(/\r\n/g,"</br>")  
	e=e.replace(/\n/g,"</br>");
    return e;
}
function htmldecode(s)
{
    var div = document.createElement('div');
    div.innerHTML = s;
    return div.innerText || div.textContent;
}


var stuffconfig = {'mass':'弥撒','med':'日祷','comp':'夜祷','let':'诵读','lod':'晨祷','thought':'反省','ordo':'礼仪','ves':'晚祷','saint':'圣人传记'};
$.mvc.controller.create('stuff', {
    views: {
        "list_stuff_tpl": "views/list_stuff.tpl",
		"stuff_detail_tpl": "views/stuff_detail.tpl"
    },
    default:function() {
		$.ui.showMask();
		var dtNow = new Date();
		stuffs.fetchStuff(dtNow.Format("yyyy-MM-dd"),'',function(all) {
            $("#main").html($.template('list_stuff_tpl', {
                title: '日课及读经',
                stuff: all,
				date: dtNow
            }));
		});
		$.ui.hideMask();
		$.ui.scrollToTop("#mainc",-10);
		if($.ui.isSideMenuOn())
			$.ui.toggleSideMenu();
    },
    /* This is executed when the controller is created.  It assumes the views are loaded, but can not interact with models
     * This is useful for wiring up page events, etc.
     */
    init: function() {
        var self = this;
    },
	detail: function(_d,_t)
	{
		stuffs.fetchStuff(_d,_t,function(t) {
			$("#main").html($.template('stuff_detail_tpl', {
                type: _t,
                stuff: t,
				date: _d
            }));
		});
		
		$.ui.scrollToTop("#mainc",-10);
		if($.ui.isSideMenuOn())
			$.ui.toggleSideMenu();
	}
});


$.mvc.controller.create('article', {
    views: {
        "list_tpl": "views/list.tpl",
        "list_item":"views/list_item.tpl"
    },
    default:function() {
		myRemoteAdapter.fetchAll('stuff',function(all) {
            $("#main").html($.template('list_tpl', {
                title: '日课及读经',
                items: all
            }));
		});
    },
    /* This is executed when the controller is created.  It assumes the views are loaded, but can not interact with models
     * This is useful for wiring up page events, etc.
     */
    init: function() {
        var self = this;
    }
});