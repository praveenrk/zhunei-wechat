var articleCtrl = $.mvc.controller.create('article', {
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